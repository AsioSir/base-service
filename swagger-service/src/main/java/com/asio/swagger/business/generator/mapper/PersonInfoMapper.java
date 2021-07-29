package com.asio.swagger.business.generator.mapper;

import com.asio.swagger.business.generator.model.PersonInfo;

/**
 * @author: asio
 * @time: 2021-07-29 21:34
 * 描述: 举例 数据库逆向生成的Mapper文件
 */
public interface PersonInfoMapper {
    //新增
    int insertByExample(PersonInfo example);
    //更新
    int updateByExample(PersonInfo example);
    //删除
    int delete(Long keyId);
    //......
}
