package com.asio.vo;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工工资详情表
 * @author Administrator
 *
 */
@HeadFontStyle(fontHeightInPoints = 12, fontName = "宋体")
@HeadRowHeight(20)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class EmployeePayDetailsInfo implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 员工工资详情id
     */
    @ExcelProperty(value = "员工工资详情id")
    private Integer employeePayDetailsInfoId;

    /**
     * 员工工资表id
     */
    @ExcelProperty(value = "员工工资表id")
    private Integer employeePaySlipInfoId;

    /**
     * 员工id
     */
    @ExcelProperty(value = "员工id")
    private Integer employeeId;

    /**
     * 员工编号
     */
    @ExcelProperty(value = "员工编号")
    private String employeeCode;

    /**
     * 员工名称
     */
    @ExcelProperty(value = "员工名称")
    private String employeeName;

    /**
     * 身份证号码
     */
    @ExcelProperty(value = "身份证号码")
    private String idCard;

    /**
     * 开户银行名     (开户银行)
     */
    @ExcelProperty(value = "开户银行名")
    private String enrolBankName;

    /**
     * 开户人
     */
    @ExcelProperty(value = "开户人")
    private String enrolManName;

    /**
     * 银行账号
     */
    @ExcelProperty(value = "银行账号")
    private String bankcode;

    /**
     * 所属部门
     */
    @ExcelProperty(value = "所属部门")
    private String rangeDept;

    /**
     * 所属部门code
     */
    @ExcelProperty(value = "所属部门code")
    private String rangeDeptCode;

    /**
     * 工作部门
     */
    @ExcelProperty(value = "工作部门")
    private String workDept;

    /**
     * 工作部门code
     */
    @ExcelProperty(value = "工作部门code")
    private String workDeptCode;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    private String job;

    /**
     * 职位code
     */
    @ExcelProperty(value = "职位code")
    private String jobCode;

    /**
     * 级别
     */
    @ExcelProperty(value = "级别")
    private String employeeLevel;

    /**
     * 级别code
     */
    @ExcelProperty(value = "级别code")
    private String employeeLevelCode;

    /**
     * 入职时间
     */
    @ExcelProperty(value = "入职时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    /**
     * 基本工资
     */
    @ExcelProperty(value = "基本工资")
    private BigDecimal basePay;

    /**
     * 奖金1
     */
    @ExcelProperty(value = "奖金1")
    private BigDecimal baseBonus;

    /**
     * 工龄奖
     */
    @ExcelProperty(value = "工龄奖")
    private BigDecimal ageBonus;

    /**
     * 计件工资/提成
     */
    @ExcelProperty(value = "计件工资/提成")
    private BigDecimal countBonus;

    /**
     * 绩效奖/全勤奖
     */
    @ExcelProperty(value = "绩效奖/全勤奖")
    private BigDecimal performanceBonus;

    /**
     * 其他/电话补助
     */
    @ExcelProperty(value = "其他/电话补助")
    private BigDecimal elseBonus;

    /**
     * 应付工资
     */
    @ExcelProperty(value = "应付工资")
    private BigDecimal shouldPay;

    /**
     * 扣社保
     */
    @ExcelProperty(value = "扣社保")
    private BigDecimal socialSecurityOut;

    /**
     * 工作过失扣款
     */
    @ExcelProperty(value = "工作过失扣款")
    private BigDecimal workErrorOut;

    /**
     * 请假/其他扣款
     */
    @ExcelProperty(value = "请假/其他扣款")
    private BigDecimal elseOut;

    /**
     * 市话通/伙食
     */
    @ExcelProperty(value = "市话通/伙食")
    private BigDecimal boardWagesOut;

    /**
     * 应扣金额
     */
    @ExcelProperty(value = "应扣金额")
    private BigDecimal shouldOut;

    /**
     * 实发工资
     */
    @ExcelProperty(value = "实发工资")
    private BigDecimal truePay;

    /**
     * 本月上班天数
     */
    @ExcelProperty(value = "本月上班天数")
    private BigDecimal nonceWorkday;

    /**
     * 是否全勤(是,否)
     */
    @ExcelProperty(value = "是否全勤(是,否)")
    private String isDiligence;

    /**
     * 考勤编辑状态(编辑中,编辑完成)
     */
    @ExcelProperty(value = "考勤编辑状态(编辑中,编辑完成)")
    private String kqStatus;

    /**
     * 考勤备注
     */
    @ExcelProperty(value = "考勤备注")
    private String kqWriteRemark;

    /**
     * 工资编辑状态(编辑中,编辑完成)
     */
    @ExcelProperty(value = "工资编辑状态(编辑中,编辑完成)")
    private String gzStatus;

    /**
     * 工资备注
     */
    @ExcelProperty(value = "工资备注")
    private String gzWriteRemark;

    /**
     * 作废状态
     */
    @ExcelProperty(value = "作废状态")
    private String isDisabled;

    /**
     * 作废人id
     */
    @ExcelProperty(value = "作废人id")
    private Integer disabledManId;

    /**
     * 作废人名称
     */
    @ExcelProperty(value = "作废人名称")
    private String disabledManName;

    /**
     * 作废时间
     */
    @ExcelProperty(value = "作废时间")
    private Date disabledDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 最大薪酬
     */
    @ExcelProperty(value = "最大薪酬")
    private String mostStipend;

    /**
     * 生活补助/加班费3
     */
    @ExcelProperty(value = "生活补助/加班费3")
    private BigDecimal lifeBonus;

    /**
     * 奖金2 （新增字段）
     */
    @ExcelProperty(value = "奖金2")
    private BigDecimal baseBonus2;

    /**
     * 收款银行所在省份
     */
    @ExcelProperty(value = "收款银行所在省份")
    private String gatheringUserProvince;

    /**
     * 收款银行所在城市
     */
    @ExcelProperty(value = "收款银行所在城市")
    private String gatheringUserCity;

    /**
     * 所属银行
     */
    @ExcelProperty(value = "所属银行")
    private String rangeBankName;

    /**
     * 银行代发定额
     */
    @ExcelProperty(value = "银行代发定额")
    private BigDecimal bankGenerationQuota;

    /**
     * 费用报销额度
     */
    @ExcelProperty(value = "费用报销额度")
    private BigDecimal reimbursementQuota;

    /**
     * 是否允许对公子账号打款
     */
    @ExcelProperty(value = "是否允许对公子账号打款")
    private Integer isWagesChildAccount;

    /**
     * 允许对公子账号打款最大额度
     */
    @ExcelProperty(value = "允许对公子账号打款最大额度")
    private BigDecimal wagesChildAccountQuota;

    /**
     *员工类别
     */
    @ExcelProperty(value = "员工类别")
    private Integer employeeType;

    /**
     * 工资发放类别
     */
    @ExcelProperty(value = "工资发放类别")
    private String wagesType;

    /**
     *个人所得税
     */
    @ExcelProperty(value = "个人所得税")
    private BigDecimal personTax;

    /**
     *劳务税
     */
    @ExcelProperty(value = "劳务税")
    private BigDecimal labourTax;

    /*
     *税前工资
     */
    @ExcelProperty(value = "税前工资")
    private BigDecimal beforePay;

    //银行代发金额
    @ExcelProperty(value = "银行发放金额")
    private BigDecimal bankPayMoney;

    //对私发放金额
    @ExcelProperty(value = "对私发放金额")
    private BigDecimal privatePayMoney;

    //对公子账号金额
    @ExcelProperty(value = "对公子账号金额")
    private BigDecimal childAccountPayMoney;

    /**
     * 实际费用报销金额
     */
    @ExcelProperty(value = "实际费用报销金额")
    private BigDecimal reimbursementMoney;

    /**
     * 资金发放公司
     */
    @ExcelProperty(value = "资金发放公司")
    private String invoiceCompany;

    /**
     * 支行编号
     */
    @ExcelProperty(value = "支行编号")
    private String payDetailsBankCode;

    /**
     * 专项附加扣除金额
     */
    @ExcelProperty(value = "专项附加扣除金额")
    private BigDecimal specialSubtractMoney;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getEmployeePayDetailsInfoId() {
        return employeePayDetailsInfoId;
    }

    public void setEmployeePayDetailsInfoId(Integer employeePayDetailsInfoId) {
        this.employeePayDetailsInfoId = employeePayDetailsInfoId;
    }

    public Integer getEmployeePaySlipInfoId() {
        return employeePaySlipInfoId;
    }

    public void setEmployeePaySlipInfoId(Integer employeePaySlipInfoId) {
        this.employeePaySlipInfoId = employeePaySlipInfoId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEnrolBankName() {
        return enrolBankName;
    }

    public void setEnrolBankName(String enrolBankName) {
        this.enrolBankName = enrolBankName;
    }

    public String getEnrolManName() {
        return enrolManName;
    }

    public void setEnrolManName(String enrolManName) {
        this.enrolManName = enrolManName;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getRangeDept() {
        return rangeDept;
    }

    public void setRangeDept(String rangeDept) {
        this.rangeDept = rangeDept;
    }

    public String getRangeDeptCode() {
        return rangeDeptCode;
    }

    public void setRangeDeptCode(String rangeDeptCode) {
        this.rangeDeptCode = rangeDeptCode;
    }

    public String getWorkDept() {
        return workDept;
    }

    public void setWorkDept(String workDept) {
        this.workDept = workDept;
    }

    public String getWorkDeptCode() {
        return workDeptCode;
    }

    public void setWorkDeptCode(String workDeptCode) {
        this.workDeptCode = workDeptCode;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getEmployeeLevel() {
        return employeeLevel;
    }

    public void setEmployeeLevel(String employeeLevel) {
        this.employeeLevel = employeeLevel;
    }

    public String getEmployeeLevelCode() {
        return employeeLevelCode;
    }

    public void setEmployeeLevelCode(String employeeLevelCode) {
        this.employeeLevelCode = employeeLevelCode;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public BigDecimal getBasePay() {
        return basePay;
    }

    public void setBasePay(BigDecimal basePay) {
        this.basePay = basePay;
    }

    public BigDecimal getBaseBonus() {
        return baseBonus;
    }

    public void setBaseBonus(BigDecimal baseBonus) {
        this.baseBonus = baseBonus;
    }

    public BigDecimal getAgeBonus() {
        return ageBonus;
    }

    public void setAgeBonus(BigDecimal ageBonus) {
        this.ageBonus = ageBonus;
    }

    public BigDecimal getCountBonus() {
        return countBonus;
    }

    public void setCountBonus(BigDecimal countBonus) {
        this.countBonus = countBonus;
    }

    public BigDecimal getPerformanceBonus() {
        return performanceBonus;
    }

    public void setPerformanceBonus(BigDecimal performanceBonus) {
        this.performanceBonus = performanceBonus;
    }

    public BigDecimal getElseBonus() {
        return elseBonus;
    }

    public void setElseBonus(BigDecimal elseBonus) {
        this.elseBonus = elseBonus;
    }

    public BigDecimal getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(BigDecimal shouldPay) {
        this.shouldPay = shouldPay;
    }

    public BigDecimal getSocialSecurityOut() {
        return socialSecurityOut;
    }

    public void setSocialSecurityOut(BigDecimal socialSecurityOut) {
        this.socialSecurityOut = socialSecurityOut;
    }

    public BigDecimal getWorkErrorOut() {
        return workErrorOut;
    }

    public void setWorkErrorOut(BigDecimal workErrorOut) {
        this.workErrorOut = workErrorOut;
    }

    public BigDecimal getElseOut() {
        return elseOut;
    }

    public void setElseOut(BigDecimal elseOut) {
        this.elseOut = elseOut;
    }

    public BigDecimal getBoardWagesOut() {
        return boardWagesOut;
    }

    public void setBoardWagesOut(BigDecimal boardWagesOut) {
        this.boardWagesOut = boardWagesOut;
    }

    public BigDecimal getShouldOut() {
        return shouldOut;
    }

    public void setShouldOut(BigDecimal shouldOut) {
        this.shouldOut = shouldOut;
    }

    public BigDecimal getTruePay() {
        return truePay;
    }

    public void setTruePay(BigDecimal truePay) {
        this.truePay = truePay;
    }

    public BigDecimal getNonceWorkday() {
        return nonceWorkday;
    }

    public void setNonceWorkday(BigDecimal nonceWorkday) {
        this.nonceWorkday = nonceWorkday;
    }

    public String getIsDiligence() {
        return isDiligence;
    }

    public void setIsDiligence(String isDiligence) {
        this.isDiligence = isDiligence;
    }

    public String getKqStatus() {
        return kqStatus;
    }

    public void setKqStatus(String kqStatus) {
        this.kqStatus = kqStatus;
    }

    public String getKqWriteRemark() {
        return kqWriteRemark;
    }

    public void setKqWriteRemark(String kqWriteRemark) {
        this.kqWriteRemark = kqWriteRemark;
    }

    public String getGzStatus() {
        return gzStatus;
    }

    public void setGzStatus(String gzStatus) {
        this.gzStatus = gzStatus;
    }

    public String getGzWriteRemark() {
        return gzWriteRemark;
    }

    public void setGzWriteRemark(String gzWriteRemark) {
        this.gzWriteRemark = gzWriteRemark;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Integer getDisabledManId() {
        return disabledManId;
    }

    public void setDisabledManId(Integer disabledManId) {
        this.disabledManId = disabledManId;
    }

    public String getDisabledManName() {
        return disabledManName;
    }

    public void setDisabledManName(String disabledManName) {
        this.disabledManName = disabledManName;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMostStipend() {
        return mostStipend;
    }

    public void setMostStipend(String mostStipend) {
        this.mostStipend = mostStipend;
    }

    public BigDecimal getLifeBonus() {
        return lifeBonus;
    }

    public void setLifeBonus(BigDecimal lifeBonus) {
        this.lifeBonus = lifeBonus;
    }

    public BigDecimal getBaseBonus2() {
        return baseBonus2;
    }

    public void setBaseBonus2(BigDecimal baseBonus2) {
        this.baseBonus2 = baseBonus2;
    }

    public String getRangeBankName() {
        return rangeBankName;
    }

    public void setRangeBankName(String rangeBankName) {
        this.rangeBankName = rangeBankName;
    }

    public String getPayDetailsBankCode() {
        return payDetailsBankCode;
    }

    public void setPayDetailsBankCode(String payDetailsBankCode) {
        this.payDetailsBankCode = payDetailsBankCode;
    }

    public String getGatheringUserProvince() {
        return gatheringUserProvince;
    }

    public void setGatheringUserProvince(String gatheringUserProvince) {
        this.gatheringUserProvince = gatheringUserProvince;
    }

    public String getGatheringUserCity() {
        return gatheringUserCity;
    }

    public void setGatheringUserCity(String gatheringUserCity) {
        this.gatheringUserCity = gatheringUserCity;
    }

    public Integer getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }

    public String getWagesType() {
        return wagesType;
    }

    public void setWagesType(String wagesType) {
        this.wagesType = wagesType;
    }

    public BigDecimal getReimbursementQuota() {
        return reimbursementQuota;
    }

    public void setReimbursementQuota(BigDecimal reimbursementQuota) {
        this.reimbursementQuota = reimbursementQuota;
    }

    public BigDecimal getReimbursementMoney() {
        return reimbursementMoney;
    }

    public void setReimbursementMoney(BigDecimal reimbursementMoney) {
        this.reimbursementMoney = reimbursementMoney;
    }

    public BigDecimal getBankGenerationQuota() {
        return bankGenerationQuota;
    }

    public void setBankGenerationQuota(BigDecimal bankGenerationQuota) {
        this.bankGenerationQuota = bankGenerationQuota;
    }

    public BigDecimal getWagesChildAccountQuota() {
        return wagesChildAccountQuota;
    }

    public void setWagesChildAccountQuota(BigDecimal wagesChildAccountQuota) {
        this.wagesChildAccountQuota = wagesChildAccountQuota;
    }

    public Integer getIsWagesChildAccount() {
        return isWagesChildAccount;
    }

    public void setIsWagesChildAccount(Integer isWagesChildAccount) {
        this.isWagesChildAccount = isWagesChildAccount;
    }

    public BigDecimal getPersonTax() {
        return personTax;
    }

    public void setPersonTax(BigDecimal personTax) {
        this.personTax = personTax;
    }

    public BigDecimal getLabourTax() {
        return labourTax;
    }

    public void setLabourTax(BigDecimal labourTax) {
        this.labourTax = labourTax;
    }

    public BigDecimal getBeforePay() {
        return beforePay;
    }

    public void setBeforePay(BigDecimal beforePay) {
        this.beforePay = beforePay;
    }

    public BigDecimal getBankPayMoney() {
        return bankPayMoney;
    }

    public void setBankPayMoney(BigDecimal bankPayMoney) {
        this.bankPayMoney = bankPayMoney;
    }

    public BigDecimal getPrivatePayMoney() {
        return privatePayMoney;
    }

    public void setPrivatePayMoney(BigDecimal privatePayMoney) {
        this.privatePayMoney = privatePayMoney;
    }

    public BigDecimal getChildAccountPayMoney() {
        return childAccountPayMoney;
    }

    public void setChildAccountPayMoney(BigDecimal childAccountPayMoney) {
        this.childAccountPayMoney = childAccountPayMoney;
    }
}
