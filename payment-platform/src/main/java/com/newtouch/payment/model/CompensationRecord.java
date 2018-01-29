package com.newtouch.payment.model;

import java.util.Date;

/**
 * 补偿任务记录表
 * @author xf
 *
 */
public class CompensationRecord implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3943817599703308148L;

	/**物理主键*/
	private Long id;
	
	/**交易流水号*/
	private String transNo;
	
	/**补偿结果：交易流水状态(0-失败；1-成功；2-处理中)*/
	private String transStatus;
	
	/**下次补偿时间 */
	private Date nextTime;
	
	/**补偿次数 */
	private int compensationTimes;
	
	/**创建时间*/
	private Date creatDate;
	
	/**更新时间*/
	private Date updateDate;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getNextTime() {
		return nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	public int getCompensationTimes() {
		return compensationTimes;
	}

	public void setCompensationTimes(int compensationTimes) {
		this.compensationTimes = compensationTimes;
	}


}
