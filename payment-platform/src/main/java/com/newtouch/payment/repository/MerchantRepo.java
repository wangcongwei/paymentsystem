package com.newtouch.payment.repository;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.payment.model.Merchant;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface MerchantRepo extends Repository {
	
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "id ,"
			+ "createDate ,"
			+ "merchantId ,"
			+ "merchantName ,"
			+ "status ,"
			+ "macKey ,"
			+ "platformCode ,"
			+ "platformName ,"
			+ "payTypeCode ,"
			+ "payTypeName ,"
			+ " from Merchant m "
			+ "where m.merchantId ={merchantId}") })
	Merchant findMerchantByMerchantId(Class<Merchant> returnClass, QueryParams queryParams);
}
