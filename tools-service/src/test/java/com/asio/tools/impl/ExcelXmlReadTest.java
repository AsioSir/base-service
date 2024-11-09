package com.asio.tools.impl;

import cn.hutool.core.io.FileUtil;
import com.asio.tools.BaseToolAppTest;
import com.asio.tools.excelutils.bo.ExpressTemplateExcelBO;
import com.asio.tools.excelutils.model.PropertyFeatureMapping;
import com.asio.tools.excelutils.service.PropertyFeatureMappingBizService;
import com.asio.tools.excelutils.service.XmlEasyExcelService;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExcelXmlReadTest extends BaseToolAppTest {


    @Test
    public void testMethod() throws Exception {
        // 按xml模版读取excel数据
        File file = FileUtil.file("D:\\yourFileName.xlsx");
        XmlEasyExcelService<ExpressTemplateExcelBO> xmlEasyExcelService = new XmlEasyExcelService<>();
        List<ExpressTemplateExcelBO> expressTemplateExcelBOList = xmlEasyExcelService.readExcel(file, getXmlData(), ExpressTemplateExcelBO.class);

        // 按熟悉特征规则映射字段
        PropertyFeatureMappingBizService propertyFeatureMappingBizService = new PropertyFeatureMappingBizService();
        // 获取映射信息
        List<PropertyFeatureMapping> propertyFeatureMappingList = new ArrayList<>();
        propertyFeatureMappingBizService.convertExpressTemplateExcelBOList(expressTemplateExcelBOList, propertyFeatureMappingList);
    }



    private static String getXmlData() {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<workbook>\n" +
                "    <worksheet name=\"明细\">\n" +
                "        <loop startRow=\"2\" items=\"expressDatas1\"\n" +
                "              var=\"model\"\n" +
                "              varType=\"com.szjlc.payable.business.express.pojo.bo.ExpressTemplateExcelBO\">\n" +
                "            <section>\n" +
                "                <mapping col=\"3\">model.expressNo</mapping>\n" +
                "                <mapping col=\"9\">model.settlementOrganizeName</mapping>\n" +
                "                <mapping col=\"12\">model.deliverTimeStr</mapping>\n" +
                "                <mapping col=\"14\">model.deliverProvince</mapping>\n" +
                "                <mapping col=\"15\">model.deliverCity</mapping>\n" +
                "                <mapping col=\"16\">model.receiveProvince</mapping>\n" +
                "                <mapping col=\"17\">model.receiveCity</mapping>\n" +
                "                <mapping col=\"27\">model.weight</mapping>\n" +
                "                <mapping col=\"29\">model.volume</mapping>\n" +
                "                <mapping col=\"34\">model.packageQuantity</mapping>\n" +
                "                <mapping col=\"37\">model.productTypeName</mapping>\n" +
                "                <mapping col=\"54\">model.totalAmount</mapping>\n" +
                "                <mapping col=\"21\">model.expressSettlementTypeName</mapping>\n" +
                "            </section>\n" +
                "        </loop>\n" +
                "    </worksheet>\n" +
                "</workbook>";
        return xmlData;
    }

}
