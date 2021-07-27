package com.asio.tools.enumutils.model;

import com.asio.tools.enumutils.EnumUtil;
import com.asio.tools.enumutils.enums.BaseEnum;
import com.asio.tools.loggerutils.LoggerUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author: asio
 * @time: 2021-07-26 14:03
 * 描述:  枚举 key-value 值
 */
public class EnumProperty<K, V> implements Serializable {

    private static  final long serialVersionUID = 20210725120000L;

    //对应key值
    private K key;

    //对应value值
    private V value;

    public EnumProperty(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @author asio
     * @time 2021-07-27 11:09
     * @param baseEnum 枚举类class[基类]
     * @param keyFieldType
     * @param valueFieldType
     * description: 将枚举中的字段转为key-value对象【EnumProperty】
     */
    public static <K, V> EnumProperty<K, V> of(BaseEnum baseEnum, String keyFieldType, String valueFieldType) {
        Method keyMethod = null;
        boolean isKeyMethodAccessible = true;
        Method valueMethod = null;
        boolean isValueMethodAccessible = true;
        try {
            if (EnumUtil.FIELD_VALUE.equals(keyFieldType)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getValue");
            } else if (EnumUtil.FIELD_CODE.equals(keyFieldType)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getCode");
            } else if (EnumUtil.FIELD_NAME.equals(keyFieldType)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getName");
            }
            if (EnumUtil.FIELD_VALUE.equals(valueFieldType)) {
                valueMethod = baseEnum.getClass().getDeclaredMethod("getValue");
            } else if (EnumUtil.FIELD_CODE.equals(valueFieldType)) {
                valueMethod = baseEnum.getClass().getDeclaredMethod("getCode");
            } else if (EnumUtil.FIELD_NAME.equals(valueFieldType)) {
                valueMethod = baseEnum.getClass().getDeclaredMethod("getName");
            }
            if (keyMethod != null && valueMethod != null) {
                isKeyMethodAccessible = keyMethod.isAccessible();
                keyMethod.setAccessible(true);
                isValueMethodAccessible = valueMethod.isAccessible();
                valueMethod.setAccessible(true);
            } else {
                LoggerUtils.error("获取枚举key-value异常, 映射方法失败");
                return null;
            }
            return new EnumProperty(keyMethod.invoke(baseEnum), valueMethod.invoke(baseEnum));
        } catch (Exception e) {
            LoggerUtils.error("获取枚举key-value异常：" + e);
        } finally {
            //恢复方法可见性
            if (keyMethod != null && valueMethod != null) {
                keyMethod.setAccessible(isKeyMethodAccessible);
                valueMethod.setAccessible(isValueMethodAccessible);
            }
        }
        return null;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
