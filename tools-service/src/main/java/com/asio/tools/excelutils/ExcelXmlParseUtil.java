package com.asio.tools.excelutils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;

import com.asio.exception.BusinessException;

import com.asio.tools.excelutils.entity.ExcelTemplateXmlEntity;
import com.asio.tools.excelutils.entity.ExcelXmlToBoEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: leijun
 * @creat: 2024-11-04 18:11
 * 描述:表格XML配置解析工具类
 */
public class ExcelXmlParseUtil {

    // 解析xml文件获取映射对象属性字段
    public static List<ExcelTemplateXmlEntity> readXmlEntity(String xmlContent) {
        Document document = XmlUtil.readXML(xmlContent);
        Element root = document.getDocumentElement();
        NodeList worksheetNodeList = root.getElementsByTagName("worksheet");


        List<ExcelTemplateXmlEntity> excelXmlEntityList = new ArrayList<>(worksheetNodeList.getLength());
        for (int loopIndex = 0; loopIndex < worksheetNodeList.getLength(); loopIndex++) {
            Element workElement = (Element) worksheetNodeList.item(loopIndex);
            String sheetName = workElement.getAttribute("name");
            if (StrUtil.isBlank(sheetName)) {
                throw new BusinessException("xml信息解析异常，未配置sheet名称");
            }

            NodeList loopNodeList = workElement.getElementsByTagName("loop");

            Element element = (Element)loopNodeList.item(0);

            String itemsName = element.getAttribute("items");
            String modelClassPath = element.getAttribute("varType");
            String startRow = element.getAttribute("startRow");
            String endRow = element.getAttribute("endRow");

            ExcelTemplateXmlEntity excelTemplateXmlEntity = new ExcelTemplateXmlEntity();
            excelTemplateXmlEntity.setSheetName(sheetName);
            excelTemplateXmlEntity.setItemsName(itemsName);
            excelTemplateXmlEntity.setModelClassPath(modelClassPath);
            if (StrUtil.isNotBlank(startRow) && NumberUtil.isInteger(startRow)) {
                excelTemplateXmlEntity.setStartRow(Integer.parseInt(startRow));
            }
            if (StrUtil.isNotBlank(endRow) && NumberUtil.isInteger(endRow)) {
                excelTemplateXmlEntity.setEndRow(Integer.parseInt(endRow));
            }


            // 映射字段值
            List<ExcelXmlToBoEntity> excelXmlToBoEntityList = new ArrayList<>();
            NodeList mappingNodeList = element.getElementsByTagName("mapping");
            for (int mappingIndex = 0; mappingIndex < mappingNodeList.getLength(); mappingIndex++) {
                Element mappingElement = (Element) mappingNodeList.item(mappingIndex);
                String row = mappingElement.getAttribute("row");
                String col = mappingElement.getAttribute("col");
                String nodeValue = mappingElement.getTextContent();
                if (StrUtil.isBlank(nodeValue) || StrUtil.isEmpty(col) || !NumberUtil.isInteger(col)) {
                    throw new BusinessException("xml信息解析异常，未配置正确的属性映射列");
                }

                ExcelXmlToBoEntity excelXmlToBoEntity = new ExcelXmlToBoEntity();
                excelXmlToBoEntity.setColumIndex(Integer.parseInt(col));
                String[] nodeValueSplit = nodeValue.split("\\.");
                excelXmlToBoEntity.setFieldName(nodeValueSplit[nodeValueSplit.length - 1]);
                excelXmlToBoEntityList.add(excelXmlToBoEntity);
            }
            excelTemplateXmlEntity.setExcelXmlToBoEntityList(excelXmlToBoEntityList);

            excelXmlEntityList.add(excelTemplateXmlEntity);
        }

        return excelXmlEntityList;
    }

}