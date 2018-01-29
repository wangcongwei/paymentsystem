package com.newtouch.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.model.CompensationRecord;
import com.newtouch.payment.model.OrderPayRequest;
import com.newtouch.payment.model.TPayPlatformTransation;
import com.newtouch.payment.repository.CompensationRecordRepo;
import com.newtouch.payment.repository.OrderPayRequestRepo;
import com.newtouch.payment.repository.PayPlatformTransactionRepo;
import com.newtouch.payment.service.TransStatusSearch;

@Service(value="transStatusSearch")
public class TransStatusSearchImpl implements TransStatusSearch{
	
	@Autowired
	private OrderPayRequestRepo orderPayRequestRepo;
	
	@Autowired
	private CompensationRecordRepo compensationRecordRepo;
	
	@Autowired
	private PayPlatformTransactionRepo payPlatformTransactionRepo;

	/**
	 * 根据交易流水号查询交易流水状态
	 * @return 交易流水状态(0-失败；1-成功；2-处理中)
	 */
	@Override
	public String SearchStatusByTransNo(String transNo) {
		QueryParams queryParams = new QueryParams();
		queryParams.put("reqNo", transNo);
		TPayPlatformTransation tppt = payPlatformTransactionRepo.findByReqNo(TPayPlatformTransation.class, queryParams);
		return tppt.getStatus();
	}
	
	/**
	 * 更新支付订单信息
	 * @param orderPayRequest
	 */
	public void idoOrderPayRequest(OrderPayRequest orderPayRequest){
		orderPayRequestRepo.update(orderPayRequest);
	}
	
	/**
	 * 更新交易流水信息
	 * @param tppt
	 */
	public void idoTPayPlatformTransation(TPayPlatformTransation tppt){
		payPlatformTransactionRepo.update(tppt);
	}
	
	/**
	 * 保存交易查询补偿记录
	 * @param cr
	 */
	public void idoCompensationRecord(CompensationRecord cr){
		compensationRecordRepo.save(cr);
	}

	public void setPayPlatformTransactionRepo(PayPlatformTransactionRepo payPlatformTransactionRepo) {
		this.payPlatformTransactionRepo = payPlatformTransactionRepo;
	}
	
	

	public void setOrderPayRequestRepo(OrderPayRequestRepo orderPayRequestRepo) {
		this.orderPayRequestRepo = orderPayRequestRepo;
	}

	public void setCompensationRecordRepo(
			CompensationRecordRepo compensationRecordRepo) {
		this.compensationRecordRepo = compensationRecordRepo;
	}


}
