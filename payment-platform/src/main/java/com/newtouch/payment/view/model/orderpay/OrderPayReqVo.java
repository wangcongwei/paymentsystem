package com.newtouch.payment.view.model.orderpay;

import java.math.BigDecimal;

/**
 * 订单支付请求体
 * @author xiangzhe.zeng
 *
 */
public class OrderPayReqVo {
	
	/**
	 * 商户订单号
	 */
	private String busOrderNo;
	
	/**
	 * 商户号
	 */
	private String busNum;
	/**
	 * 订单金额
	 */
	private BigDecimal amount;
	
	/**
	 * 商户订单摘要
	 */
	private String orderDetail;
	
	/**
	 * 扩展字段一
	 */
	private String ext1;
	
	/**
	 * 扩展字段二
	 */
	private String ext2;
	/**
	 * 扩展字段三
	 */
	private String ext3;
	/**
	 * 扩展字段四
	 */
	private String ext4;

	public String getBusOrderNo() {
		return busOrderNo;
	}

	public void setBusOrderNo(String busOrderNo) {
		this.busOrderNo = busOrderNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getBusNum() {
		return busNum;
	}

	public void setBusNum(String busNum) {
		this.busNum = busNum;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

}
