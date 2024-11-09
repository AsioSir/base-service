package com.asio.tools.excelutils.annotions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: leijun
 * @creat: 2024-10-28 13:37
 * 描述:属性映射字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FeatureMapping {


    /**
     * 属性字段描述
     */
    String desc();

}
