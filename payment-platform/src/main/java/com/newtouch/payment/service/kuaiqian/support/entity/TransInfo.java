package com.newtouch.payment.service.kuaiqian.support.entity;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/13
 */
public class TransInfo {
	
	public final static String DEFAULE_TXN_TYPE_PUR = "PUR"; //消费交易
	
	private  String postUrl;			//提交地址ַ
	private  boolean FLAG;		//用来判断选用的解析函数
	private  String recordeText_1;		//用来记录XML内容格式字段，用来记录XML第一个标志内容格式字段
	private  String recordeText_2;		//当标记的内容多时，用来记录XML第二个标志内容格式字段
	private  String txnType;		//交易类型

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public boolean isFLAG() {
		return FLAG;
	}

	public void setFLAG(boolean flag) {
		FLAG = flag;
	}

	public String getRecordeText_1() {
		return recordeText_1;
	}

	public void setRecordeText_1(String recordeText_1) {
		this.recordeText_1 = recordeText_1;
	}

	public String getRecordeText_2() {
		return recordeText_2;
	}

	public void setRecordeText_2(String recordeText_2) {
		this.recordeText_2 = recordeText_2;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
}
