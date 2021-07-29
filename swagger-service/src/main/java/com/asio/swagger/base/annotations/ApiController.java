package com.asio.swagger.base.annotations;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author: asio
 * @time: 2021-07-29 22:23
 * 描述:  Api注解基类,用于拦截请求验证，内部接口调用
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface ApiController {
}
