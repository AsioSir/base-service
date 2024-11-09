package com.asio.tools.excelutils.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.asio.tools.excelutils.ExcelXmlParseUtil;
import com.asio.tools.excelutils.entity.ExcelTemplateXmlEntity;
import com.asio.tools.excelutils.listener.EasyExcelDataWithXmlListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlEasyExcelService<T> {


    public List<T> readExcel(File file, String xmlContent, Class<T> boClazz) {

        List<ExcelTemplateXmlEntity> excelTemplateXmlEntitieList = ExcelXmlParseUtil.readXmlEntity(xmlContent);
        String suffix = FileNameUtil.getSuffix(file.getName());


        List<T> targetDataList = new ArrayList<>();

        for (ExcelTemplateXmlEntity excelTemplateXmlEntity : excelTemplateXmlEntitieList) {

            // 自定义监听器分批读取
            EasyExcelDataWithXmlListener<T, ?> excelDataListener = new EasyExcelDataWithXmlListener<>(partList -> {
                targetDataList.addAll(partList);
            }, excelTemplateXmlEntity, boClazz);


            if (null != excelTemplateXmlEntity.getStartRow()) {
                excelDataListener.setStartRowNum(excelTemplateXmlEntity.getStartRow());
            }

            if (null != excelTemplateXmlEntity.getEndRow()) {
                excelDataListener.setEndRowNum(excelTemplateXmlEntity.getEndRow());
            }

            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.XLSX.getValue().contains(suffix) ? ExcelTypeEnum.XLSX : ExcelTypeEnum.XLS;

            // 表空间名
            String sheetName = excelTemplateXmlEntity.getSheetName();
            InputStream is = FileUtil.getInputStream(file);
            EasyExcel.read(is).headRowNumber(0).registerReadListener(excelDataListener).ignoreEmptyRow(true)
                    .excelType(excelTypeEnum).sheet(sheetName).autoTrim(true).doReadSync();
        }

        return targetDataList;
    }


}