package com.newtouch.payment.view.model.quickorderSMS;

import java.math.BigDecimal;
/**
 * 快捷支付短信验证请求
 * @author xiangzhe.zeng
 *
 */
public class QuickOrderSMSReqVo {
	
	/**
	 * 商户订单号
	 */
	private String busOrderNo;
	/**
	 * 支付订单号
	 */
	private String orderNo;
	
	/**
	 * 商户号
	 */
	private String busNum;
	/**
	 * 订单金额
	 */
	private BigDecimal amount;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户手机
	 */
	private String userMobile;
	/**
	 * 用户证件类型
	 */
	private String userCerType;
	/**
	 * 用户证件号码
	 */
	private String userCerNum;
	/**
	 * 用户银行帐号
	 */
	private String userAccount;


	/**
	 * 卡号CSV码
	 */
	private String cvv2;

	/**
	 * 卡号有效期
	 */
	private String expiredDate;
	/**
	 * 卡片类型
	 */
	private String cardType;
	
	
	private String ext1;
	
	private String ext2;
	
	private String ext3;
	
	private String ext4;

	public String getBusOrderNo() {
		return busOrderNo;
	}

	public void setBusOrderNo(String busOrderNo) {
		this.busOrderNo = busOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBusNum() {
		return busNum;
	}

	public void setBusNum(String busNum) {
		this.busNum = busNum;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserCerType() {
		return userCerType;
	}

	public void setUserCerType(String userCerType) {
		this.userCerType = userCerType;
	}

	public String getUserCerNum() {
		return userCerNum;
	}

	public void setUserCerNum(String userCerNum) {
		this.userCerNum = userCerNum;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
