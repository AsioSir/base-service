package com.asio.swagger.business.generator.model;

/**
 * @author: asio
 * @time: 2021-07-29 21:23
 * 描述: 举例 数据库逆向生成的实体
 */
public class PersonInfo {
    //姓名
    String name;
    //年龄
    Integer age;
    //体重
    Double weight;
    //性别值
    Integer sex;

    public PersonInfo(String name, Integer age, Double weight, Integer sex) {
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
        this.sex = sex;
    }
}
