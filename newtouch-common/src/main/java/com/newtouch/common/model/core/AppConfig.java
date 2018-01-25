package com.newtouch.common.model.core;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 应用配置数据模型
 *
 * @author dongfeng.zhang
 */
@Entity
@Table(name = "T_CFG_CONFIG ")
public class AppConfig extends BaseEntity {
	private static final long serialVersionUID = 4377310260864701253L;

	@Id
	@SequenceGenerator(name = "SEQ_T_CFG_CONFIG", sequenceName = "SEQ_T_CFG_CONFIG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CFG_CONFIG")
	@Column(name = "CONFIG_ID", nullable = false)
	private Long id;// 主键

	@Column(name = "CODE", nullable = false)
	private String code;// 查询是code

	@Column(name = "CATE_CODE")
	private String cateCode;

	@Column(name = "VALUE", nullable = false)
	private String value;// value值

	@Column(name = "VALUE_EXT")
	private String valueExt;// 扩展

	@Column(name = "ENABLE_FLG")
	private Boolean enable;

	private String describe;// 备注

	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueExt() {
		return valueExt;
	}

	public void setValueExt(String valueExt) {
		this.valueExt = valueExt;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
