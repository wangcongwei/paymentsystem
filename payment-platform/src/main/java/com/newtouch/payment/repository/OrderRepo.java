package com.newtouch.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.Order;

/**
 * @author c_zhitianfeng
 * @version 1.0
 * @date 2015/4/3
 */
public interface OrderRepo extends Repository {

	@Inquiries(query = { @Inquiry(value = "select"
			+ " id,busOrderNo,busDetail,amount,orderNo,creatTime,outTime,busNum,orderStatus,updateTime" + "from Order"
			+ " where 1=1 /* and busNum={busNum} *//* and creatTime > {creatTime} */ /*and creatTime <{creatTime} */") })
	Page<Order> page(Class<Order> returnClass, QueryParams queryParams, Pageable pageable);

	@Inquiries(query = { @Inquiry(value = "select"
			+ " id,busOrderNo,busDetail,amount,orderNo,creatTime,outTime,busNum,orderStatus,updateTime " + "from Order"
			+ " where 1=1 and orderNo={orderNo} ") })
	Order findByOrderNo(Class<Order> returnClass, QueryParams queryParams);

	/**
	 * 创建支付订单号
	 * 
	 * @return
	 */
	public String createOrderNo();

	/**
	 * 根据支付订单号查找支付订单
	 * 
	 * @return
	 */
	public Order findByOrderNo(String orderNo);

	/**
	 * 创建支付流水号
	 * 
	 * @return
	 */
	public String createPaySeriNo();

	/**
	 * 更新保存order
	 * 
	 * @param order
	 */
	public void saveOrUpdate(Order order);

}
