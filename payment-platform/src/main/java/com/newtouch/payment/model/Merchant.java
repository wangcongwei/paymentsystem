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
@Table(name = "T_MECHANT")
public class Merchant extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9081213317114777620L;

	@Id
//	@SequenceGenerator(name = "SEQ_T_MERCHANT_ID", sequenceName = "SEQ_T_MERCHANT_ID", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_MERCHANT_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	/**
	 * 商户号
	 */
	@Column(name = "MERCHANT_ID")
	private String merchantId;
	
	/**
	 * 商户名称
	 */
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	
	/**
	 * 状态:0-无效;1-有效
	 */
	@Column(name = "STATUS")
	private String status;
	
	/**
	 * 密钥
	 */
	@Column(name = "MAC_KEY")
	private String macKey;
	
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
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
