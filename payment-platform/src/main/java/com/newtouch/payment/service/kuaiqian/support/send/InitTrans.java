package com.newtouch.payment.service.kuaiqian.support.send;

import com.newtouch.payment.service.kuaiqian.support.entity.TransInfo;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/13
 */
public class InitTrans {
	
	private TransInfo transInfo;
	
	public TransInfo getTransInfo() {
		return transInfo;
	}
	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}	
	
	public InitTrans(String txnType){
		this.transInfo = new TransInfo();
		this.setTxnType(TransInfo.DEFAULE_TXN_TYPE_PUR);
		this.setOtherType(txnType);
	}
	
	/**
	 * 根据不同的交易，选择不同提交地址和不同的XML节点
	 * 只用于消费、分期、预授权、预授权完成、撤销、退货、首次代扣、后续代扣
	 * */
	public void setTxnType(String txnType){
		//设置交易类型
		transInfo.setTxnType(txnType);
		//返回组合前和返回TR2后的第一个标志字段
		transInfo.setRecordeText_1("TxnMsgContent");
		//返回TR2后的错误标志字段
		transInfo.setRecordeText_2("ErrorMsgContent");
		//设置最后的解析方式
		transInfo.setFLAG(true);
		if(txnType.equals("PUR")){
			transInfo.setPostUrl("/cnp/purchase");//消费、首次代扣、后续代扣交易提交地址
		}else if(txnType.equals("INP")){
			transInfo.setPostUrl("/cnp/installment_purchase");//分期交易提交地址
		}else if(txnType.equals("PRE")){
			transInfo.setPostUrl("/cnp/preauth");//预授权交易提交地址
		}else if(txnType.equals("CFM")){
			transInfo.setPostUrl("/cnp/confirm");//预授权完成交易提交地址
		}else if(txnType.equals("VTX")){
			transInfo.setPostUrl("/cnp/void");//撤销交易提交地址
		}else if(txnType.equals("RFD")){
			transInfo.setPostUrl("/cnp/refund");//退货交易提交地址
		}  
	}
	/**
	 * 根据不同的交易，选择不同提交地址和不同的XML节点
	 * 只用于代扣退款、单笔查询、查询批交易、查询日确认交易、查询日入账交易、卡信息查询、卡验证、卡号与手机绑定、短信动态密码发送申请
	 * */
	public void setOtherType(String otherType){
		//下面设置代扣退款
		if(otherType.equals("batch_refund")){
			transInfo.setRecordeText_1("BatchRefundContent");
			transInfo.setRecordeText_2("ErrorMsgContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/batch_refund");
		//下面设置单笔查询
		}else if(otherType.equals("query_txn")){
			transInfo.setRecordeText_1("QryTxnMsgContent");
			transInfo.setRecordeText_2("ErrorMsgContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/query_txn");
		//下面设置查询批交易
		}else if(otherType.equals("query_txn_list")){
			transInfo.setRecordeText_1("BatchQryTxnMsgContent");
			transInfo.setRecordeText_2("TxnListContent");
			transInfo.setFLAG(false);
			transInfo.setPostUrl("/cnp/query_txn_list");
		//下面设置查询日确认交易
		}else if(otherType.equals("query_confirm_txn_list")){
			transInfo.setRecordeText_1("QryConfirmListContent");
			transInfo.setRecordeText_2("TxnListContent");
			transInfo.setFLAG(false);
			transInfo.setPostUrl("/cnp/query_confirm_txn_list");
		//下面设置查询日入账交易
		}else if(otherType.equals("query_settlement_txn_list")){
			transInfo.setRecordeText_1("QrySettlementListContent");
			transInfo.setRecordeText_2("SettleListContent");
			transInfo.setFLAG(false);
			transInfo.setPostUrl("/cnp/query_settlement_txn_list");
		//下面设置卡信息查询
		}else if(otherType.equals("query_cardinfo")){
			transInfo.setRecordeText_1("QryCardContent");
			transInfo.setRecordeText_2("CardInfoContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/query_cardinfo");
		//下面设置卡验证
		}else if(otherType.equals("card_validation")){
			transInfo.setRecordeText_1("SvcCardContent");
			transInfo.setRecordeText_2("ErrorMsgContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/card_validation");
		//下面设置卡号手机绑定
		}else if(otherType.equals("card_phoneno_binding")){
			transInfo.setRecordeText_1("CardPhoneBindContent");
			transInfo.setRecordeText_2("ErrorMsgContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/card_phoneno_binding");
		//下面设置短信动态密码发送申请
		}else if(otherType.equals("dp_request")){
			transInfo.setRecordeText_1("DyPinContent");
			transInfo.setRecordeText_2("ErrorMsgContent");
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/cnp/dp_request");
		//下载日交易确认明细
		}else if(otherType.equals("get_confirm_txnlist")){
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/mcs/get_confirm_txnlist");
		//下载日入账明细
		}else if(otherType.equals("get_settlement_list")){
			transInfo.setFLAG(true);
			transInfo.setPostUrl("/mcs/get_settlement_list");
		}   
	}
}
