package com.newtouch.common.service.core;

import java.util.List;

import com.newtouch.common.model.ValidationDefine;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;

/**
 * 根据请求uri获取校验规则
 * 
 * @author dongfeng.zhang
 *
 */
public interface ValidationService {
	/**
	 * 通过接口uri获取该模块下属的所有校验规则
	 * 
	 * @param request
	 * @return 校验规则
	 */
	public List<ValidationDefine> getModuleRules(String uri);
	/**
	 * 通过接口uri获取该模块下属的所有校验规则
	 * 
	 * @param request
	 * @return 校验规则
	 */
	public ValidationDefine getIntRule(String uri,String intPath,String method);
	/**
	 * 请求数据校验
	 * @param request
	 * @return
	 */
	public RespModel validate(ValidationDefine reqRuleModel, ReqModel reqModel);
	
	public boolean isServerEnable();

	public boolean isClientEnable();

}
