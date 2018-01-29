package com.newtouch.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.Repository;
import com.newtouch.demo.model.Product;

/**
 * 增删改查演示仓储
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/9/20
 */
public interface CurdRepo extends Repository {
	
    @Inquiries(query = 
    	{@Inquiry(value = "select"
    						+ " id,prodCode,prodName,price,crtDttm,lastuptDttm "
    					    + "from Product"
    					    + " where 1=1 /* and prodCode={prodCode} *//* and price > {price1} */ /*and price <{price2} */")})
    Page<Product> pageProduct(Class<Product> returnClass, QueryParams queryParams, Pageable pageable);
}
