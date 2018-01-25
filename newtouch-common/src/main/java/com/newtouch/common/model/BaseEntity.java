package com.newtouch.common.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 更新标记
 * 
 * @author c_zhitianfeng
 * @version 1.0
 * @date 2015/4/8
 */
@MappedSuperclass
public abstract class BaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = -4223718838548582029L;
	
	/** 创建人 */
	@Column(name = "CRT_USER_ID")
	private Long crtUserId;
	/** 创建时间 */
	@Column(name = "CRT_DTTM")
	private Timestamp crtDttm;
	/** 更新人 */
	@Column(name = "LASTUPT_USER_ID")
	private Long lastuptUserId;
	/** 更新时间 */
	@Column(name = "LASTUPT_DTTM")
	private Timestamp lastuptDttm;
	@Transient
	private String crtName;
	@Transient
	private String lastUptName;

	public String getCrtName() {
		return crtName;
	}

	public void setCrtName(String crtName) {
		this.crtName = crtName;
	}

	public String getLastUptName() {
		return lastUptName;
	}

	public void setLastUptName(String lastUptName) {
		this.lastUptName = lastUptName;
	}

	public Long getCrtUserId() {
		return crtUserId;
	}

	public void setCrtUserId(Long crtUserId) {
		this.crtUserId = crtUserId;
	}

	public Timestamp getCrtDttm() {
		return crtDttm;
	}

	public void setCrtDttm(Timestamp crtDttm) {
		this.crtDttm = crtDttm;
	}

	public Long getLastuptUserId() {
		return lastuptUserId;
	}

	public void setLastuptUserId(Long lastuptUserId) {
		this.lastuptUserId = lastuptUserId;
	}

	public Timestamp getLastuptDttm() {
		return lastuptDttm;
	}

	public void setLastuptDttm(Timestamp lastuptDttm) {
		this.lastuptDttm = lastuptDttm;
	}

}
