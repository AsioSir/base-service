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

