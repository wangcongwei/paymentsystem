package com.newtouch.demo.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.newtouch.common.model.BaseEntity;


/**
 * 增删改查演示PRODUCT模型
 * 
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/9/20
 */
@Entity
@Table(name = "DEMO_PRODUCT")
public class Product extends BaseEntity implements java.io.Serializable{
	@Id
	@Column(name = "ID")
	private Long id;
	/** 商品code */
	@Column(name = "PROD_CODE")
	private String prodCode;
	/** 商品名称 */
	@Column(name = "PROD_NAME")
	private String prodName;
	/** 价格 */
	@Column(name = "PRICE")
	private double price;
	@Transient
	private Date systemTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public Date getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", prodCode=" + prodCode + ", prodName=" + prodName + ", price=" + price + "]";
	}
	
}
