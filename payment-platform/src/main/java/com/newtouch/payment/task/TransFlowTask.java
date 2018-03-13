package com.newtouch.payment.task;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.newtouch.common.model.QueryParams;
//import com.newtouch.payment.repository.OrderPayRequestRepo;
//import com.newtouch.payment.repository.PayPlatformTransactionRepo;
import com.newtouch.payment.service.KuaiqianQuickPayService;
import com.newtouch.payment.service.TransFlowSearch;


public class TransFlowTask {
	
	@Autowired
	private KuaiqianQuickPayService kuaiqianQuickPayService;
	
	@Autowired
	private TransFlowSearch transFlowSearch;
	
//	@Autowired
//	private PayPlatformTransactionRepo payPlatformTransactionRepo;
//	
//	@Autowired
//	private OrderPayRequestRepo orderPayRequestRepo;
	
//	@Autowired
//	private TransStatusSearch transStatusSearch;
	
	
	
	public void searchTransStatus(){
		/**查询出数据库中交易状态为处理中的交易流水*/
		QueryParams queryParams = new QueryParams();
		Date sysdate = new Date();
		queryParams.put("sysdate", sysdate);
//		List<TPayPlatformTransation> tPayPlatformTransation = payPlatformTransactionRepo.findPayTrans(TPayPlatformTransation.class, queryParams);
//		for(TPayPlatformTransation tppt :  tPayPlatformTransation){
//			Date nextTime = null;//下次补偿时间 
//			/**调用快钱交易流水查询接口获取 最新状态 */
//			String newStatus = transFlowSearch.searchTransFlow(tppt);//交易流水状态（FAILED-失败；SUCCESS-成功；CREATE-处理中）
//			if("FAILED".equals(newStatus) || "SUCCESS".equals(newStatus)){//成功或者失败
//				QueryParams queryParam = new QueryParams();
//				queryParam.put("paySeriNo", tppt.getReqNo());
//				OrderPayRequest orderPayRequest = orderPayRequestRepo.findByPaySeriNo(OrderPayRequest.class, queryParam);
//				orderPayRequest.setPayRequestStatus(newStatus);
//				orderPayRequest.setUpdateTime(new Date());
//				transStatusSearch.idoOrderPayRequest(orderPayRequest);
//			}else if("CREATE".equals(newStatus)){
//				if(compareDate(new Date(), tppt.getEnable_time())){
//					newStatus = "FAILED";
//				}else{
//					nextTime = this.getNextTime(tppt.getQuery_time(), tppt.getQuery_times());
//				}
//			}
//			//更新交易流水信息
//			tppt.setStatus(newStatus);
//			tppt.setQuery_times(tppt.getQuery_times() + 1);//补偿次数
//			tppt.setQuery_time(nextTime);
//			transStatusSearch.idoTPayPlatformTransation(tppt);
//			//记录补偿任务记录
//			CompensationRecord cr = new CompensationRecord();
//			cr.setTransNo(tppt.getReqNo());
//			cr.setTransStatus(tppt.getStatus());
//			cr.setNextTime(tppt.getQuery_time());
//			cr.setCompensationTimes(tppt.getQuery_times());
//			cr.setCreatDate(new Date());
//			cr.setUpdateDate(new Date());
//			transStatusSearch.idoCompensationRecord(cr);
//		}
	}

	/**
	 * 获取下次补偿时间 
	 * @param lastTime 最后一次补偿时间
	 * @param times  已经补偿次数
	 * @return
	 */
	private Date getNextTime(Date lastTime, int times){
		Date nextTime = new Date(lastTime.getTime() + 60000*(2*times));
		return nextTime;
	}
	
	
	private Boolean compareDate(Date startDate, Date endDate) {
		Calendar calenderStart = Calendar.getInstance();
		calenderStart.setTime(getSimpleDate(startDate));
		Calendar calenderEnd = Calendar.getInstance();
		calenderEnd.setTime(getSimpleDate(endDate));
		if (calenderStart.after(calenderEnd) || calenderStart.equals(calenderEnd)){
			return true;
		} else {
			return false;
		}
	}
	
	private Date getSimpleDate(Date date) {
		if (date == null) {
			date = new Date();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public void setKuaiqianQuickPayService(
			KuaiqianQuickPayService kuaiqianQuickPayService) {
		this.kuaiqianQuickPayService = kuaiqianQuickPayService;
	}

	public void setTransFlowSearch(TransFlowSearch transFlowSearch) {
		this.transFlowSearch = transFlowSearch;
	}

//	public void setPayPlatformTransactionRepo(
//			PayPlatformTransactionRepo payPlatformTransactionRepo) {
//		this.payPlatformTransactionRepo = payPlatformTransactionRepo;
//	}
//
//	public void setOrderPayRequestRepo(OrderPayRequestRepo orderPayRequestRepo) {
//		this.orderPayRequestRepo = orderPayRequestRepo;
//	}

//	public void setTransStatusSearch(TransStatusSearch transStatusSearch) {
//		this.transStatusSearch = transStatusSearch;
//	}
	
	

	
	
}