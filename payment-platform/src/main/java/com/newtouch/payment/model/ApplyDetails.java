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
//	@SequenceGenerator(name = "SEQ_T_APPLY_DETAILS_ID", sequenceName = "SEQ_T_APPLY_DETAILS_ID", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_APPLY_DETAILS_ID")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date createDate;

	/**
	 * 商品代码
	 */
	@Column(name = "GOODS_CODE")
	private String goodsCode;
	
	/**
	 * 商品描述
	 */
	@Column(name = "GOODS_DESC")
	private String goodsDesc;
	
	/**
	 * 商品价格
	 */
	@Column(name = "GOODS_PRICE")
	private String goodsPrice;
	
	/**
	 * 商品数量
	 */
	@Column(name = "GOODS_COUNT")
	private String goodsCount;
	
	/**
	 * 支付ID
	 */
	@ManyToOne()
	@JoinColumn(name = "PAYMENT_ID")
	private Payment payment;

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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
