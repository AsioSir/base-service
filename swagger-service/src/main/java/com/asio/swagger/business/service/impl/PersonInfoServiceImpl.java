package com.asio.swagger.business.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asio.swagger.business.generator.extend.PersonInfoExtMapper;
import com.asio.swagger.business.generator.model.PersonInfo;
import com.asio.swagger.business.service.PersonInfoService;
import com.asio.swagger.business.vo.request.PersonParams;
import com.asio.tools.loggerutils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: asio
 * @time: 2021-07-29 21:48
 * 描述:  服务实现类
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoExtMapper personInfoExtMapper;

    /**
     * Description: 查询Person
     * @author asio
     * @time 2021-07-29 16:03
     * @param params 查询条件
     */
    public PersonInfo selectPersonInfoByName(PersonParams params) throws Exception {
        if (StrUtil.isEmpty(params.getName()) || params.getSexEnum() == null ) {
            LoggerUtils.error("查询参数不完整，params={}", params);
            return null;
        }
        if (StrUtil.isNotEmpty(params.getName())) {
            return personInfoExtMapper.selectPersonInfoByName(params.getName(), params.getSexEnum().getValue());
        }
        return null;
    }

}
