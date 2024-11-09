package com.asio.tools.excelutils.bo;
import com.asio.tools.excelutils.annotions.FeatureMapping;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: leijun
 * @creat: 2024-10-25 17:59
 * 描述:快递模板Excel对象
 */
public class ExpressTemplateExcelBO {

    @FeatureMapping(desc = "对账期间 yyyyMM")
    private Integer reconciliationPeriod;

    /**
     * Description: 结算公司编号
     */
    private String settlementOrganizeCode;

    @FeatureMapping(desc = "结算公司名称")
    private String settlementOrganizeName;

    @FeatureMapping(desc = "供应商简称")
    private String shortSupplierName;

    @FeatureMapping(desc = "账单标识号")
    private String expressBillNo;

    @FeatureMapping(desc = "快递账号")
    private String expressAccount;

    @FeatureMapping(desc = "运单号")
    private String expressNo;

    @FeatureMapping(desc = "发票号")
    private String invoiceNumber;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @FeatureMapping(desc = "发票日期(yyyy-MM-dd hh:mm:ss)")
    private Date invoiceDate;

    @FeatureMapping(desc = "发票日期字符串")
    private String invoiceDateStr;

    @FeatureMapping(desc = "包裹件数")
    private Integer packageQuantity;

    @FeatureMapping(desc = "体积")
    private BigDecimal volume;

    @FeatureMapping(desc = "运单总金额")
    private BigDecimal totalAmount;

    @FeatureMapping(desc = "基础运费")
    private BigDecimal basicShippingFee;

    @FeatureMapping(desc = "需求附加费")
    private BigDecimal attachmentFee;

    @FeatureMapping(desc = "燃油费")
    private BigDecimal fuelFee;

    @FeatureMapping(desc = "偏远地址费")
    private BigDecimal remoteAreaFee;

    @FeatureMapping(desc = "垫付税费")
    private BigDecimal advanceTaxFee;

    @FeatureMapping(desc = "其余手续费")
    private BigDecimal otherServiceFee;

    @FeatureMapping(desc = "币别")
    private String currencyName;

    @FeatureMapping(desc = "重量(kg)")
    private BigDecimal weight;

    /**
     * Description: 产品类型编码
     */
    private Integer productType;

    @FeatureMapping(desc = "产品类型")
    private String productTypeName;

    /**
     * Description: 付款方式编码
     */
    private Integer expressSettlementType;

    @FeatureMapping(desc = "付款方式")
    private String expressSettlementTypeName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @FeatureMapping(desc = "发货日期(yyyy-MM-dd hh:mm:ss)")
    private Date deliverTime;

    @FeatureMapping(desc = "发货日期字符串")
    private String deliverTimeStr;

    // 默认中国
    @FeatureMapping(desc = "寄出国")
    private String deliverNation = "中国";

    @FeatureMapping(desc = "寄出省")
    private String deliverProvince;

    @FeatureMapping(desc = "寄出市")
    private String deliverCity;

    // 默认中国
    @FeatureMapping(desc = "寄达国")
    private String receiveNation = "中国";

    @FeatureMapping(desc= "寄达省")
    private String receiveProvince;

    @FeatureMapping(desc = "寄达市")
    private String receiveCity;

    public Integer getReconciliationPeriod() {
        return reconciliationPeriod;
    }

    public void setReconciliationPeriod(Integer reconciliationPeriod) {
        this.reconciliationPeriod = reconciliationPeriod;
    }

    public String getSettlementOrganizeCode() {
        return settlementOrganizeCode;
    }

    public void setSettlementOrganizeCode(String settlementOrganizeCode) {
        this.settlementOrganizeCode = settlementOrganizeCode;
    }

    public String getSettlementOrganizeName() {
        return settlementOrganizeName;
    }

    public void setSettlementOrganizeName(String settlementOrganizeName) {
        this.settlementOrganizeName = settlementOrganizeName;
    }

    public String getShortSupplierName() {
        return shortSupplierName;
    }

    public void setShortSupplierName(String shortSupplierName) {
        this.shortSupplierName = shortSupplierName;
    }

    public String getExpressBillNo() {
        return expressBillNo;
    }

    public void setExpressBillNo(String expressBillNo) {
        this.expressBillNo = expressBillNo;
    }

    public String getExpressAccount() {
        return expressAccount;
    }

    public void setExpressAccount(String expressAccount) {
        this.expressAccount = expressAccount;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDateStr() {
        return invoiceDateStr;
    }

    public void setInvoiceDateStr(String invoiceDateStr) {
        this.invoiceDateStr = invoiceDateStr;
    }

    public Integer getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(Integer packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getBasicShippingFee() {
        return basicShippingFee;
    }

    public void setBasicShippingFee(BigDecimal basicShippingFee) {
        this.basicShippingFee = basicShippingFee;
    }

    public BigDecimal getAttachmentFee() {
        return attachmentFee;
    }

    public void setAttachmentFee(BigDecimal attachmentFee) {
        this.attachmentFee = attachmentFee;
    }

    public BigDecimal getFuelFee() {
        return fuelFee;
    }

    public void setFuelFee(BigDecimal fuelFee) {
        this.fuelFee = fuelFee;
    }

    public BigDecimal getRemoteAreaFee() {
        return remoteAreaFee;
    }

    public void setRemoteAreaFee(BigDecimal remoteAreaFee) {
        this.remoteAreaFee = remoteAreaFee;
    }

    public BigDecimal getAdvanceTaxFee() {
        return advanceTaxFee;
    }

    public void setAdvanceTaxFee(BigDecimal advanceTaxFee) {
        this.advanceTaxFee = advanceTaxFee;
    }

    public BigDecimal getOtherServiceFee() {
        return otherServiceFee;
    }

    public void setOtherServiceFee(BigDecimal otherServiceFee) {
        this.otherServiceFee = otherServiceFee;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Integer getExpressSettlementType() {
        return expressSettlementType;
    }

    public void setExpressSettlementType(Integer expressSettlementType) {
        this.expressSettlementType = expressSettlementType;
    }

    public String getExpressSettlementTypeName() {
        return expressSettlementTypeName;
    }

    public void setExpressSettlementTypeName(String expressSettlementTypeName) {
        this.expressSettlementTypeName = expressSettlementTypeName;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getDeliverTimeStr() {
        return deliverTimeStr;
    }

    public void setDeliverTimeStr(String deliverTimeStr) {
        this.deliverTimeStr = deliverTimeStr;
    }

    public String getDeliverNation() {
        return deliverNation;
    }

    public void setDeliverNation(String deliverNation) {
        this.deliverNation = deliverNation;
    }

    public String getDeliverProvince() {
        return deliverProvince;
    }

    public void setDeliverProvince(String deliverProvince) {
        this.deliverProvince = deliverProvince;
    }

    public String getDeliverCity() {
        return deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    public String getReceiveNation() {
        return receiveNation;
    }

    public void setReceiveNation(String receiveNation) {
        this.receiveNation = receiveNation;
    }

    public String getReceiveProvince() {
        return receiveProvince;
    }

    public void setReceiveProvince(String receiveProvince) {
        this.receiveProvince = receiveProvince;
    }

    public String getReceiveCity() {
        return receiveCity;
    }

    public void setReceiveCity(String receiveCity) {
        this.receiveCity = receiveCity;
    }

}