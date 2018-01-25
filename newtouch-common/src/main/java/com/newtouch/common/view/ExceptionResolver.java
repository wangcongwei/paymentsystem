package com.newtouch.common.view;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.newtouch.common.exception.ApplicationException;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/23
 */
public class ExceptionResolver extends DefaultHandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.warn("ResolveException", ex);
		String code;
		String message;

		if (ex instanceof ApplicationException) {
			code = ((ApplicationException) ex).getCode();
			message = ex.getMessage();
			if (!StringUtils.hasText(message)) {
				message = Views.getI18nMessage(ApplicationException.PREFIX + code, ((ApplicationException) ex).getVargs(), "unknown");
			}
		} else {
			logger.error("unknown exception:", ex);
			code = "unknown";
			message = "unknown exception";
		}

		if ((request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf(
                "XMLHttpRequest") > -1)) {// ajax错误信息
			ModelAndView modelAndView = new ModelAndView();
			RespModel model = new RespModel();
			model.setTimestamp(System.currentTimeMillis());
			model.setSuccess(false);
			model.setErrCode(code);
			model.setErrMsg(message);
			return model.toModelAndView();
		} else {// 常规HTTP请求异常

			ModelAndView httpExModelAndView = super.doResolveException(request, response, handler, ex);

			if (httpExModelAndView == null) {// 非标准http异常，即应用异常
				ApplicationException ae;
				if (ex instanceof ApplicationException) {
					ae = (ApplicationException) ex;
				} else {
					ae = new ApplicationException(code, message);
				}

				try {
					super.sendServerError(ae, request, response);
				} catch (Exception handlerException) {
					logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
				}

				return new ModelAndView();
			} else {// 标准http异常
				return httpExModelAndView;
			}
		}
	}

}
