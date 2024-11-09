package com.asio.tools.excelutils.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 映射类型枚举
 */
public enum FeatureMappingTypeEnum {
    DICTIONARY("dictionary", "嘉立创字典项值", "dictionary(取值属性字段,字典项Id)"),
    ENUMS("enum", "枚举项", "enum(取值属性字段,枚举类全路径)"),
    PRE_STR("preStr", "字符串加前缀", "preStr(propertyName, aaa"),
    SUF_STR("sufStr", "字符串加后缀", "sufStr(propertyName, aaa"),
    STR("str", "字符串", "str(propertyName)"),
    REPLACE("replace", "替换字符串", "replace(aa,bb)"),
    UPPER_CASE("upperCase", "转换大写", "upperCase()"),
    UPPER_FIRST_CASE("upperFirstCase", "转换首字母大写", "upperFirstCase()"),
    LOWER_CASE("lowerCase", "转换小写", "lowerCase()"),
    LOWER_FIRST_CASE("lowerFirstCase", "转换首字母小写", "lowerFirstCase()"),
    FORMAT_DATE("formatDate", "转换日期格式", "formatDate(propertyName, \"yyyy-MM-dd mm:hh:ss\")"),
    TO_DATE("toDate", "转换为日期", "toDate(propertyName)"),
    TO_DATE_DEF_YYYY("toDateDefYYYY()", "MMdd/MM-dd转日期", "toDateDefYYYY(propertyName)"),
    CALC("calc", "加减乘除计算", "calc((a + b) * c)"),
    IF_EQUALS("ifEquals", "ifEquals判断", "ifEquals(propertyName){\n" + " when a then 1;\n" + " when b then 2;\n" + " default 3;\n" + "}"),
    ;

    private final String value;
    private final String name;

    private final String example;

    private static Map<String, FeatureMappingTypeEnum> VALUE_MAP;

    static {
        VALUE_MAP = Arrays.stream(FeatureMappingTypeEnum.values()).collect(Collectors.toMap(FeatureMappingTypeEnum::getValue, Function.identity()));
    }

    public static FeatureMappingTypeEnum getByValue(String value) {
        return VALUE_MAP.get(value);
    }

    FeatureMappingTypeEnum(String value, String name, String example) {
        this.value = value;
        this.name = name;
        this.example = example;
    }

    public String getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getExample() {
        return example;
    }
}