package com.newtouch.common.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.common.repository.Repository;

public interface CodeRepository extends Repository {

	@Inquiries(query = { 
			@Inquiry(value = 
					"select "
					+ "e.id as id,"
					+ "e.parentId as parentId,"
					+ "e.name as name ,"
					+ "e.cateCode as cateCode ,"
					+ "e.code as code,"
					+ "e.describe as describe,"
					+ "e.sysType as sysType "
					+ "from "
					+ "   CodeCate c,CodeEntry e "
					+ "where "
					+ "	c.code=e.cateCode "
					+ " and e.enable=true "
					+ " /* and e.sysType={sysType}*/ "
					+ "	/* and e.code={code} */ "
					+ "	/* and e.cateCode={cateCode} */ "
					+ "	/* and e.name={name} */") })
	Page<CodeEntry> pageCodeEntry(Class<CodeEntry> returnClass, QueryParams queryParams, Pageable pageable);

	@Inquiries(query = { 
			@Inquiry(value = 
					"select id,code,name,sysType,describe "
					+ "from CodeCate "
					+ "where  enable = true "
					+ "/* and sysType={sysType} */ "
					+ "/* and code like {code} */ "
					+ "/* and name like {name} */  "
					+ "order by code") })
	Page<CodeCate> pageCodeCate(Class<CodeCate> returnClass, QueryParams queryParams, Pageable pageable);

	@Inquiries(query = { 
			@Inquiry(value = 
					"select "
					+ "id,"
					+ "parentId,"
					+ "name ,"
					+ "cateCode ,"
					+ "code,"
					+ "describe,"
					+ "sysType "
					+ "from"
					+ "	 CodeEntry e "
					+ "where "
					+ "	(parentId is null or parentId = '0') "
					+ "	and e.enable=true "
					+ "	/* and e.sysType={sysType}*/ "
					+ "	/* and e.code={code} */  "
					+ "	/* and e.cateCode={cateCode} */ "
					+ "	order by  code asc") })
	List<CodeEntry> findParentIdEntry(Class<CodeEntry> returnClass, QueryParams queryParams);
	
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "id,"
			+ "parentId,"
			+ "name ,"
			+ "cateCode ,"
			+ "code,"
			+ "describe,"
			+ "sysType"
			+ " from CodeEntry e "
			+ "where e.enable=true "
			+ "/* and e.parentId={parentId} */ "
			+ "/* and e.parentCode={parentCode} */ "
			+ "/* and e.sysType={sysType}*/ "
			+ "/* and e.code={code} */  "
			+ "/* and e.cateCode={cateCode} */  "
			+ "order by code asc") })
	List<CodeEntry> findCodeEntryList(Class<CodeEntry> returnClass, QueryParams queryParams);
	
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "id,"
			+ "parentId,"
			+ "parentCode ,"
			+ "name ,"
			+ "cateCode ,"
			+ "code "
			+ " from CodeEntry e "
			+ "where e.enable=true "
			+ " and e.parentId <> '0' "
			+ "/* and e.sysType={sysType}*/ "
			+ "/* and e.code={code} */  "
			+ "/* and e.cateCode={cateCode} */  "
			+ "order by code asc") })
	List<CodeEntry> findChildCodeEntryList(Class<CodeEntry> returnClass, QueryParams queryParams);
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "name ,"
			+ "cateCode ,"
			+ "code "
			+ " from CodeEntry e "
			+ "where e.enable=true and cateCode ={cateCode}"
			+ "/* and e.sysType={sysType}*/ "
			+ "/* and e.name={name} */  ") })
	CodeEntry findCodeByName(Class<CodeEntry> returnClass, QueryParams queryParams);
	
	@Inquiries(query = { @Inquiry(value = 
			"select "
			+ "id,"
			+ "parentId,"
			+ "name ,"
			+ "cateCode ,"
			+ "code,"
			+ "describe,"
			+ "sysType"
			+ " from CodeEntry e "
			+ "where e.enable=true "
			+ "/* and e.parentCode={parentCode} */ "
			+ "/* and e.sysType={sysType}*/ "
			+ "/* and e.code={code} */  "
			+ "/* and e.cateCode={cateCode} */  "
			+ "order by code asc") })
	List<CodeEntry> findEntryListByParentCode(Class<CodeEntry> returnClass, QueryParams queryParams);
}
