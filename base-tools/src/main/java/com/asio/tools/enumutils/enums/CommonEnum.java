package com.asio.tools.enumutils.enums;

/**
 * @author: asio
 * @time: 2021-07-27 13:39
 * 描述:  通常类枚举
 */
public enum CommonEnum {
    APPLE   ("apple", "苹果", 5.68),
    BANANA  ("banana", "香蕉", 2.28);

    CommonEnum(String code, String name, Double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PRICE = "price";

    private String code;

    private String name;

    private Double price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
