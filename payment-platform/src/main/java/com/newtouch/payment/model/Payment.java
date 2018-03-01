package com.newtouch.payment.model;

import java.util.Date;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 支付模型
 *
 * @author
 */
@Entity
@Table(name = "T_PAYMENT")
public class Payment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938386021890499835L;
	@Id
	@SequenceGenerator(name = "SEQ_T_PAYMENT_ID", sequenceName = "SEQ_T_PAYMENT_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PAYMENT_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;
	
	@Column(name = "PAYMENT_NO")
	private String paymentNo;
	
	@Column(name = "CHECK_NO")
	private String checkNo;
	
	@Column(name = "DEAL_NO")
	private String dealNo;
	
	@Column(name = "PAY_TIME")
	private Date payTime;
	
	@Column(name = "PAY_AMOUNT")
	private Double payAmount;
	
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	
	@Column(name = "PAY_TYPE")
	private String payType;
	
	@Column(name = "PLATFORM")
	private String platform;
	
	@Column(name = "COM_CODE")
	private String comCode;
	
	@Column(name = "PAY_EFF_START")
	private Date payEffStart;
	
	@Column(name = "PAY_EFF_END")
	private Date payEffEnd;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "MERCHANT_NO")
	private String merchantNo;
	
	@Column(name = "TERMINAL_NO")
	private String terminalNo;
	
	@Column(name = "CARD_NO")
	private String cardNo;
	
	@Column(name = "REQ_NO")
	private String reqNo;
	
	@Column(name = "ISVALID")
	private String isvalid;
	
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

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public Date getPayEffStart() {
		return payEffStart;
	}

	public void setPayEffStart(Date payEffStart) {
		this.payEffStart = payEffStart;
	}

	public Date getPayEffEnd() {
		return payEffEnd;
	}

	public void setPayEffEnd(Date payEffEnd) {
		this.payEffEnd = payEffEnd;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
