package com.asio.tools.enumutils.enums;

public interface BaseEnum<E extends Enum<?>, T> {

    //枚举的value值
    T getValue();

    //枚举的code值
    void setCode(String code);
    String getCode();

    //枚举的name值
    void setName(String name);
    String getName();
}
