package com.asio.tools.enumutils;

import cn.hutool.json.JSONUtil;
import com.asio.tools.enumutils.enums.BaseEnum;
import com.asio.tools.enumutils.enums.TestEnum;
import com.asio.tools.enumutils.model.EnumProperty;

import java.util.ArrayList;
import java.util.List;

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

    public static <E extends BaseEnum, K, V>List<EnumProperty<K, V>> toKeyAndValueList(Class<E> enumClass, String filedType1, String filedType2) {
        BaseEnum[] enumConstants = enumClass.getEnumConstants();
        List<EnumProperty<K,V>> resultList = new ArrayList<>(enumConstants.length);
        for (BaseEnum baseEnum : enumConstants) {
            resultList.add(EnumProperty.of(baseEnum, filedType1, filedType2));
        }
        return resultList;
    }


    public static void main(String[] args) {
//        TestEnum testEnum = EnumUtil.valueOf(TestEnum.class, EnumUtil.FIELD_CODE, "java");
        List<EnumProperty<Integer, String>> enumProperties = EnumUtil.toKeyAndValueList(TestEnum.class, EnumUtil.FIELD_VALUE, EnumUtil.FIELD_NAME);
        System.out.println(JSONUtil.toJsonStr(enumProperties));
    }
}
