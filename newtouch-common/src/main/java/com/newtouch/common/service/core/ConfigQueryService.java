package com.newtouch.common.service.core;

import java.util.List;

import com.newtouch.common.model.core.AppConfig;

/**
 * 配置数据
 * 
 * @author dongfeng.zhang
 */
public interface ConfigQueryService {

	/**
	 * 查询配置
	 * 
	 * @param key
	 *            配置键值
	 * @param catalog
	 *            配置大类
	 * @param sysType
	 *            系统类型（产寿标识）
	 * @return 配置项
	 */
	AppConfig getConfig(String key, String catalog, String sysType);

	/**
	 * 查询大类下配置
	 * 
	 * @param catalog
	 *            配置大类
	 * @param sysType
	 *            系统类型（产寿标识）
	 * @return 配置项
	 */
	List<AppConfig> getConfigs(String catalog, String sysType);

}
