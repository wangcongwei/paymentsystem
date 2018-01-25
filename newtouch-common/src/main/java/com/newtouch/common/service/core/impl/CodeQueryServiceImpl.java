package com.newtouch.common.service.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.newtouch.common.annotation.cache.CacheableDictionary;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.common.repository.CodeRepository;
import com.newtouch.common.service.core.CodeQueryService;

/**
 * 字典数据查询服务
 * 
 * @author dongfeng.zhang
 */
@Service
public class CodeQueryServiceImpl implements CodeQueryService {

	@Autowired
	private CodeRepository repo;

	@CacheableDictionary
	public CodeEntry getCode(String entryCode, String categoryCode, String sysType) {
		QueryParams queryParam = new QueryParams();
		queryParam.put("code", entryCode);
		queryParam.put("cateCode", categoryCode);
		queryParam.put("sysType", sysType);
		queryParam.put("enable", true);
		return repo.findOneByParam(CodeEntry.class, queryParam);
	}

	@CacheableDictionary
	public List<CodeEntry> getCodes(String path, String sysType) {
		QueryParams queryParam = new QueryParams();
		queryParam.put("sysType", sysType);
		queryParam.put("enable", true);
		queryParam.put("pathCode", path);
		Sort sort=new Sort(new Order(Direction.ASC,"sortNum"));
		return repo.findByParamAndSort(CodeEntry.class, queryParam, sort);
	}
	
	@CacheableDictionary
	public CodeCate getCodeCateByCode(String code,String sysType) {
		QueryParams queryParam = new QueryParams();
		queryParam.put("code", code);
		queryParam.put("sysType", sysType);
		queryParam.put("enable", true);
		return repo.findOneByParam(CodeCate.class, queryParam);
	}
	
}
