package com.newtouch.payment.model;


/**
 * 商户号及渠道表
 * @author xiangzhe.zeng
 *
 */
public class BusinessNum implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7710301317455860523L;

	private Long id;
	
	/**
	 * 商户号
	 */
	private String busNum;

	/**
	 * 支付渠道
	 */
	private String payChannel;
	
	/**
	 * 支付账户
	 */
	private String payAccoutn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusNum() {
		return busNum;
	}

	public void setBusNum(String busNum) {
		this.busNum = busNum;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getPayAccoutn() {
		return payAccoutn;
	}

	public void setPayAccoutn(String payAccoutn) {
		this.payAccoutn = payAccoutn;
	}

}
