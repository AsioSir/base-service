package com.asio.tools.enumutils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.asio.tools.enumutils.enums.BaseEnum;
import com.asio.tools.enumutils.model.EnumProperty;
import com.asio.tools.loggerutils.LoggerUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举工具类
 */
public class EnumUtil {

    public static final String FIELD_VALUE = "value";

    public static final String FIELD_CODE = "code";

    public static final String FIELD_NAME = "name";

    /**
     * @author asio
     * @time 2021-07-26 13:54
     * @param enumClass 指定枚举类
     * @param fieldType 枚举属性类型
     * @param fieldValue 枚举属性值
     * description: 获取指定枚举类指定类型值的枚举
     */
    public static <E extends BaseEnum, T> E valueOf(Class<E> enumClass, String fieldType, T fieldValue) {
        BaseEnum[] enumConstants = enumClass.getEnumConstants();
        for (BaseEnum baseEnum : enumConstants) {
            if (FIELD_VALUE.equals(fieldType)) {
                if (baseEnum.getValue().equals(fieldValue)) {
                    return (E) baseEnum;
                }
            } else if (FIELD_CODE.equals(fieldType)) {
                if (baseEnum.getCode().equals(fieldValue)) {
                    return (E) baseEnum;
                }
            } else if (FIELD_NAME.equals(fieldType)) {
                if (baseEnum.getName().equals(fieldValue)) {
                    return (E) baseEnum;
                }
            }
        }
        return null;
    }

    /**
     * @author asio
     * @time 2021-07-27 11:07
     * @param enumClass 枚举类class(BaseEnum子类)
     * @param keyFiledType key属性
     * @param valueFiledType value属性
     * description: 将枚BaseEnum子类举中的字段转为key-value对象【EnumProperty】集合
     */
    public static <E extends BaseEnum, K, V>List<EnumProperty<K, V>> toBaseEnumKeyAndValueList(Class<E> enumClass, String keyFiledType, String valueFiledType) {
        BaseEnum[] enumConstants = enumClass.getEnumConstants();
        List<EnumProperty<K,V>> resultList = new ArrayList<>(enumConstants.length);
        for (BaseEnum baseEnum : enumConstants) {
            resultList.add(EnumProperty.of(baseEnum, keyFiledType, valueFiledType));
        }
        return resultList;
    }

    /**
     * @author asio
     * @time 2021-07-27 14:07
     * @param enumClass 枚举类class(常规枚举类)
     * @param keyFieldName key属性
     * @param nameFieldName value属性
     * description: 将常规枚举中的字段转为key-value对象【EnumProperty】集合
     */
    public static <E, K, V> List<EnumProperty<K, V>> toCommonEnumKeyAndValueList(Class<E> enumClass, String keyFieldName, String nameFieldName) {
        E[] enumConstants = enumClass.getEnumConstants();
        List<EnumProperty<K, V>> resultList = new ArrayList<>(enumConstants.length);
        for (E elementEnum : enumConstants) {
            K keyValue = (K)ReflectUtil.getFieldValue(elementEnum, keyFieldName);
            V nameValue = (V)ReflectUtil.getFieldValue(elementEnum, nameFieldName);
            resultList.add(new EnumProperty<K, V>(keyValue, nameValue));
        }
        return resultList;
    }

    /**
     * @author asio
     * @time 2021-07-27 14:11
     * @param enumClass 枚举类class
     * @param fieldName 转为list集合的字段名
     * description: 将常规枚举中的指定字段转为list集合
     */
    public static <E, U> List<U> listEnumField(Class<E> enumClass, String fieldName) {
        E[] enumConstants = enumClass.getEnumConstants();
        List<U> resultList = new ArrayList<>();
        for (E elementEnum : enumConstants) {
            U fieldValue;
            try {
                Method method = enumClass.getMethod("get".concat(StrUtil.upperFirst(fieldName)));
                fieldValue = (U)method.invoke(elementEnum);
                resultList.add(fieldValue);
            } catch (Exception e) {
                LoggerUtils.error("转list反射调用获取枚举值失败：", e);
            }
        }
        return resultList;
    }

    /**
     * @author asio
     * @time 2021-07-27 14:22
     * @param enumClass 枚举类class
     * @param keyFieldName key值枚举属性
     * description: 将常规枚举按指定字段为key，枚举对象为value转为map集合
     */
    public static <E, K> Map<K, E> enumToMap(Class<E> enumClass, String keyFieldName) {
        E[] enumConstants = enumClass.getEnumConstants();
        Map<K, E> resultMap = new HashMap<>();
        for (E elementEnum : enumConstants) {
            K keyValue;
            try {
                keyValue = (K)enumClass.getMethod("get".concat(StrUtil.upperFirst(keyFieldName))).invoke(elementEnum);
                resultMap.put(keyValue, elementEnum);
            } catch (Exception e) {
                LoggerUtils.error("转map反射调用获取枚举值失败：", e);
            }
        }
        return resultMap;
    }

    /**
     * @author asio
     * @time 2021-07-27 14:24
     * @param enumClass 枚举类class
     * @param keyFieldName key值枚举属性
     * @param valueFieldName value值枚举属性
     * description: 将常规枚举按指定字段为key，指定字段为value转为map集合
     */
    public static <E, K, V> Map<K, V> enumToMap(Class<E> enumClass, String keyFieldName, String valueFieldName) {
        E[] enumConstants = enumClass.getEnumConstants();
        Map<K, V> resultMap = new HashMap<>();
        for (E elementEnum : enumConstants) {
            K keyValue;
            V value;
            try {
                keyValue = (K)enumClass.getMethod("get".concat(StrUtil.upperFirst(keyFieldName))).invoke(elementEnum);
                value = (V)enumClass.getMethod("get".concat(StrUtil.upperFirst(valueFieldName))).invoke(elementEnum);
                resultMap.put(keyValue, value);
            } catch (Exception e) {
                LoggerUtils.error("转map反射调用获取枚举值失败：", e);
            }
        }
        return resultMap;
    }



    /**
     * @author asio
     * @time 2021-07-27 14:32
     * description: 测试EnumUtil
     */
    public static void main(String[] args) {
        // 获取指定枚举类指定类型值的枚举
        /*TestEnum testEnum = EnumUtil.valueOf(TestEnum.class, EnumUtil.FIELD_CODE, "java");*/

        //将枚BaseEnum子类举中的字段转为key-value对象【EnumProperty】集合
        /* List<EnumProperty<Integer, String>> enumProperties = EnumUtil.toBaseEnumKeyAndValueList(TestEnum.class, EnumUtil.FIELD_VALUE, EnumUtil.FIELD_NAME);
        System.out.println(JSONUtil.toJsonStr(enumProperties));*/

        //将常规枚举中的字段转为key-value对象【EnumProperty】集合
        /*List<EnumProperty<String, Double>> commonEnumKeyAndValueList = EnumUtil.toCommonEnumKeyAndValueList(CommonEnum.class, "code", "price");
        System.out.println(JSONUtil.toJsonStr(commonEnumKeyAndValueList));
        System.out.println(commonEnumKeyAndValueList.get(0).getValue() instanceof Double);*/

        //将常规枚举中的指定字段转为list集合
       /* List<String> commonNameList = EnumUtil.listEnumField(CommonEnum.class, CommonEnum.FIELD_NAME);
        List<String> commonPriceList = EnumUtil.listEnumField(CommonEnum.class, CommonEnum.FIELD_PRICE);
        System.out.println(commonNameList + " -- " + commonPriceList);*/

        //将常规枚举按指定字段为key，枚举对象为value转为map集合
        /*Map<String, CommonEnum> commonEnumMap = EnumUtil.enumToMap(CommonEnum.class, "name");
        System.out.println(commonEnumMap);
        System.out.println("苹果价格: " + commonEnumMap.get("苹果").getPrice());*/

        //将常规枚举按指定字段为key，指定字段为value转为map集合
        /*Map<String, Double> nameAndPriceMap = EnumUtil.enumToMap(CommonEnum.class, "name", "price");
        System.out.println(nameAndPriceMap);
        System.out.println("苹果价格: " + nameAndPriceMap.get("苹果"));*/
    }


}
