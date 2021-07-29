package com.asio.tools.streamutils.model;

import com.asio.tools.enumutils.enums.BaseEnum;

/**
 * @author: asio
 * @time: 2021-07-27 20:09
 * 描述:  性别枚举
 */
public enum SexEnum implements BaseEnum {
    BOY     (1, "boy", "男"),
    GIRL     (2, "girl", "女"),
    ;

    SexEnum(Integer value, String code, String name) {
        this.value = value;
        this.code = code;
        this.name = name;
    }

    private Integer value;
    private String code;
    private String name;

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
