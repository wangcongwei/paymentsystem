package com.newtouch.payment.repository;

import java.util.List;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.BusinessNum;

public interface BusinessNumRepo extends Repository{
	
	@Inquiries(query = 
    	{@Inquiry(value = "select"
    						+ " id,busNum,payChannel,payAccoutn "
    					    + "from BusinessNum"
    					    + " where 1=1 and busNum={busNum} and  payChannel={payChannel} ")})
    public List<BusinessNum> findByBusNum(Class<BusinessNum> entityClass, QueryParams queryParam);


}
