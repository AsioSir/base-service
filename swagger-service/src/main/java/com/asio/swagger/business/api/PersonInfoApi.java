package com.asio.swagger.business.api;

import com.asio.swagger.business.generator.model.PersonInfo;
import com.asio.swagger.business.vo.request.PersonParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

/**
 * @author: asio
 * @time: 2021-07-29 22:00
 * 描述:  swagger api
 */
@Api(value = PersonInfoApi.TAGS, description = "PersonApi", tags = PersonInfoApi.TAGS)
public interface PersonInfoApi {

    String TAGS = "personApi";

    final static String API_KEY = "api_key";

    @ApiOperation(value = "查询条件", httpMethod = "Post", tags = TAGS, authorizations = @Authorization(API_KEY))
    PersonInfo getPerson(@ApiParam(value = "person查询条件")PersonParams params) throws Exception;

}
