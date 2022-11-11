package com.asio.tools.excelutils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Excel xlsx文件生成类
 * @Author 王盛
 * @Date 2021-07-23 14:58
 */
public class ExcelExportXlsx {

    private SXSSFWorkbook workbook;

    private SXSSFSheet defaultSheet;

    private Font defaultHeaderFont;

    private Font defaultContentFont;

    private XSSFCellStyle defaultHeaderStyle;

    private CellStyle defaultContentStyle;

    private final int columnWidth = 15;

    private final short rowHeight = 300;

    private Map<String, String> headerMapping;

    private Map<String, String> headerNewMapping;

    private Collection<?> contents;

    private Map<String, XSSFColor> CUSTOM_COLOR = new HashMap<>();

    /**
     * @param headerMapping 有序请定义LinkedHashMap
     * @param contents 表格正文内容集合
     */
    public ExcelExportXlsx(Map<String, String> headerMapping, Collection<?> contents) {
        this.headerMapping = headerMapping;
        this.contents = contents;
        this.init();
    }

    private void init() {
        // 创建一个表格
        this.workbook = new SXSSFWorkbook();
        // 打开压缩功能 防止占用过多磁盘
        this.workbook.setCompressTempFiles(true);
        // 创建一个工作区
        this.defaultSheet = workbook.createSheet("default");
        // 设置表格默认列宽度为15个字节
        this.defaultSheet.setDefaultColumnWidth(columnWidth);
        this.defaultSheet.setDefaultRowHeight(rowHeight);

        // 自定义一个颜色
        CUSTOM_COLOR.put("jlc_gray", new XSSFColor(new java.awt.Color(218, 222, 232)));

        // 创建默认标题栏字体
        this.createDefaultHeaderFont();
        this.createDefaultContentFont();
        // 创建默认标题栏的样式
        this.createDefaultHeaderStyle();
        // 创建默认内容样式
        this.createDefaultContentStyle();
    }

    private void createDefaultHeaderFont() {
        this.defaultHeaderFont = this.workbook.createFont();
        this.defaultHeaderFont.setColor(HSSFColorPredefined.BLACK.getIndex());
        this.defaultHeaderFont.setFontHeightInPoints((short) 9.5d);
        this.defaultHeaderFont.setBold(true);
    }

    private void createDefaultContentFont() {
        this.defaultContentFont = this.workbook.createFont();
        this.defaultContentFont.setColor(HSSFColorPredefined.BLACK.getIndex());
        this.defaultContentFont.setFontHeightInPoints((short) 9d);
    }

    private void createDefaultHeaderStyle() {
        this.defaultHeaderStyle = (XSSFCellStyle) this.workbook.createCellStyle();
        this.defaultHeaderStyle.setFillForegroundColor(CUSTOM_COLOR.get("jlc_gray"));
        this.defaultHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.defaultHeaderStyle.setBorderBottom(BorderStyle.THIN);
        this.defaultHeaderStyle.setBorderLeft(BorderStyle.THIN);
        this.defaultHeaderStyle.setBorderRight(BorderStyle.THIN);
        this.defaultHeaderStyle.setBorderTop(BorderStyle.THIN);
        this.defaultHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        this.defaultHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.defaultHeaderStyle.setFont(this.defaultHeaderFont);
    }

    private void createDefaultContentStyle() {
        // 定义纯文本列格式
        this.defaultContentStyle = this.workbook.createCellStyle();
        this.defaultContentStyle.setAlignment(HorizontalAlignment.CENTER);
        this.defaultContentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        DataFormat format = workbook.createDataFormat();
        this.defaultContentStyle.setDataFormat(format.getFormat("@"));
        this.defaultContentStyle.setFont(this.defaultContentFont);
    }

    private void initHeader() {
        // 设置标题栏
        Row row = this.defaultSheet.createRow(0);
        row.setHeight((short) 460);

        headerNewMapping = new LinkedHashMap<String, String>();

        int headerCollIndex = 0;
        if (MapUtil.isNotEmpty(headerMapping)) {
            for (Map.Entry<String, String> mapping : headerMapping.entrySet()) {
                if (StrUtil.isEmpty(mapping.getKey())) {
                    continue;
                }

                Cell headerCell = row.createCell(headerCollIndex);
                headerCell.setCellStyle(this.defaultHeaderStyle);
                headerCell.setCellValue(mapping.getValue());

                String[] innerColumns = mapping.getKey().split(",");
                if (innerColumns.length == 2) {
                    this.defaultSheet.setColumnWidth(headerCollIndex, Integer.parseInt(innerColumns[1]) * 256);
                }
                headerNewMapping.put(innerColumns[0], null);
                headerCollIndex ++;
            }
            // 设置冻结第一行，下滑时表头保持不动
            this.defaultSheet.createFreezePane(0, 1, 0, 1);
        }
    }

    private void fillContent() {
        // 设置表内容
        if (CollectionUtil.isNotEmpty(this.contents)) {
            int contentRowIndex = 1;
            try {
                for (Object contentRow : this.contents) {
                    Class<?> beanClass = contentRow.getClass();
                    SXSSFRow row = this.defaultSheet.createRow(contentRowIndex);
                    row.setHeight((short) 300);

                    int contentCollIndex = 0;
                    Map<String, Field> fieldMap = ReflectUtil.getFieldMap(beanClass);
                    for (Map.Entry<String, String> mapping : headerNewMapping.entrySet()) {
                        Field field = fieldMap.get(mapping.getKey());
                        Object value = null;
                        if (field != null) {
                            field.setAccessible(true);
                            PropertyDescriptor descriptor = BeanUtil.getPropertyDescriptor(beanClass, field.getName());
                            value = descriptor.getReadMethod().invoke(contentRow);
                        }

                        SXSSFCell cell = this.defaultSheet.getRow(contentRowIndex).createCell(contentCollIndex);
                        cell.setCellStyle(this.defaultContentStyle);
                        if (value == null) {
                            cell.setCellValue(new XSSFRichTextString(""));
                        } else {
                            if (field.getType() == Date.class) {
                                Date date = (Date) value;
                                String fieldValue = DateUtil.format(date, DatePattern.NORM_DATETIME_FORMAT);
                                cell.setCellValue(new XSSFRichTextString(fieldValue));

                            } else if (field.getType() == Boolean.class) {
                                Boolean fieldValue = (Boolean) value;
                                cell.setCellValue(new XSSFRichTextString(fieldValue ? "是" : "否"));

                            } else if (field.getType() == BigDecimal.class) {
                                BigDecimal fieldValue = (BigDecimal) value;
                                cell.setCellValue(new XSSFRichTextString(NumberUtil.toStr(fieldValue, Boolean.TRUE)));

                            } else {
                                cell.setCellValue(new XSSFRichTextString(StrUtil.isEmpty(value.toString()) ? "" : value.toString()));
                            }

                        }
                        contentCollIndex ++;
                    }

                    contentRowIndex ++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buildContent() {
        // 创建标题栏
        this.initHeader();
        // 填充数据内容
        this.fillContent();
    }

    public void buildStream(OutputStream outputStream) {
        try {
            this.buildContent();
            this.workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("创建excel文件失败", e);
        } finally {
            if (this.workbook != null) {
                try {
                    this.workbook.dispose();
                    this.workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void buildHttpResponse(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            String agent = request.getHeader("USER-AGENT").toLowerCase();
            if (agent.contains("firefox")) {
                response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx" );
            } else {
                response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.DEFAULT.encode(fileName, Charset.forName("UTF-8")) +".xlsx");
            }
            this.buildStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, String> headerMapping = new LinkedHashMap<>();
        headerMapping.put("index,6", "序号");
        headerMapping.put("name", "供应商名称");
        headerMapping.put("name1", "供应商编码");
        headerMapping.put("name2", "结算组织");
        headerMapping.put("name3", "是否睡着了");

        List<Test> arrayList = new ArrayList<>();
        arrayList.add(new Test(1, "奥术大师多", BigDecimal.valueOf(100.12), new Date(), true));
        arrayList.add(new Test(2, "奥术大师多", BigDecimal.valueOf(99.334152), new Date(), false));
        arrayList.add(new Test(3, "奥术大师多", BigDecimal.valueOf(65), new Date(), true));

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\win10\\Desktop\\abc.xlsx");
        new ExcelExportXlsx(headerMapping, arrayList).buildStream(fileOutputStream);
    }

    public static class Test {
        private Integer index;
        private String name;
        private BigDecimal name1;
        private Date name2;
        private Boolean name3;

        public Test(Integer index, String name, BigDecimal name1, Date name2, Boolean name3) {
            this.index = index;
            this.name = name;
            this.name1 = name1;
            this.name2 = name2;
            this.name3 = name3;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getName1() {
            return name1;
        }

        public void setName1(BigDecimal name1) {
            this.name1 = name1;
        }

        public Date getName2() {
            return name2;
        }

        public void setName2(Date name2) {
            this.name2 = name2;
        }

        public Boolean getName3() {
            return name3;
        }

        public void setName3(Boolean name3) {
            this.name3 = name3;
        }
    }

}



