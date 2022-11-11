package com.asio.tools.jsonutil;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Json字符串工具类
 */
public class JsonUtil {
    /**
     * 将bean序列化为Json字符串，忽略空值属性
     * @param bean
     * @return
     * @throws IOException
     */
    public static String toJson(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //忽略null值属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略不能序列化的属性（这个貌似不是很好，容易出现问题）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper.writeValueAsString(bean);
    }

    /**
     * 将bean序列化为Json字符串，忽略空值属性
     * @param bean
     * @return
     * @throws IOException
     */
    public static String toJsonWithOrder(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //忽略null值属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略不能序列化的属性（这个貌似不是很好，容易出现问题）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //按照字段名排序
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,true);
        return objectMapper.writeValueAsString(bean);
    }

    public static String toJsonWrapRoot(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        //忽略null值属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略不能序列化的属性（这个貌似不是很好，容易出现问题）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper.writeValueAsString(bean);
    }

    /**
     * 格式化输出的JSON
     * @param bean
     * @return
     * @throws IOException
     */
    public static String toPrettyJson(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper.writeValueAsString(bean);
    }

    /**
     * 忽略空值或者空白的属性，如{attr: "" }
     * @param bean
     * @return
     * @throws IOException
     */
    public static String toJsonIgnoreEmpty(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper.writeValueAsString(bean);
    }

    public static String toJsonQuietly(Object bean) {
        try {
            return toJson(bean);
        } catch (IOException e) {
            return "toJson failed: "+e.toString()+", bean: "+bean;
        }
    }

    public static String allToJson(Object bean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //包含所有属性，包括空值
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return objectMapper.writeValueAsString(bean);
    }


    public static <T> T fromJson(String json, Class valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //重要：忽略未知属性，兼容性新版本VO产生的Json
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T)objectMapper.readValue(json, valueType);
    }

    public static <T> T fromJson(InputStream input, Class valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T)objectMapper.readValue(input, valueType);
    }

    public static <T> T fromJsonUnwrapRoot(String json, Class valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        //重要：忽略未知属性，兼容性新版本VO产生的Json
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T)objectMapper.readValue(json, valueType);
    }

    public static <T> T fromJsonUnwrapRoot(InputStream input, Class valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T)objectMapper.readValue(input, valueType);
    }

    /**
     * 使用自定义的Type/JavaType来反序列化生成bean，如下面的例子：
     * List<TestVO> list2 = JsonUtil.fromJson(json, new TypeToken< List< TestVO>>(){}.getType());
     * @param json json字符串
     * @param type bean类型描述
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(String json, Type type) throws IOException {

        if(StrUtil.isEmpty(json)){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (type instanceof JavaType) {
            JavaType javaType = (JavaType) type;
            return (T)objectMapper.readValue(json, javaType);
        }else {
            return (T) objectMapper.readValue(json, objectMapper.constructType(type));
        }
    }

    /**
     * 反序列化泛型类型，参考用法：
     * List< UserVO> items2 = JsonUtil.fromJson(json, List.class, UserVO.class);
     * @param json json字符串
     * @param parametrized 返回bean类型
     * @param parameterClasses 泛型参数类型
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> parametrized, Class<?>... parameterClasses) throws IOException {

        if(StrUtil.isEmpty(json)){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        return (T)objectMapper.readValue(json, objectMapper.constructType(javaType));
    }
}
