package com.newtouch.payment.repository;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.OrderPayRequest;

public interface OrderPayRequestRepo extends Repository{
	
	@Inquiries(query = 
    	{@Inquiry(value = "select"
    						+ " id,amount,orderNo,creatTime,updateTime,payRequestStatus,busNum,paySeriNo,userName,userCerType,userCerNum,userAccount,userMobile,ext1,ext2,ext3,ext4  "
    					    + "from OrderPayRequest"
    					    + " where 1=1 and paySeriNo={paySeriNo} ")})
    public OrderPayRequest findByPaySeriNo(Class<OrderPayRequest> entityClass, QueryParams queryParam);
}
