package com.asio.tools.excelutils.entity;

/**
 * @author: leijun
 * @creat: 2024-11-01 21:54
 * 描述:Xml配置转为Bo对象
 */
public class ExcelXmlToBoEntity {

    /**
     * Description: 列下标 从0开始计算
     */
    private Integer columIndex;

    /**
     * Description: 属性名
     */
    private String fieldName;


    public Integer getColumIndex() {
        return columIndex;
    }

    public void setColumIndex(Integer columIndex) {
        this.columIndex = columIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}