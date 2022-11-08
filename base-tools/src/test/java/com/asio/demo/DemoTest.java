package com.asio.demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;

import com.asio.tools.excelutils.ExcelExportXlsx;
import com.asio.tools.excelutils.ExcelUtils;
import com.asio.vo.EmployeePayDetailsInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author: leijun
 * @creat: 2022-09-29 9:25
 * 描述:
 */
public class DemoTest {

    private final static String key = "asio5210";
    private final static String iv = "12365478";
    private final static DES des = new DES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());


    public static void main(String[] args) throws Exception {

// 加解密
        String msg = "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\n" +
                "\n";
//
//        DES des = new DES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
//        // 加密：密文
//        String encrypt = des.encryptBase64(msg);
//        System.out.println(encrypt);
//        // 解密：明文
        String decrypt = des.decryptStr(msg);
        System.out.println(decrypt);


// 序列化表格
//        dealChangeExcelAndBinary("SOFT");

//        String sourceFilePath = "D:\\payment\\Demo\\SourceExcelData.txt";
//        String gzipFilePath = "D:\\payment\\Demo\\ExcelZipData.txt";
//        String unGzipFilePath = "D:\\payment\\Demo\\TargetExcelData.txt";
//
//        // 读取 压缩
//        readGzip(sourceFilePath, gzipFilePath);
//
//        // 压缩 解压
//        writeUnGzip(gzipFilePath, unGzipFilePath);

        // 读序列化文件到表格
//        String excelFileName = "new";
//        List<EmployeePayDetailsInfo> readDataList = readBinaryToExcel2("D:\\payment\\" + excelFileName + "_binary.txt", "D:\\payment\\" + excelFileName + "_result.xlsx",  EmployeePayDetailsInfo.class);
    }



    // 读取压缩
    public static void readGzip(String readFilePath, String gzipFilePath) throws Exception {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            System.out.println("=========== START ZIP =============");
            // 读取文件
            fileReader = new FileReader(readFilePath);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder dataStr = new StringBuilder();
            char[] buffer = new char[1024];
            while ((bufferedReader.read(buffer)) != -1) {
                dataStr.append(buffer);
            }

            // 压缩存放
            String gzipStr = gzip(dataStr.toString());

            fileWriter = new FileWriter(gzipFilePath);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(gzipStr);
            System.out.println("=========== END ZIP =============");
        } catch (Exception e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();;
            }
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    // 解压写入
    public static void writeUnGzip(String gzipFilePath, String unGzipFilePath) throws Exception {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            System.out.println("=========== START UNZIP =============");
            // 读取文件
            fileReader = new FileReader(gzipFilePath);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder dataStr = new StringBuilder();
            char[] buffer = new char[1024];
            while ((bufferedReader.read(buffer)) != -1) {
                dataStr.append(buffer);
            }

            // 解压存放
            String unGzipStr = unGzip(dataStr.toString());

            fileWriter = new FileWriter(unGzipFilePath);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(unGzipStr);
            bufferedWriter.close();
            fileWriter.close();
            System.out.println("=========== END UNZIP =============");
        } catch (Exception e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();;
            }
            if (fileReader != null) {
                fileReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }


    }



    // 压缩
    public static String gzip(String toGzip) {
        return Base64.encode(ZipUtil.gzip(toGzip, CharsetUtil.CHARSET_UTF_8.name()));
    }


    // 解压
    public static String unGzip(String toUnGzip) {
        byte[] decode = Base64.decode(toUnGzip);
        return ZipUtil.unGzip(decode , CharsetUtil.CHARSET_UTF_8.name());
    }




    public static void dealChangeExcelAndBinary(String excelFileName) throws Exception {

        // 读excel文件
        List<EmployeePayDetailsInfo> excelDetailList = readExcel2("D:\\payment\\" + excelFileName + ".xlsx", EmployeePayDetailsInfo.class);

        if (CollUtil.isEmpty(excelDetailList)) {
            return;
        }

        // 写序列化文件
        writeBinaryTxt(excelDetailList, "D:\\payment\\" + excelFileName + "_binary.txt");

        // 读序列化文件到表格
        List<EmployeePayDetailsInfo> readDataList = readBinaryToExcel2("D:\\payment\\" + excelFileName + "_binary.txt", "D:\\payment\\" + excelFileName + "_result.xlsx",  EmployeePayDetailsInfo.class);

        // System.out.println(JsonUtil.toPrettyJson(readDataList));
    }


    public static  <G>List<G> readBinaryToExcel2(String txtFilePath, String excelPath, Class<G> clazz) throws Exception {
        // 创建Excel
        FileOutputStream fileOutputStream = new FileOutputStream(excelPath);

        ExcelWriter writer = null;

        try {
            writer = EasyExcel.write(fileOutputStream, clazz).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("default").build();
            FileInputStream inputStream = new FileInputStream(txtFilePath);
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            List<G> dataList = new ArrayList<>();
            while(inputStream.available() > 0) {
                Object obj = ois.readObject();
                if (null == obj) {
                    continue;
                }
                G data = Convert.convert(clazz, obj);
                dataList.add(data);
            }
            List<List<G>> splitList = CollUtil.split(dataList, 1000);
            for (List<G> partDataList : splitList) {
                writer.write(partDataList, writeSheet);
                fileOutputStream.flush();
            }

            return dataList;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            //关闭流
            if (writer != null) {
                writer.finish();
            }
        }
    }


    @Deprecated
    public static  <G>List<G> readBinaryToExcel(String txtFilePath, String excelPath, Class<G> clazz) throws Exception {
        FileInputStream inputStream = new FileInputStream(txtFilePath);
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        List<G> dataList = new ArrayList<>();
        while(inputStream.available() > 0) {
            Object obj = ois.readObject();
            if (null == obj) {
                continue;
            }
            G data = Convert.convert(clazz, obj);
            dataList.add(data);
        }

        // 创建Excel
        FileOutputStream fileOutputStream = new FileOutputStream(excelPath);
        // 设置标题
        Map<String, String> headerMapping = new LinkedHashMap<>();
        headerMapping.put("supplierName,23", "供应商名称");
        headerMapping.put("supplierCode,10", "供应商编码");
        headerMapping.put("settlementOrganizeName,30", "结算组织");
        headerMapping.put("businessTypeName,10", "业务类型");
        headerMapping.put("businessDesc", "暂估应付单号");
        headerMapping.put("purchaseOrderCode", "采购单号");
        headerMapping.put("businessTime", "业务日期");
        headerMapping.put("materielCode", "应付明细编码");
        headerMapping.put("materielName", "应付明细名称");
        headerMapping.put("specification", "规格");
        headerMapping.put("giftFlag,5", "赠品");
        headerMapping.put("taxPrice,10", "含税单价");
        headerMapping.put("denominatedNum,10", "计价数量");
        headerMapping.put("totalTaxAmount,10", "计价总额");
        headerMapping.put("estimatePayableNum,10", "暂估数量");
        headerMapping.put("taxRate,10", "税率");
        headerMapping.put("totalAmount,10", "不含税总额");
        headerMapping.put("taxAmount,10", "税额");
        headerMapping.put("estimatePayableAmount,10", "暂估总额");
        headerMapping.put("invoiceType", "发票类型");
        headerMapping.put("invoiceCode", "发票号码");
        headerMapping.put("invoiceTotalAmount", "发票不含税金额");
        headerMapping.put("invoiceCesse", "发票税额");
        headerMapping.put("invoiceNum,10", "发票数量");
        headerMapping.put("invoiceTaxPrice,10", "发票物料单价");
        headerMapping.put("invoiceRemark,24", "发票备注");
        headerMapping.put("estimatePayableDetailAccessId,0","安全码");
        new ExcelExportXlsx(headerMapping, dataList).buildStream(fileOutputStream);

        return dataList;
    }

    public static <T>void writeBinaryTxt(List<T> excelDetailList, String filePath) throws Exception {
        ObjectOutputStream osStream = new ObjectOutputStream(new FileOutputStream(filePath));
        for (T t : excelDetailList) {
            osStream.writeObject(t);
        }
        osStream.close();
    }



    public static <T> List<T> readExcel2(String filePath, Class<T> clazz) throws Exception {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        DataExcelListener<T> dataExcelListener = new DataExcelListener<>();
        EasyExcel.read(inputStream, clazz, dataExcelListener).sheet().doRead();
        List<T> excelDataList = dataExcelListener.excelVoList;
        // 验证数据不能为空
        if (CollUtil.isEmpty(excelDataList)) {
            System.out.println("导入Excel内容不能为空！");
            return null;
        }
        return excelDataList;
    }


    @Deprecated
    public static <T> List<T> readExcel(String filePath, Class<T> clazz) throws Exception {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = ExcelUtils.readExcel(file.getName(), inputStream);
        if (workbook == null) {
            System.out.println("解析失败，Excel文件内容不正确");
            return null;
        }
        // 获取默认的工作空间
        Sheet sheet = workbook.getSheetAt(0);
        /*   // 验证标题栏是否正确
        boolean validTitleFlag = ExcelUtils.validTitle(sheet, Arrays.asList("供应商名称", "供应商编码", "结算组织", "业务类型",
                "暂估应付单号", "采购单号", "业务日期", "应付明细编码", "应付明细名称", "规格" ,"赠品", "含税单价", "计价数量", "计价总额",
                "暂估数量", "税率", "不含税总额", "税额", "暂估总额", "发票类型", "发票号码", "发票不含税金额", "发票税额", "发票数量", "发票物料单价", "发票备注", "安全码"));

        if (!validTitleFlag) {
            System.out.println("解析失败，表格标题栏内容不正确");
            return null;
        }*/

        // 提取数据转成对象列表
        List<T> excelDataList = Collections.unmodifiableList(ExcelUtils.getDataList(sheet, clazz));
        // 验证数据不能为空
        if (CollUtil.isEmpty(excelDataList)) {
            System.out.println("导入Excel内容不能为空！");
            return null;
        }

        return excelDataList;
    }



    static class DataExcelListener<U> extends AnalysisEventListener<U> {

        // excel数据
        List<U> excelVoList = new ArrayList<>();

        public DataExcelListener() {
        }

        /**
         * 每次从Excel读取一行数据都会调用
         **/
        @Override
        public void invoke(U inStockCostImportExcelVo, AnalysisContext analysisContext) {

            // excel原始行号
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            // 保存数据
            excelVoList.add(inStockCostImportExcelVo);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

    }
}

