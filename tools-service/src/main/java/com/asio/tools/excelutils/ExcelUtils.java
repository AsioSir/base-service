package com.asio.tools.excelutils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asio.annotation.ExcelCell;
import com.asio.constant.DefaultValueConstant;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Excel工具类
 *
 * @Author 王盛
 * @Date 2021-07-24 11:11
 **/
public class ExcelUtils {

    /**
     * 加载Excel文件
     * @Author 王盛
     * @Date 2021-07-26 13:46
     */
    public static Workbook readExcel(String fileName, InputStream inputStream) {
        if (StrUtil.isEmpty(fileName) || inputStream == null) {
            return null;
        }
        try {
            // 获取文件后缀
            String suffix = FileUtil.getSuffix(fileName);
            if ("xls".equalsIgnoreCase(suffix)) {
                return new HSSFWorkbook(inputStream);
            }

            if ("xlsx".equalsIgnoreCase(suffix)) {
                return new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取标题行数据（第一行）
     * @Author 王盛
     * @Date 2021-07-26 13:46
     */
    public static List<String> getTitleList(Sheet sheet) {
        // 获取标题行
        Row row = sheet.getRow(0);
        // 获取总列数
        short cellNum = row.getLastCellNum();

        List<String> titleList = new ArrayList<>(cellNum);
        for (short i = 0; i < cellNum; i++) {
            // 获取列的内容
            Cell cell = row.getCell(i);
            titleList.add(cell.getRichStringCellValue().getString().trim());
        }
        return titleList;
    }

    /**
     * 验证表格中的标题栏（第一行）是否正确
     * @Author 王盛
     * @Date 2021-07-26 13:41
     */
    public static boolean validTitle(Sheet sheet, List<String> titleList) {
        List<String> originalTitleList = getTitleList(sheet);
        // 首先对比长度
        if (titleList.size() != originalTitleList.size()) {
            return false;
        }
        // 逐行对比
        for (int i = 0; i < titleList.size(); i++) {
            boolean isSame = titleList.get(i).equals(originalTitleList.get(i));
            if (!isSame) return false;
        }
        return true;
    }

    /**
     * 将Excel数据（不包含第一行）提取出来，并转换成对象集合（适合数据量小的场景，数据量大会导致OOM）
     * @Author 王盛
     * @Date 2021-07-26 13:44
     */
    public static <T> List<T> getDataList(Sheet sheet, Class<T> tClass) throws Exception {
        // 获取总行数
        int rowNum = sheet.getLastRowNum();

        Field[] fields = ReflectUtil.getFieldsDirectly(tClass, Boolean.TRUE);
        Map<Short, Field> tClassFieldMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelCell.class)) continue;
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            tClassFieldMap.put(excelCell.value(), field);
        }

        List<T> tList = new ArrayList<>(rowNum + 1);

        // 循环遍历行
        for (int i = 1; i < rowNum + 1; i++) {
            Row row = sheet.getRow(i);

            if (null == row) continue;

            // 获取总列数
            short cellNum = row.getLastCellNum();

            // 创建对象
            T t = null;

            // 循环遍历列
            for (short j = 0; j < cellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                Field field = tClassFieldMap.get(j);
                if (field == null) continue;

                if (t == null) t = tClass.newInstance();

                PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(t.getClass(), field.getName());
                Class<?> fieldType = field.getType();
                CellType cellType = cell.getCellType();
                Object value = null;
                if (cellType == CellType.NUMERIC) {
                    BigDecimal bgValue = Convert.toBigDecimal(cell.getNumericCellValue());
                    if (bgValue != null) value = bgValue.toPlainString();

                } else if (cellType == CellType.STRING) {
                    value = cell.getRichStringCellValue().getString();

                } else if (cellType == CellType.BOOLEAN) {
                    value = cell.getBooleanCellValue();

                } else if (cellType == CellType.FORMULA) {
                    try {
                        value = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        value = String.valueOf(cell.getRichStringCellValue());
                    }
                }

                if (value == null) {
                    continue;
                }

                if (fieldType == String.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toStr(value));
                } else if (fieldType == Byte.class || fieldType == byte.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toByte(value));
                } else if (fieldType == Short.class || fieldType == short.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toShort(value));
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toInt(value));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toLong(value));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toLong(value));
                } else if (fieldType == Float.class || fieldType == float.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toLong(value));
                } else if (fieldType == BigDecimal.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toBigDecimal(value));
                } else if (fieldType == Boolean.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toBool(value));
                } else if (fieldType == Date.class) {
                    propertyDescriptor.getWriteMethod().invoke(t, Convert.toDate(value));
                }
            }
            if (t != null) {

                String jsonData = JSONUtil.toJsonPrettyStr(t);
                if (!"{}".equals(jsonData)) {
                    // 避免行出现空内容数据
                    tList.add(t);
                }
            }
        }
        return tList;
    }


    /**
     * Description: 检查导入Excel文件
     * @author leijun
     * @create 2022-09-23 10:49
     * @param file 导入excel文件
     * @param headTabList 标题
     * @param clazz 解析的类
     * @return [是否解析成功，说明， 解析数据]
     */
    public static <T> ImmutableTriple<Boolean, String, List<T>> validateExcelFile(MultipartFile file, List<String> headTabList, Class<T> clazz) throws Exception {
        List<T> importDataList = new ArrayList<>();
        String fileSuffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!("xls".equalsIgnoreCase(fileSuffix) || "xlsx".equalsIgnoreCase(fileSuffix))) {
            return ImmutableTriple.of(false,"解析失败，文件格式不支持，目前只支持 xls 和 xlsx", importDataList);
        }
        // 判断文件大小限制
        if (file.getSize() > DefaultValueConstant.DEFAULT_EXCEL_FILE_SIZE) {
            return ImmutableTriple.of(false,"解析失败，文件大小限制10M", importDataList);
        }
        // 解析Excel文件成对象
        Workbook workbook = ExcelUtils.readExcel(file.getOriginalFilename(), file.getInputStream());
        if (workbook == null) {
            return ImmutableTriple.of(false,"解析失败，Excel文件内容不正确", importDataList);
        }
        // 获取默认的工作空间
        Sheet sheet = workbook.getSheetAt(0);
        // 验证标题栏是否正确
        boolean validTitleFlag = ExcelUtils.validTitle(sheet, headTabList);
        if (!validTitleFlag) {
            return ImmutableTriple.of(false,"解析失败，表格标题栏内容不正确", importDataList);
        }
        importDataList = Collections.unmodifiableList(ExcelUtils.getDataList(sheet, clazz));

        // 验证数据不能为空
        if (CollUtil.isEmpty(importDataList)) {
            return ImmutableTriple.of(false,"导入Excel内容不能为空", importDataList);
        }

        return ImmutableTriple.of(true, DefaultValueConstant.SUCCESS, importDataList);
    }

}

