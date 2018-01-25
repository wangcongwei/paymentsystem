package com.newtouch.common.service.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ArrayListMultimap;
import com.newtouch.common.model.ValidationDefine;
import com.newtouch.common.model.ValidationFieldRule;
import com.newtouch.common.service.core.ValidationService;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.common.view.Views;
import com.newtouch.common.view.validation.ValidateUtil;

/**
 * 校验规则数据支持
 * 
 * @author dongfeng.zhang
 */
public class ValidationServiceImpl implements ValidationService {
	private Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);
	private boolean clientEnable = true;
	private boolean serverEnable = true;
	private static ArrayListMultimap<String, ValidationDefine> valiMultimap = ArrayListMultimap.create();

	@Override
	public List<ValidationDefine> getModuleRules(String uri) {
		if (StringUtils.isEmpty(uri)) {
			return Collections.EMPTY_LIST;
		}
		return valiMultimap.get(uri);
	}

	@Override
	public ValidationDefine getIntRule(String uri, String intPath, String method) {
		if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(method)) {
			return null;
		}
		if (StringUtils.isEmpty(intPath)) {
			intPath = "/";
		}

		for (String key : valiMultimap.keySet()) {
			if (uri.indexOf(key) == 0) {
				List<ValidationDefine> reqRules = valiMultimap.get(key);
				for (ValidationDefine rule : reqRules) {
					if (matcher(intPath, rule.getIntPath()) && rule.getIntMethod().indexOf(method) != -1) {
						return rule;
					}
				}
			}
		}
		return null;
	}

	@Override
	public RespModel validate(ValidationDefine reqRuleModel, ReqModel reqModel) {
		if (reqRuleModel != null) {
			Map<String, String> errMaps = ValidateUtil.validate(reqModel, reqRuleModel.getFields());
			logger.debug("validErr:{}", errMaps);
			if (errMaps.size() > 0) {
				RespModel resp = Views.getErrorModel("Request.ValidateErr");
				resp.setValidateErrs(errMaps);
				return resp;
			}
		}
		return null;
	}

	@Override
	public boolean isClientEnable() {
		return clientEnable;
	}

	@Override
	public boolean isServerEnable() {
		return serverEnable;
	}

	public void setClientEnable(boolean clientEnable) {
		this.clientEnable = clientEnable;
	}

	public void setServerEnable(boolean serverEnable) {
		this.serverEnable = serverEnable;
	}

	public void setResource(String configLocations) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources(configLocations);
			for (Resource resource : resources) {
				StringBuilder text = new StringBuilder();
				String LS = System.getProperty("line.separator");
				Scanner scanner = new Scanner(resource.getInputStream(), "UTF-8");
				try {
					while (scanner.hasNextLine()) {
						text.append(scanner.nextLine() + LS);
					}
				} finally {
					scanner.close();
				}

				Class clazz = getControllerClass(((FileSystemResource) resource).getPath());
				String domainUrl = getDomainUrl(clazz);
				loadRuleData(domainUrl, text.toString());
			}

		} catch (IOException e) {
			logger.error("MVC数据校验插件配置文件加错误[" + configLocations + "]！", e);
		}
	}

	private void loadRuleData(String domainUrl, String content) {
		try {
			Document document = DocumentHelper.parseText(content);
			Element root = document.getRootElement();
			List<Element> requestEles = root.elements();// request节点
			for (Element requestEle : requestEles) {
				ValidationDefine req = new ValidationDefine();
				List<ValidationFieldRule> fields = new ArrayList<ValidationFieldRule>();

				List<Element> fieldEles = requestEle.elements();
				for (Element fieldEle : fieldEles) {
					ValidationFieldRule field = new ValidationFieldRule();
					field.setName(fieldEle.attributeValue("name"));
					field.setType(fieldEle.attributeValue("type"));
					field.setRequired(Boolean.parseBoolean(fieldEle.attributeValue("required")));

					field.setMaxLen(Integer.parseInt(fieldEle.attributeValue("max-len")));
					field.setMinLen(Integer.parseInt(fieldEle.attributeValue("min-len")));
					field.setErrMsg(fieldEle.attributeValue("err-msg"));
					field.setErrMsgCode(fieldEle.attributeValue("err-msg-code"));

					field.setRangeStart(fieldEle.attributeValue("range-start"));
					field.setRangeEnd(fieldEle.attributeValue("range-end"));

					field.setPattern(fieldEle.attributeValue("pattern"));
					fields.add(field);
				}
				String method = requestEle.attributeValue("method");
				req.setIntPath(requestEle.attributeValue("int-path"));
				req.setIntMethod(method);
				req.setClientEnable(Boolean.parseBoolean(requestEle.attributeValue("client-enable")));
				req.setServerEnable(Boolean.parseBoolean(requestEle.attributeValue("server-enable")));
				req.setFields(fields);
				if (method == null || req.getIntPath() == null) {
					continue;
				}
				valiMultimap.put(domainUrl, req);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private String getDomainUrl(Class clazz) {
		RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
		if (requestMapping == null) {
			return "";
		}
		return requestMapping.value()[0];
	}

	private Class getControllerClass(String path) {
		try {
			String classPath = StringUtils.delete(StringUtils.replace(path, "/", "."), ".vali.xml");
			classPath = classPath.substring(classPath.indexOf("com.newtouch"));
			return Class.forName(classPath);
		} catch (Exception e) {
			logger.error("MVC数据校验插件配置错误,配置文件和controller匹配，请检查配置文件名称 file=[" + path + "]", e);
		}
		return null;
	}

	private boolean matcher(String requestIntPath, String configIntPath) {
		if (configIntPath == null || requestIntPath == null) {
			return true;
		}
		// 先替换多个斜杠为一个斜杠，再替换参数为正则表达式
		String regx = configIntPath.replaceAll("([/]+)", "/").replaceAll("(\\{.+?\\})", "([A-Za-z0-9_\\-]+)?");
		return requestIntPath.matches(regx);
	}

}
