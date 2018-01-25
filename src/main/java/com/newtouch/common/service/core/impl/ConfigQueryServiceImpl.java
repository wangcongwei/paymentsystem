package com.newtouch.common.service.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.AppConfig;
import com.newtouch.common.repository.ConfigRepository;
import com.newtouch.common.service.core.ConfigQueryService;

/**
 * 配置数据查询
 *
 * @author dongfeng.zhang
 */
@Service
public class ConfigQueryServiceImpl implements ConfigQueryService {

	@Autowired
	private ConfigRepository repo;


    @Override
    public AppConfig getConfig(String key, String catalog,String sysType) {
    	QueryParams queryParam = new QueryParams();
		queryParam.put("code", key);
		if(!"".equals(catalog)){
			queryParam.put("cateCode", catalog);
		}
		queryParam.put("sysType", sysType);
		return repo.findOneByParam(AppConfig.class, queryParam);
    }

    @Override
    public List<AppConfig> getConfigs(String catalog,String sysType) {
    	QueryParams queryParam = new QueryParams();
		queryParam.put("cateCode", catalog);
		queryParam.put("sysType", sysType);
		return repo.findByParam(AppConfig.class, queryParam);
    }

}
