package com.asio.swagger.business.service;

import com.asio.swagger.business.generator.model.PersonInfo;
import com.asio.swagger.business.vo.request.PersonParams;

/**
 * @author: asio
 * @time: 2021-07-29 21:49
 * 描述:  服务接口类
 */
public interface PersonInfoService {

    /**
     * Description: 查询Person
     * @author asio
     * @time 2021-07-29 16:03
     * @param params 查询条件
     */
    PersonInfo selectPersonInfoByName(PersonParams params) throws Exception;

}
