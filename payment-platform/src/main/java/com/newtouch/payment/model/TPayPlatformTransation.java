package com.newtouch.payment.model;
// Generated 2015-8-9 22:40:40 by Hibernate Tools 4.3.1.Final

import java.util.Date;

/**
 * 支付流水表
 * @author RenGL
 *
 * 2015年9月23日
 */
public class TPayPlatformTransation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 949200178207455119L;
	private Long id;
	private Date createDate;
	private String userTransactionNo;
	private String reqNo;
	private String resNo;
	private String orderNo;
	private String checkNo;
	private Double amount;
	private Double payAmount;
	private String currency;
	private String status;
	private String source;
	private String payerCode;
	private String payType;
	private Date payTime;
	private Date returnTime;
	private Date paysuccessTime;
	private String subMerchantId;
	private String accountno;
	private String accountName;
	private String validdate;
	private String ccv2;
	private String idType;
	private String idNo;
	private String storableCardNo;
	private String cardOrg;
	private String issuer;
	private String mobile;
	private String verifycode;
	private String token;
	private String messagecode;
	private String message;
	
	private Date enable_time;
	private Date query_time;
	private int query_times;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserTransactionNo() {
		return userTransactionNo;
	}

	public void setUserTransactionNo(String userTransactionNo) {
		this.userTransactionNo = userTransactionNo;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getResNo() {
		return resNo;
	}

	public void setResNo(String resNo) {
		this.resNo = resNo;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPayerCode() {
		return this.payerCode;
	}

	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}

	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Date getPayTime() {
		return this.payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Date getPaysuccessTime() {
		return paysuccessTime;
	}

	public void setPaysuccessTime(Date paysuccessTime) {
		this.paysuccessTime = paysuccessTime;
	}

	public String getSubMerchantId() {
		return subMerchantId;
	}

	public void setSubMerchantId(String subMerchantId) {
		this.subMerchantId = subMerchantId;
	}

	public String getAccountno() {
		return this.accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	public String getValiddate() {
		return this.validdate;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}

	public String getCcv2() {
		return this.ccv2;
	}

	public void setCcv2(String ccv2) {
		this.ccv2 = ccv2;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getStorableCardNo() {
		return storableCardNo;
	}

	public void setStorableCardNo(String storableCardNo) {
		this.storableCardNo = storableCardNo;
	}

	public String getCardOrg() {
		return cardOrg;
	}

	public void setCardOrg(String cardOrg) {
		this.cardOrg = cardOrg;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerifycode() {
		return this.verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessagecode() {
		return this.messagecode;
	}

	public void setMessagecode(String messagecode) {
		this.messagecode = messagecode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

	public Date getEnable_time() {
		return enable_time;
	}

	public void setEnable_time(Date enable_time) {
		this.enable_time = enable_time;
	}

	public Date getQuery_time() {
		return query_time;
	}

	public void setQuery_time(Date query_time) {
		this.query_time = query_time;
	}

	public int getQuery_times() {
		return query_times;
	}

	public void setQuery_times(int query_times) {
		this.query_times = query_times;
	}

}
