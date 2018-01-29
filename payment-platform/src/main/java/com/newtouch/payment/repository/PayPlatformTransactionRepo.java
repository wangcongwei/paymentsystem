package com.newtouch.payment.repository;

import java.util.List;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.TPayPlatformTransation;

/**
 * @author c_zhitianfeng
 * @version 1.0
 * @date 2015/4/3
 */
public interface PayPlatformTransactionRepo extends Repository {

	
	@Inquiries(query = 
    	{@Inquiry(value = "select"
    						+ " status  "
    					    + "from TPayPlatformTransation"
    					    + " where 1=1 and reqNo={reqNo} ")})
    public TPayPlatformTransation findByReqNo(Class<TPayPlatformTransation> entityClass, QueryParams queryParam);
	
	@Inquiries(query = 
    	{@Inquiry(value = "select"
    						+ " id,reqNo,status,enable_time,query_time,query_times "
    					    + "from TPayPlatformTransation"
    					    + " where status='CREATE' and query_time <= {sysdate} ")})
    public List<TPayPlatformTransation> findPayTrans(Class<TPayPlatformTransation> entityClass, QueryParams queryParam);
}
