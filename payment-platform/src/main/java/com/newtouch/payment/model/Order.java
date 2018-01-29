package com.newtouch.payment.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2300413856417268245L;
	private Long id;
	/**
	 * 商户订单号
	 */
	private String busOrderNo;

	/**
	 * 商户摘要
	 */
	private String busDetail;

	/**
	 * 订单金额
	 */
	private BigDecimal amount;

	/**
	 * 支付订单号
	 */
	private String orderNo;

	/**
	 * 支付订单创建时间
	 */
	private Date creatTime;
	/**
	 * 支付订单失效时间
	 */
	private Date outTime;

	/**
	 * 商户号
	 */
	private String busNum;

	/**
	 * 支付状态
	 */
	private String orderStatus;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusOrderNo() {
		return busOrderNo;
	}

	public void setBusOrderNo(String busOrderNo) {
		this.busOrderNo = busOrderNo;
	}

	public String getBusDetail() {
		return busDetail;
	}

	public void setBusDetail(String busDetail) {
		this.busDetail = busDetail;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getBusNum() {
		return busNum;
	}

	public void setBusNum(String busNum) {
		this.busNum = busNum;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

}
