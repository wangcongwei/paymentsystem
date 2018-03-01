package com.newtouch.payment.model;

import java.util.Date;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 申请明细模型
 *
 * @author
 */
@Entity
@Table(name = "T_APPLY_DETAILS")
public class ApplyDetails extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1056615033105527711L;
	
	@Id
	@SequenceGenerator(name = "SEQ_T_APPLY_DETAILS_ID", sequenceName = "SEQ_T_APPLY_DETAILS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_APPLY_DETAILS_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "GOODS_CODE")
	private String goodsCode;
	
	@Column(name = "GOODS_DESC")
	private String goodsDesc;
	
	@Column(name = "GOODS_PRICE")
	private String goodsPrice;
	
	@Column(name = "GOODS_COUNT")
	private String goodsCount;
	
	@Column(name = "PAYMENT_ID")
	private Long paymentId;

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

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	
}
