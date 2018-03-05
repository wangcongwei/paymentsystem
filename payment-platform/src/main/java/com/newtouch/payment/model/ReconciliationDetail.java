package com.newtouch.payment.model;

import java.util.Date;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 对账明细模型
 *
 * @author
 */
@Entity
@Table(name = "T_RECONCILIATION_DETAIL")
public class ReconciliationDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9195173069514149890L;

	@Id
//	@SequenceGenerator(name = "SEQ_T_RECON_DETAIL_ID", sequenceName = "SEQ_T_RECON_DETAIL_ID", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_RECON_DETAIL_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	/**
	 * 支付号
	 */
	@Column(name = "PAYMENT_NO")
	private String paymentNo;
	
	/**
	 * 支付流水
	 */
	@Column(name = "DEAL_NO")
	private String dealNo;
	
	/**
	 * 支付时间
	 */
	@Column(name = "PAY_TIME")
	private Date payTime;
	
	/**
	 * 支付金额
	 */
	@Column(name = "PAY_AMOUNT")
	private Double payAmount;
	
	/**
	 * 支付号金额
	 */
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	
	/**
	 * 支付方式
	 */
	@Column(name = "PAY_TYPE")
	private String payType;
	
	/**
	 * 卡号
	 */
	@Column(name = "CARD_NO")
	private String cardNo;
	
	/**
	 * 币种
	 */
	@Column(name = "CURRENCY")
	private String currency;
	
	/**
	 * 银行代码
	 */
	@Column(name = "BANKCODE")
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	@Column(name = "BANKNAME")
	private String bankName;
	
	/**
	 * 卡类型
	 */
	@Column(name = "CARD_TYPE")
	private String cardType;
	
	/**
	 * 状态
	 */
	@Column(name = "STATUS")
	private String status;
	
	/**
	 * 终端号
	 */
	@Column(name = "TERMINAL_NO")
	private String terminalNo;
	
	/**
	 * 终端名称
	 */
	@Column(name = "TERMINAL_NAME")
	private String terminalName;
	
	/**
	 * 交易类型
	 */
	@Column(name = "BUSS_TYPE")
	private String bussType;
	
	/**
	 * 支付方
	 */
	@Column(name = "PLATFORM")
	private String platform;
	
	/**
	 * 商户号
	 */
	@Column(name = "MERCHANT_NO")
	private String merchantNo;
	
	/**
	 * 第三方流水
	 */
	@Column(name = "THIRD_NO")
	private String thirdNo;
	
	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_TIME")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getDealNo() {
		return dealNo;
	}

	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getBussType() {
		return bussType;
	}

	public void setBussType(String bussType) {
		this.bussType = bussType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getThirdNo() {
		return thirdNo;
	}

	public void setThirdNo(String thirdNo) {
		this.thirdNo = thirdNo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
