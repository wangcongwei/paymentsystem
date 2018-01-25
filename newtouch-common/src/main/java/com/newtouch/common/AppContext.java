package com.newtouch.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.newtouch.common.model.LoginToken;
import com.newtouch.common.model.core.AppConfig;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.common.service.core.CodeQueryService;
import com.newtouch.common.service.core.ConfigQueryService;

/**
 * 应用上下文，用户获取应用运行中的配置数据，上下文数据等
 *
 * @author dongfeng.zhang
 */
public class AppContext {
	public static final String CATALOG_CACHE_SWITCH = "CACHE-SWITCH";
	public static final String CATALOG_INTERFACE = "INTERFACE";
	public static final String CATALOG_INIT = "INIT";
	public static final String CATALOG_OTHER = "OTHER";
	
	private static Logger logger = LoggerFactory.getLogger(AppContext.class);
	private static CodeQueryService codeQueryService;
	private static ConfigQueryService configQueryService;

	
	/**
	 * 获取配置数据
	 *
	 * @param catalogCode
	 * @param key
	 * @return
	 */
	public static AppConfig getConf(String key, String catalogCode, String sysType) {
		Assert.notNull(catalogCode);
		Assert.notNull(key);
		Assert.notNull(sysType);
		AppConfig value = configQueryService.getConfig(key, catalogCode, sysType);
		return value;
	}

	/**
	 * 获取字典项
	 * 
	 * @param code
	 * @param catalogCode
	 * @param sysType
	 * @return
	 */
	public static CodeEntry getDic(String code, String catalogCode, String sysType) {
		if(code==null||catalogCode==null||sysType==null)
			return null;
		CodeEntry value = codeQueryService.getCode(code, catalogCode, sysType);
		return value;
	}

	/**
	 * 获取字典列表
	 * 
	 * @param path 根据path查询子代码
	 * @param catalogCode
	 * @param sysType
	 * @return
	 */
	public static List<CodeEntry> getDics(String path, String sysType) {
		return codeQueryService.getCodes(path, sysType);
	}


	/**
	 * 获取某项缓存开关
	 * 
	 * @param key
	 * @param sysType
	 * @return
	 */
	public static Boolean getCacheSwitch(String key, String sysType) {
		AppConfig value = getConf(key, CATALOG_CACHE_SWITCH, sysType);
		if (!value.getEnable())
			return false;
		Boolean _switch = false;
		try {
			_switch = Boolean.valueOf(value.getValue());
		} catch (Exception ex) {
			logger.warn("系统缓存配置错误,key=" + key + "，value=" + value + ",已经忽略该参数", ex);
		}
		return _switch;
	}

	/**
	 * 获取缓存开关数据
	 * 
	 * @param sysType
	 * @return
	 */
	public static Map<String, Boolean> getAllCacheSwitch(String sysType) {
		List<AppConfig> configList = configQueryService.getConfigs(CATALOG_CACHE_SWITCH, sysType);
		Map<String, Boolean> valueMap = new HashMap<String, Boolean>();
		for (AppConfig value : configList) {
			if (!value.getEnable())
				continue;
			else {
				Boolean _switch = false;
				try {
					_switch = Boolean.valueOf(value.getValue());
				} catch (Exception ex) {
					logger.warn("系统缓存配置错误,key=" + value.getValue() + "，value=" + _switch + ",已经忽略该参数", ex);
				}
				valueMap.put(value.getValue(), _switch);
			}
		}
		return valueMap;
	}
	/**
	 * 当前登陆用户
	 * 
	 * @return
	 */
	public static LoginToken currentUserToken() {
		LoginToken token = (LoginToken) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		return token;
	}

	public static Long getCurrentUserId() {
		return currentUserToken().getUserId();
	}

	public static String getCurrentUserName() {
		return currentUserToken().getUsername();
	}

	public static String getCurrentSysType() {
		return currentUserToken().getSysType();
	}
	
	/**
	 * 转换字典代码为字典名称
	 * 
	 * @param objects
	 * @param codePropertyName
	 *            代码字段
	 * @param targetPropertyName
	 *            转换结果字段
	 * @param catalogCode
	 * @param sysType
	 */
	
	public static void trans(Object single, String codePropertyName, String targetPropertyName, String catalogCode) {
		List objects=new ArrayList();
		objects.add(single);
		trans(objects,codePropertyName,targetPropertyName,catalogCode,getCurrentSysType());
	}
	
	public static void trans(Object single, String codePropertyName, String targetPropertyName, String catalogCode,
			String sysType) {
		List objects=new ArrayList();
		objects.add(single);
		trans(objects,codePropertyName,targetPropertyName,catalogCode,sysType);
	}
	
	public static void trans(List objects, String codePropertyName, String targetPropertyName, String catalogCode) {
		trans(objects,codePropertyName,targetPropertyName,catalogCode,getCurrentSysType());
	}
	
	public static void trans(List objects, String codePropertyName, String targetPropertyName, String catalogCode,
			String sysType) {

		Assert.notNull(catalogCode);
		Assert.notNull(sysType);
		Assert.notNull(objects);
		Assert.notNull(codePropertyName);
		Assert.notNull(targetPropertyName);

		for (Object o : objects) {
			try {
				String key = (String) PropertyUtils.getProperty(o, codePropertyName);
				if (key == null || "".equals(key))
					continue;

				String[] codes = key.split(",");
				String name = "";

				for (String code : codes) {
					CodeEntry value = getDic(code, catalogCode, sysType);
					if(value==null){
						continue;
					}
					name += value.getName() + ",";
				}
				if(name.endsWith(",")){
					name = name.substring(0, name.lastIndexOf(","));
				}
				PropertyUtils.setProperty(o, targetPropertyName, name);
			} catch (Exception ex) {
				logger.warn("translate Dic error:", ex);
			}
		}
	}

	public static String getProperty(String name) {
		String env = System.getenv(name);
		String property = System.getProperty(name);
		return env == null ? property : env;
	}

	public void setCodeQueryService(CodeQueryService codeQueryService) {
		AppContext.codeQueryService = codeQueryService;
	}

	public void setConfigQueryService(ConfigQueryService configQueryService) {
		AppContext.configQueryService = configQueryService;
	}

}
