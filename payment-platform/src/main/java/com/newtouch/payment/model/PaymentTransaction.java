package com.newtouch.payment.model;

import java.util.Date;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 支付流程轨迹模型
 *
 * @author
 */
@Entity
@Table(name = "T_PAYMENT_TRANSACTION")
public class PaymentTransaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462455134524142536L;

	@Id
	@SequenceGenerator(name = "SEQ_T_PAY_TRANS_ID", sequenceName = "SEQ_T_PAY_TRANS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PAY_TRANS_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "PAYMENT_NO")
	private String paymentNo;
	
	@Column(name = "STATUS")
	private String status;
	
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
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "CARD_NO")
	private String cardNo;
	
	@Column(name = "EXPIREDATE")
	private String expireDate;
	
	@Column(name = "CVV")
	private String cvv;
	
	@Column(name = "BANKCODE")
	private String bankCode;
	
	@Column(name = "BANKNAME")
	private String bankName;
	
	@Column(name = "REQ_NO")
	private String reqNo;
	
	@Column(name = "MERCHANT_NO")
	private String merchantNo;
	
	@Column(name = "PAYMENT_ID")
	private String paymentId;
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
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

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
