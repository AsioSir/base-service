package com.asio.swagger.business.controller;

import cn.hutool.json.JSONUtil;
import com.asio.swagger.base.annotations.WebApiController;
import com.asio.swagger.business.api.PersonInfoApi;
import com.asio.swagger.business.generator.model.PersonInfo;
import com.asio.swagger.business.service.PersonInfoService;
import com.asio.swagger.business.vo.request.PersonParams;
import com.asio.tools.loggerutils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: asio
 * @time: 2021-07-29 22:15
 * 描述:  swagger controller
 */
@WebApiController
@RequestMapping("/person")
public class PersonInfoController implements PersonInfoApi {

    @Autowired
    private PersonInfoService personInfoService;

    @Override
    @PostMapping("/getInfo")
    public PersonInfo getPerson(@RequestBody PersonParams params) throws Exception {
        try {
            return personInfoService.selectPersonInfoByName(params);
        } catch (Exception e) {
            LoggerUtils.error("查询Person异常，params={}", JSONUtil.toJsonStr(params), e);
            return null;
        }
    }
}
