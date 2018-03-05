package com.newtouch.payment.model;

import java.util.Date;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 权限配置模型
 *
 * @author
 */
@Entity
@Table(name = "T_AUTH_PRO")
public class AuthPro extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1282196235710933681L;

	@Id
//	@SequenceGenerator(name = "SEQ_T_AUTH_PRO_ID", sequenceName = "SEQ_T_AUTH_PRO_ID", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AUTH_PRO_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	/**
	 * 服务消费方ID
	 */
	@Column(name = "CONSUMER_ID")
	private String consumerID;
	
	/**
	 * 服务消费方密码
	 */
	@Column(name = "CONSUMER_PW")
	private String consumerPW;
	
	/**
	 * 服务消费方名称
	 */
	@Column(name = "CONSUMER_NAME")
	private String consumerName;
	
	/**
	 * 渠道代码
	 */
	@Column(name = "COM_CODE")
	private String comCode;
	
	/**
	 * 渠道名称
	 */
	@Column(name = "COM_NAME")
	private String comName;
	
	/**
	 * 支付方代码
	 */
	@Column(name = "platform_Code")
	private String platformCode;
	
	/**
	 * 支付方名称
	 */
	@Column(name = "platform_Name")
	private String platformName;
	
	/**
	 * 支付方式代码
	 */
	@Column(name = "pay_Type_Code")
	private String payTypeCode;
	
	/**
	 * 支付方式名称
	 */
	@Column(name = "pay_Type_Name")
	private String payTypeName;

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

	public String getConsumerID() {
		return consumerID;
	}

	public void setConsumerID(String consumerID) {
		this.consumerID = consumerID;
	}

	public String getConsumerPW() {
		return consumerPW;
	}

	public void setConsumerPW(String consumerPW) {
		this.consumerPW = consumerPW;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getPayTypeCode() {
		return payTypeCode;
	}

	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

}
