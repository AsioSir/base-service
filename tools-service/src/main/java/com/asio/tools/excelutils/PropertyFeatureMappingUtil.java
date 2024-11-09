package com.asio.tools.excelutils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.asio.constant.DefaultValueConstant;
import com.asio.exception.BusinessException;
import com.asio.tools.excelutils.enums.FeatureMappingTypeEnum;
import com.asio.tools.excelutils.model.PropertyFeatureMapping;
import com.asio.tools.loggerutils.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nfunk.jep.JEP;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: leijun
 * @creat: 2024-10-25 19:48
 * 描述:属性字段映射工具类
 */
public class PropertyFeatureMappingUtil {

    // 尝试转换的日期格式： yyyy-MM-dd HH:mm:ss | yyyyMMdd | yyyy-MM-dd | yyyyMM | yyyy-MM | yyyy年MM月dd日
    private final static List<String> TRY_FORMAT_DATE_PATTERN = Arrays.asList(DatePattern.NORM_DATETIME_PATTERN, DatePattern.PURE_DATE_PATTERN,
            DatePattern.NORM_DATE_PATTERN, DatePattern.SIMPLE_MONTH_PATTERN, DatePattern.NORM_MONTH_PATTERN, DatePattern.CHINESE_DATE_PATTERN,
            "yyy/MM/dd HH:mm:ss", "yyyy/MM/dd");

    private final static List<Class<?>> DEAL_FIELD_TYPE_LIST = Arrays.asList(Integer.class, Long.class, Float.class, Double.class,
            Boolean.class, Short.class, Date.class, String.class, BigDecimal.class);

    public static Object convertBasePropertyValue(PropertyFeatureMapping propertyFeatureMapping, Object obj, Map<String, String> targetValueMap) throws Exception {

        String featureType = propertyFeatureMapping.getFeatureType();
        String propertyName = propertyFeatureMapping.getPropertyName();
        String featureRule = propertyFeatureMapping.getFeatureRule();

        Class<?> objClazz = obj.getClass();

        Field field;
        try {
            field = objClazz.getDeclaredField(propertyName);
            if (null == field) {
                throw new BusinessException("字段映射异常：不存在属性字段:" + propertyName);
            }
        } catch (NoSuchFieldException e) {
            throw new BusinessException("字段映射异常：不存在属性字段:" + propertyName);
        }

        // 目标字段类型
        Type targetFieldType = TypeUtil.getType(field);

        Object targetValue;

// 取枚举值 或 字典值
        if (FeatureMappingTypeEnum.ENUMS.getValue().equals(featureType) || FeatureMappingTypeEnum.DICTIONARY.getValue().equals(featureType)) {
            targetValue = convertPropertyByDictionaryMap(propertyFeatureMapping, obj, targetFieldType, targetValueMap);

        } else {
            // 按规则转换字段值
            targetValue = convertPropertyByRule(featureType, featureRule, obj, propertyName, targetFieldType);
        }

        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(obj, targetValue);
        field.setAccessible(accessible);
        return targetValue;
    }


    /**
     * Description: 转换枚举/字典等key-value类型值
     * @author leijun
     * @create 2024-10-28 17:41
     * @param propertyFeatureMapping 映射关系表
     * @param targetFieldType 目标字段属性
     * @param targetValueMap 目标值集合
     * @return Object
     */
    private static Object convertPropertyByDictionaryMap(PropertyFeatureMapping propertyFeatureMapping, Object obj, Type targetFieldType,
                                                         Map<String, String> targetValueMap) throws Exception {

        if (MapUtils.isEmpty(targetValueMap)) {
            return null;
        }

        String featureType = propertyFeatureMapping.getFeatureType();
        String targetKeyField = propertyFeatureMapping.getTargetKeyField();
        String targetValueField = propertyFeatureMapping.getTargetValueField();
        String featureRule = propertyFeatureMapping.getFeatureRule();
        if (StrUtil.isEmpty(targetKeyField) || StrUtil.isEmpty(targetValueField)) {
            throw new BusinessException("字段映射异常：未配置枚举或字典项取值字段，" + featureRule);
        }

        // 规则去除左右括号
        String rules = substringByBrackets(featureRule);
        String[] splits = rules.split(DefaultValueConstant.E_COMMA);

// 原属性字段
        String originalFieldName = splits[0];
// 枚举全路径 或 字典项Id
        String target = splits[1];

// 取枚举字段值
        if (FeatureMappingTypeEnum.ENUMS.getValue().equals(featureType) && MapUtils.isEmpty(targetValueMap)) {
            targetValueMap = getEnumMap(target, targetKeyField, targetValueField);
        }

        ImmutablePair<Object, Type> targetValuePair = getObjFieldValue(obj, originalFieldName);
        Object targetValue = targetValuePair.left;
        if (null == targetValue) {
            return null;
        }

        String targetValueStr = targetValueMap.get(targetValue.toString());
        if (StrUtil.isEmpty(targetValueStr)) {
            throw new BusinessException("解析映射枚举值异常：");
        }

        return changeFieldTypeValue(targetValueStr, targetFieldType);
    }


    /**
     * Description: 按规则表达式解析字段值
     * @author leijun
     * @create 2024-10-28 17:33
     * @param featureType 规则类型
     * @param featureRule 规则内容
     * @param obj 取值对象
     * @param propertyName 原始取值字段
     * @param targetFieldType 目标字段类型
     * @return Object
     */

    private static Object convertPropertyByRule(String featureType, String featureRule, Object obj, String propertyName, Type targetFieldType) throws Exception {

        String originalValue = null;
        ImmutablePair<Object, Type> fieldValuePair = getObjFieldValue(obj, propertyName);
        Object fieldValue = fieldValuePair.left;
        if (null != fieldValue) {
            if (Date.class.equals(fieldValuePair.right)) {
                originalValue = DateUtil.format((Date) fieldValue, DatePattern.NORM_DATETIME_PATTERN);
            } else {
                originalValue = fieldValue.toString();
            }
        }

        if (!isFieldBaseType(targetFieldType)) {
            throw new BusinessException("字段映射异常：仅转换基本数据类型，当前数据类型：" + targetFieldType);
        }

        // 默认值
        String rules = substringByBrackets(featureRule);
        if (FeatureMappingTypeEnum.STR.getValue().equals(featureType)) {
            return changeFieldTypeValue(rules, targetFieldType);
        }

        // 字符串拼接前缀
        if (FeatureMappingTypeEnum.PRE_STR.getValue().equals(featureType)) {
            return preOrSufStrFun(rules, originalValue, obj, true);
        }

        // 字符串拼接后缀
        if (FeatureMappingTypeEnum.SUF_STR.getValue().equals(featureType)) {
            return preOrSufStrFun(rules, originalValue, obj, false);
        }


        // 替换字符串值
        if (FeatureMappingTypeEnum.REPLACE.getValue().equals(featureType)) {
            String[] splitRules = rules.split(DefaultValueConstant.E_COMMA);
            String originalStr = splitRules[0];
            String targetStr = splitRules[1];
            return StrUtil.isEmpty(originalValue) ? null : originalValue.replaceAll(originalStr, targetStr);
        }

        // 转换为大写
        if (FeatureMappingTypeEnum.UPPER_CASE.getValue().equals(featureType)) {
            return StrUtil.isEmpty(originalValue) ? null : originalValue.toUpperCase();
        }

        // 转换首字母大写
        if (FeatureMappingTypeEnum.UPPER_FIRST_CASE.getValue().equals(featureType)) {
            return StrUtil.upperFirst(originalValue);
        }

        // 转换为小写
        if (FeatureMappingTypeEnum.LOWER_CASE.getValue().equals(featureType)) {
            return StrUtil.isEmpty(originalValue) ? null : originalValue.toLowerCase();
        }

        // 转换首字母小写
        if (FeatureMappingTypeEnum.LOWER_FIRST_CASE.getValue().equals(featureType)) {
            return StrUtil.lowerFirst(originalValue);
        }

        // 转换日期格式
        if (FeatureMappingTypeEnum.FORMAT_DATE.getValue().equals(featureType)) {
            return formatDateFun(rules, obj, targetFieldType);
        }


        // 转换为日期
        if (FeatureMappingTypeEnum.TO_DATE.getValue().equals(featureType)) {
            return toDateFun(rules, obj);
        }

        // 转为日期 MM-dd 或 MM/dd 或 mmDD
        if (FeatureMappingTypeEnum.TO_DATE_DEF_YYYY.getValue().equals(featureType)) {
            return toDateDefYYYYFun(rules, obj);
        }


        // 数字计算：BigDecimal
        if (FeatureMappingTypeEnum.CALC.getValue().equals(featureType)) {
            String calcExpression = replaceCalcExpression(rules, obj);
            return executeCalcExpression(calcExpression);
        }

        // IF_EQUALS判断
        if (FeatureMappingTypeEnum.IF_EQUALS.getValue().equals(featureType)) {
            return ifEqualsExpressionFun(featureRule, obj);
        }

        throw new BusinessException("属性字段值转换异常，未知的转换类型：", featureType);
    }



    /**
     * Description: 获取枚举值转换为Map
     * @param enumClassPath 枚举对象全路径
     * @param mapKeyField 作为mapKey的属性字段
     * @param mapValueField 作为mapvALUE的属性字段
     * @return Map
     */
    private static Map<String, String> getEnumMap(String enumClassPath, String mapKeyField, String mapValueField) throws Exception {
        Class<?> enumClass = Class.forName(enumClassPath);
        Object[] enumConstants = enumClass.getEnumConstants();
        Map<String, String> enumMap = new HashMap<>();

        for (Object enumConst : enumConstants) {
            // 获取枚举常量的方法
            Method keyMethod = enumConst.getClass().getMethod("get" + StrUtil.upperFirst(mapKeyField));
// 调用方法获取枚举常量的位置
            String key = StrUtil.toString(keyMethod.invoke(enumConst));

// 获取枚举常量的方法
            Method valueMethod = enumConst.getClass().getMethod("get" + StrUtil.upperFirst(mapValueField));
// 调用方法获取枚举常量的位置
            String value = StrUtil.toString(valueMethod.invoke(enumConst));
            enumMap.put(key, value);
        }

        return enumMap;
    }


    /**
     * Description: 转换属性字段类型
     * @author leijun
     * @create 2024-10-26 15:11
     * @param originalValue 属性值
     * @param targetType 目标类型
     */
    public static Object changeFieldTypeValue(String originalValue, Type targetType) {
        try {
            // 字符串
            if (String.class.equals(targetType)) {
                return originalValue;
            }

            if (StrUtil.isBlank(originalValue)) {
                return null;
            }

            originalValue = originalValue.trim();

// Boolean
            if (Boolean.class.equals(targetType)) {
                return BooleanUtil.toBoolean(originalValue);
            }

            // 日期格式
            if (Date.class.equals(targetType)) {
                return parseDate(originalValue);
            }

            /* 以下是浮点型数据 */
            // Float
            if (Float.class.equals(targetType)) {
                return Float.parseFloat(originalValue);
            }

            // Double
            if (Double.class.equals(targetType)) {
                return Double.parseDouble(originalValue);
            }

            // BigDecimal
            if (BigDecimal.class.equals(targetType)) {
                return new BigDecimal(originalValue);
            }

            /* 以下是整型数据 */
            // 避免1.00无法解析，先转为BigDecimal
            BigDecimal bigDecimal = new BigDecimal(originalValue);

// Short
            if (Short.class.equals(targetType)) {
                return bigDecimal.shortValue();
            }

            // Integer
            if (Integer.class.equals(targetType)) {
                return bigDecimal.intValue();
            }

            // Long
            if (Long.class.equals(targetType)) {
                return bigDecimal.longValue();
            }

        } catch (Exception e) {
            throw new BusinessException(StrUtil.format("转换属性类型异常，属性值：{}， 目标类型：{}", originalValue, targetType.getTypeName()));
        }

        throw new BusinessException("转换属性类型异常，未知的数据类型：" + targetType.getTypeName());
    }



    /**
     * Description: 拼接前后字符串
     * @author leijun
     * @create 2024-10-31 19:17
     * @param rules 规则 拼接字段 或 [取值属性,拼接字段]
     * @param originalValue 原字段值
     * @param obj 对象
     * @param preFlag 是否拼接前字符串
     */
    private static String preOrSufStrFun(String rules, String originalValue, Object obj, boolean preFlag) throws Exception {

        // 取特征值拼接字符串
        String[] splitRules = rules.split(DefaultValueConstant.E_COMMA);
        if (splitRules.length == 1) {
            return preFlag ? StrUtil.emptyToDefault(rules, "") + StrUtil.emptyToDefault(originalValue, "") :
                    StrUtil.emptyToDefault(originalValue, "") + StrUtil.emptyToDefault(rules, "");
        }

        String propertyName = splitRules[0];
        String padStr = splitRules[1];

        ImmutablePair<Object, Type> propertyValuePair = getObjFieldValue(obj, propertyName);
        String propertyValue = null == propertyValuePair.left ? "" : propertyValuePair.left.toString();

        return preFlag ? padStr + propertyValue : propertyValue + padStr;
    }


    /**
     * Description: 转换日期格式
     * @author leijun
     * @create 2024-10-28 17:22
     * @return Date / String /Integer
     */
    private static Object formatDateFun(String rules, Object obj, Type targetFieldType) throws Exception {
        try {
            String[] splitRules = rules.split(DefaultValueConstant.E_COMMA);
            String propertyName = splitRules[0].trim();
            String dateFormat = splitRules[1].trim();

// 读取待转换属性值
            ImmutablePair<Object, Type> fieldValuePair = getObjFieldValue(obj, propertyName);
            Object fieldValue = fieldValuePair.left;
            if (null == fieldValue) {
                return null;
            }

            // 读取的数据是日期类型
            if (Date.class.equals(fieldValuePair.right)) {
                if (Date.class.equals(targetFieldType)) {
                    return fieldValue;
                }
                return changeFieldTypeValue(DateUtil.format((Date) fieldValue, dateFormat), targetFieldType);
            }

            // 读取的非日期类型，先转换为日期，再转换为目标类型
            Date targetDate = parseDate(fieldValue.toString());
            if (Date.class.equals(targetFieldType)) {
                return targetDate;
            }


            return changeFieldTypeValue(DateUtil.format(targetDate, dateFormat), targetFieldType);

        } catch (Exception e) {
            LoggerUtils.error("解析formatDate日期表达式异常,rule:{}, error:", rules, e);
            throw new BusinessException("解析日期表达是异常,rule:{}", rules);
        }
    }


    /**
     * Description: 转换为日期方法
     * @param rules 取值字段-日期格式
     * @param obj 取值对象
     * @return Date
     */
    private static Date toDateFun(String rules, Object obj) throws Exception {
        ImmutablePair<Object, Type> fieldValuePair = getObjFieldValue(obj, rules);
        Object fieldValue = fieldValuePair.left;
        if (null == fieldValue) {
            return null;
        }

        if (Date.class.equals(fieldValuePair.right)) {
            return (Date) fieldValue;
        }

        return parseDate(fieldValue.toString());
    }



    /**
     * Description: 转换为日期方法 MMdd 或 MM/dd 或 MM-dd
     * @param rules 取值字段-日期格式
     * @param obj 取值对象
     * @return Date
     */
    private static Date toDateDefYYYYFun(String rules, Object obj) throws Exception {
        ImmutablePair<Object, Type> fieldValuePair = getObjFieldValue(obj, rules);
        Object fieldValue = fieldValuePair.left;
        if (null == fieldValue) {
            return null;
        }

        int year = DateUtil.year(new Date());
        String targetDateStr = "";
        if (fieldValue.toString().contains(DefaultValueConstant.E_SLASH)) {
            targetDateStr = year + DefaultValueConstant.E_SLASH + fieldValue;
        } else if (fieldValue.toString().contains(DefaultValueConstant.E_RUNG)) {
            targetDateStr = year + DefaultValueConstant.E_RUNG + fieldValue;
        } else {
            targetDateStr = year + fieldValue.toString();
        }

        return parseDate(targetDateStr);
    }



    /**
     * Description: 字段判断表达式（等于）
     * @author leijun
     * @create 2024-10-28 10:09
     * @param expression ifEqual表达式
     * @param obj 对象信息
     * @return 匹配值
     */
    public static String ifEqualsExpressionFun(String expression, Object obj) throws Exception {
        try {
            String switchValue = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")")).trim();
            ImmutablePair<Object, Type> filedValuePair = getObjFieldValue(obj, switchValue);
            Object filedValue = filedValuePair.left;
            String fieldValue = null == filedValue ? "" : filedValue.toString();

            String defaultValue = null;
            if (expression.contains("default")) {
                defaultValue = expression.substring(expression.indexOf("default") + 7, expression.lastIndexOf(";")).trim();
                defaultValue = defaultValue.replaceAll("\"", "");
            }

            String[] splitList = expression.substring(expression.indexOf("{"), expression.lastIndexOf("}")).split(";");
            Map<String, String> caseMap = new HashMap<>();
            for (String splitStr : splitList) {
                if (!splitStr.contains("when")) {
                    continue;
                }
                String whenValue = splitStr.substring(splitStr.indexOf("when") + 4, splitStr.lastIndexOf("then")).trim();
                whenValue = whenValue.replaceAll("\"", "");

                String caseValue = splitStr.substring(splitStr.indexOf("then") + 4).trim();
                caseValue = caseValue.replaceAll("\"", "");
                caseMap.put(whenValue, caseValue);
            }

            return StrUtil.emptyToDefault(caseMap.get(fieldValue), defaultValue);
        } catch (Exception e) {
            LoggerUtils.error("解析ifEqual表达式异常：{}：error:", expression, e);
            throw new BusinessException(StrUtil.format("解析ifEqual表达式异常：{}", expression));
        }

    }



    public static String replaceCalcExpression(String expression, Object obj) throws Exception {
        String[] splitList = expression.split("[+-]|[*/]|[()]");

        try {
            for (String fieldName : splitList) {
                fieldName = fieldName.trim();
                if (StrUtil.isBlank(fieldName) || !expression.contains(fieldName)) {
                    continue;
                }
                if (!NumberUtil.isNumber(fieldName)) {
                    ImmutablePair<Object, Type> fieldValuePair = getObjFieldValue(obj, fieldName);
                    Object fieldValue = fieldValuePair.left;
                    expression = expression.replaceAll(fieldName, null != fieldValue ? fieldValue.toString() : "0");
                }
            }
            return expression;
        } catch (Exception e) {
            LoggerUtils.error("解析计算表达式异常：{}, error:", expression, e);
            throw new BusinessException(StrUtil.format("解析计算表达式异常：{}", expression));
        }

    }



    /**
     * Description: 执行加减乘除表达式计算
     * @author leijun
     * @create 2024-10-26 17:57
     * @param expression 四则运算表达式
     * @return 执行结果
     */
    public static BigDecimal executeCalcExpression(String expression) {
        try {
            JEP jep = new JEP();
            jep.parseExpression(expression);

            if (jep.hasError()) {
                throw new BusinessException("执行加减乘除四则运算异常：" + expression);
            } else {
                BigDecimal result = NumberUtil.toBigDecimal(jep.getValueAsObject().toString());
                return result.setScale(6, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            LoggerUtils.error("执行加减乘除四则运算异常：{}，error:", expression, e);
            throw new BusinessException("执行加减乘除四则运算异常：" + expression);
        }
    }


    public static ImmutablePair<Object, Type> getObjFieldValue(Object obj, String fieldName) throws Exception {
        Class<?> objClazz = obj.getClass();
        try {
            Field declaredField = objClazz.getDeclaredField(fieldName);
            if (null == declaredField) {
                throw new BusinessException(StrUtil.format("对象 {} 无 {} 属性字段", objClazz.getName(), fieldName));
            }

            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);

            Object filedValue = declaredField.get(obj);
            declaredField.setAccessible(accessible);
            return ImmutablePair.of(filedValue, TypeUtil.getType(declaredField));
        } catch (NoSuchFieldException e) {
            throw new BusinessException(StrUtil.format("对象 {} 无 {} 属性字段", objClazz.getName(), fieldName));
        }
    }

    /**
     * Description: 转换日期格式
     */
    public static Date parseDate(String dateStr) {
        if (StrUtil.isEmpty(dateStr)) {
            return null;
        }

        for (String formatPattern : TRY_FORMAT_DATE_PATTERN) {
            try {
                return DateUtil.parse(dateStr, formatPattern);

            } catch (Exception ignored) {
                // 忽略异常
            }
        }

        throw new BusinessException(StrUtil.format("尝试转换日期失败：date:{},pattern:{}", dateStr, TRY_FORMAT_DATE_PATTERN));
    }



    /**
     * Description: 判断是否是可解析的类型属性
     */
    private static boolean isFieldBaseType(Type type) {
        return DEAL_FIELD_TYPE_LIST.contains(type);
    }



    /**
     * Description: 截取括号内的内容
     */
    public static String substringByBrackets(String value) {
        if (StrUtil.isEmpty(value)) {
            return value;
        }

        int startIndex = value.indexOf("(");

        int lastIndex = value.lastIndexOf(")");
        lastIndex = lastIndex < 0 ? value.length() : lastIndex;

        return value.substring(startIndex+1,lastIndex).trim();
    }


    public static String checkBoFeatureProperty(String boFullPath, String propertyName) {
        try {
            Class<?> objClazz = Class.forName(boFullPath);
            objClazz.getDeclaredField(propertyName);
            return null;
        } catch (Exception e) {
            LoggerUtils.error("校验BO属性异常：{}, error:", boFullPath, e);
            return "校验BO属性异常：属性对象 或 属性字段 不存在";
        }

    }

    public static void main(String[] args) throws Exception {

// System.out.println(checkBoFeatureProperty("com.szjlc.payable1.business.common.vo.request.PropertyFeatureMappingEditForm", "basicShippingFee"));

 /* String expression = "calc((basicShippingFee+attachmentFee+otherServiceFee)*1.1 + fuelFee)";
 String calcExpression = substringByBrackets(expression);
 System.out.println(calcExpression);

 ExpressTemplateExcelBO expressTemplateExcelBO1 = new ExpressTemplateExcelBO();
 expressTemplateExcelBO1.setExpressNo("EX000001");
 expressTemplateExcelBO1.setProductTypeName(ExpressProductTypeEnum.NEXT_DAY_DELIVERY.getName());
 expressTemplateExcelBO1.setBasicShippingFee(new BigDecimal("23.25"));
 expressTemplateExcelBO1.setAttachmentFee(new BigDecimal("1.25"));
 expressTemplateExcelBO1.setOtherServiceFee(new BigDecimal("2.12"));
 expressTemplateExcelBO1.setCurrencyName("RMB");
 expressTemplateExcelBO1.setDeliverTimeStr("2024/09/14");

 calcExpression = replaceCalcExpression(calcExpression, expressTemplateExcelBO1);
 System.out.println(calcExpression);

 System.out.println(executeCalcExpression(calcExpression));*/
    }

}