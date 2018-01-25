package com.newtouch.common.view.validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newtouch.common.model.ValidationDefine;
import com.newtouch.common.service.core.ValidationService;
import com.newtouch.common.view.JsonUtils;


/**
 * 校验规则支持
 *
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/4/13
 */
public class ValiRuleFilter implements Filter {
	private ValidationService validationService;

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	private Logger logger = LoggerFactory.getLogger(ValiRuleFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getRequestURI().endsWith("vali.rule") && validationService.isClientEnable()) {
			List<ValidationDefine> validateRuleList = validationService.getModuleRules(extractUri(httpRequest));
			if (validateRuleList != null&&validateRuleList.size()>0) {
				writeJson(httpResponse, validateRuleList);
			}
			return;
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

	private void writeJson(HttpServletResponse response, Object object) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			String json = JsonUtils.toJson(object);
			out = response.getWriter();
			out.append(json);
			logger.debug(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	private String extractUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		int beginIndex = contextPath.length();
		int endIndex = uri.indexOf("/vali.rule");
		endIndex = endIndex == -1 ? uri.length() : endIndex;
		return uri.substring(beginIndex, endIndex);
	}
}
