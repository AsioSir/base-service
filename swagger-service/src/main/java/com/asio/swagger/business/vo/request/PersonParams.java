package com.asio.swagger.business.vo.request;

import com.asio.tools.streamutils.model.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author: asio
 * @time: 2021-07-29 22:12
 * 描述:  person查询条件
 */
@ApiModel("person查询条件")
public class PersonParams {

    @ApiModelProperty("客户姓名")
    String name;
    @ApiModelProperty("客户性别")
    SexEnum sexEnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SexEnum getSexEnum() {
        return sexEnum;
    }

    public void setSexEnum(SexEnum sexEnum) {
        this.sexEnum = sexEnum;
    }
}
