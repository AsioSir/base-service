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
    private K value;

    //对应value值
    private V name;

    public EnumProperty(K value, V name) {
        this.value = value;
        this.name = name;
    }


    public static <K, V> EnumProperty<K, V> of(BaseEnum baseEnum, String fieldType1, String fieldType2) {
        Method keyMethod = null;
        boolean isKeyMethodAccessible = true;
        Method valueMethod = null;
        boolean isValueMethodAccessible = true;
        try {
            if (EnumUtil.FIELD_VALUE.equals(fieldType1)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getValue");
            } else if (EnumUtil.FIELD_CODE.equals(fieldType1)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getCode");
            } else if (EnumUtil.FIELD_NAME.equals(fieldType1)) {
                keyMethod = baseEnum.getClass().getDeclaredMethod("getName");
            }
            if (EnumUtil.FIELD_VALUE.equals(fieldType2)) {
                valueMethod = baseEnum.getClass().getDeclaredMethod("getValue");
            } else if (EnumUtil.FIELD_CODE.equals(fieldType2)) {
                valueMethod = baseEnum.getClass().getDeclaredMethod("getCode");
            } else if (EnumUtil.FIELD_NAME.equals(fieldType2)) {
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

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
    }

    public V getName() {
        return name;
    }

    public void setName(V name) {
        this.name = name;
    }
}
