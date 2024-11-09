package com.asio.tools.excelutils.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.asio.exception.BusinessException;
import com.asio.tools.excelutils.PropertyFeatureMappingUtil;
import com.asio.tools.excelutils.entity.ExcelTemplateXmlEntity;
import com.asio.tools.excelutils.entity.ExcelXmlToBoEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;
/**
 * @author: leijun
 * @creat: 2024-11-01 21:19
 * 描述:
 */
public class EasyExcelDataWithXmlListener<G, T> extends AnalysisEventListener<T> {

    // 从第几行行开始读: 从 0 开始计数，默认第一行是标题行
    private Integer startRowNum = 1;

    // 读取截止行数
    private Integer endRowNum = null;

    // 当前第几行
    private Integer currentRowNum = 0;

    // 总条数
    private int totalNum = 0;

    // 默认批次条数
    private int BATCH_COUNT = 10000;

    // xml模板转换对象信息
    private ExcelTemplateXmlEntity excelTemplateXmlEntity = new ExcelTemplateXmlEntity();

    // 读取的信息
    List<T> excelDateList = new ArrayList<>();

    // 读取的数据类型
    Class<G> dataClazz;

    // 解析的信息
    List<G> list = new ArrayList<>();

    // 分批读取快递信息处理方法
    Consumer<List<G>> consumer;




    public EasyExcelDataWithXmlListener(Consumer<List<G>> consumer) {
        this.consumer = consumer;
    }


    public EasyExcelDataWithXmlListener(Consumer<List<G>> consumer, ExcelTemplateXmlEntity excelTemplateXmlEntity, Class<G> dataClazz) {
        this.consumer = consumer;
        this.dataClazz = dataClazz;
        this.excelTemplateXmlEntity = excelTemplateXmlEntity;
    }




    public void setStartRowNum(int startRowNum) {
        this.startRowNum = startRowNum;
    }

    public void setEndRowNum(int endRowNum) {
        this.endRowNum = endRowNum;
    }

    public void setBatchCount(int batchCount) {
        this.BATCH_COUNT = batchCount;
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // 获取当前实际读取行数
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        currentRowNum = readRowHolder.getRowIndex();

        ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();
        String sheetName = readSheetHolder.getSheetName();

        if (null == excelTemplateXmlEntity) {
            throw new BusinessException("未获取到Excel文件表空间XML配置：" + sheetName);
        }

        // 当前行数小于起始行数，跳过
        if (currentRowNum < startRowNum - 1) {
            return;
        }

        // 当前行数大于截止行数，跳过
        if (null != endRowNum && currentRowNum > endRowNum - 1) {
            return;
        }

        String modelClassPath = excelTemplateXmlEntity.getModelClassPath();
        if (!modelClassPath.equals(dataClazz.getName())) {
            throw new BusinessException(StrUtil.format("读取Excel异常,对象异常,xml配置对象class：{}，系统配置：{}", modelClassPath, dataClazz.getName()));
        }

        List<ExcelXmlToBoEntity> excelXmlToBoEntityList = excelTemplateXmlEntity.getExcelXmlToBoEntityList();
        if (CollUtil.isEmpty(excelXmlToBoEntityList)) {
            throw new BusinessException("读取Excel异常,未配置对象属性字段映射信息");
        }

        // 设置属性字段值
        LinkedHashMap<Integer, String> excelCellDataMap = (LinkedHashMap<Integer, String>) t;
        try {
            G targetObj = dataClazz.newInstance();
            for (ExcelXmlToBoEntity excelXmlToBoEntity : excelXmlToBoEntityList) {
                Integer columIndex = excelXmlToBoEntity.getColumIndex();
                String fieldName = excelXmlToBoEntity.getFieldName();
                try {
                    Field field = ReflectUtil.getField(dataClazz, fieldName);
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Type fieldType = TypeUtil.getType(field);

                    // 代码中是从0开始计算的
                    String cellValue = excelCellDataMap.get(columIndex - 1);
                    if (StrUtil.isNotBlank(cellValue)) {
                        // 转换为目标对象属性
                        Object filedValue = PropertyFeatureMappingUtil.changeFieldTypeValue(cellValue, fieldType);
                        field.set(targetObj, filedValue);
                    }
                    field.setAccessible(accessible);
                } catch (Exception e) {
                    throw new BusinessException(StrUtil.format("解析第{}列值到对象属性{}异常，{}", columIndex, fieldName, e.getMessage()));
                }

            }
            list.add(targetObj);
        } catch (Exception e) {
            throw new BusinessException(StrUtil.format("读取Excel解析第{}行信息异常，error:{}" , currentRowNum, e.getMessage()));
        }

        if (list.size() >= BATCH_COUNT) {
            accept();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollUtil.isNotEmpty(list)) {
            accept();
            list.clear();
        }
    }



    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {

    }



    private void accept() {
        if (CollUtil.isNotEmpty(list)) {
            totalNum += list.size();
        }
        consumer.accept(list);
    }


    public int getTotalNum() {
        return totalNum;
    }

}