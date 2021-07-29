package com.asio.swagger.base.annotations;

import java.lang.annotation.*;

/**
 * @author: asio
 * @time: 2021-07-29 22:22
 * 描述:  WebApi前端注解,主要拦截请求做验证，此api用于web
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiController
public @interface WebApiController {
}
