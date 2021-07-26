package com.asio.tools.enumutils.enums;

/**
 * @author: asio
 * @time: 2021-07-26 13:38
 * 描述:  测试枚举类
 */
public enum TestEnum implements BaseEnum<TestEnum, Integer> {

    JAVA(1, "java", "Java分类"),
    ANDROID(2, "android", "Android分类"),
    PYTHON(3, "python", "Python分类"),
    ;

    TestEnum(Integer value, String code, String name) {
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
