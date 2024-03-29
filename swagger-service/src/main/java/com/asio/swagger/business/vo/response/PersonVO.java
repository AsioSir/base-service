package com.asio.swagger.business.vo.response;

import com.asio.tools.enumutils.EnumUtils;
import com.asio.tools.streamutils.model.SexEnum;

/**
 * @author: asio
 * @time: 2021-07-27 20:06
 * 描述:  去除或新增前端不方便展示的数据
 */
public class PersonVO {

    //姓名
    String name;
    //年龄
    Integer age;
    //体重
    Double weight;
    //性别值
    Integer sex;
    //性别描述
    String sexDesc;

    public PersonVO(String name, Integer age, Double weight, Integer sex) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.setSex(sex);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        //设置性别值
        if (sex != null) {
            SexEnum sexEnum = EnumUtils.valueOf(SexEnum.class, EnumUtils.FIELD_VALUE, sex);
            this.setSexDesc(sexEnum == null ? null : sexEnum.getName());
        }
        this.sex = sex;
    }

    public String getSexDesc() {
        return sexDesc;
    }

    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
    }
}
