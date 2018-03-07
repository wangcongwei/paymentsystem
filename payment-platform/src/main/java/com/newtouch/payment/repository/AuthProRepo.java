package com.newtouch.payment.repository;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.AuthPro;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface AuthProRepo extends Repository {
	
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "id ,"
			+ "createDate ,"
			+ "consumerID ,"
			+ "consumerPW ,"
			+ "consumerName ,"
			+ "comCode ,"
			+ "comName ,"
			+ "platformCode ,"
			+ "platformName ,"
			+ "payTypeCode ,"
			+ "payTypeName ,"
			+ " from AuthPro ap "
			+ "where ap.consumerID ={consumerID}") })
	AuthPro findAuthProByConsumerId(Class<AuthPro> returnClass, QueryParams queryParams);
}
