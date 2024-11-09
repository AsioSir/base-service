package com.asio.tools.excelutils.entity;

import java.util.List;

/**
 * @author: leijun
 * @creat: 2024-10-28 21:58
 * 描述:excel导入xml模版转对象
 */
public class ExcelTemplateXmlEntity {

    /**
     * Description: 表空间名称
     */
    private String sheetName;

    /**
     * Description: 对象全路径
     */
    private String modelClassPath;

    /**
     * Description: 子项对象名称
     */
    private String itemsName;

    /**
     * Description: 开始行数
     */
    private Integer startRow;

    /**
     * Description: 截止行数
     */
    private Integer endRow;

    /**
     * Description: 读取对象映射配置信息
     */
    private List<ExcelXmlToBoEntity> excelXmlToBoEntityList;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getModelClassPath() {
        return modelClassPath;
    }

    public void setModelClassPath(String modelClassPath) {
        this.modelClassPath = modelClassPath;
    }

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public List<ExcelXmlToBoEntity> getExcelXmlToBoEntityList() {
        return excelXmlToBoEntityList;
    }

    public void setExcelXmlToBoEntityList(List<ExcelXmlToBoEntity> excelXmlToBoEntityList) {
        this.excelXmlToBoEntityList = excelXmlToBoEntityList;
    }
}