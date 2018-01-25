package com.newtouch.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.AppConfig;
import com.newtouch.common.repository.Repository;


public interface ConfigRepository  extends Repository{
	@Inquiries(query = 
    	{@Inquiry(value = 
    		"select id,code,value,sysType,valueExt,describe "
		    + "from  AppConfig "
		    + "where "
		    + "enable=true "
		    + "/* and sysType={sysType}*/ "
		    + "/* and code like {code} */ "
		    + "/* and value like {value} */")})
    Page<AppConfig> pageAppConfig(Class<AppConfig> returnClass, QueryParams queryParams, Pageable pageable);
}
