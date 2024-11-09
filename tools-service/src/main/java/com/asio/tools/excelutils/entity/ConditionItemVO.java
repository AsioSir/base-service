package com.asio.tools.excelutils.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: leijun
 * @creat: 2021-07-22 19:57
 * 描述: 下来框条件项VO
 */
@ApiModel(description = "下拉框条件子项VO")
public class ConditionItemVO implements Serializable {

    @ApiModelProperty("item的值")
    private String value;

    @ApiModelProperty("item的描述")
    private String label;

    public ConditionItemVO(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
