package com.newtouch.common.view.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newtouch.common.model.ValidationDefine;
import com.newtouch.common.service.core.ValidationService;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;

@Aspect
public class ValidateExecute {
	private Logger logger = LoggerFactory.getLogger(ValidateExecute.class);
	private ValidationService validationService;

	@Around("execution(* com.newtouch..*.*(com.newtouch.common.view.ReqModel,..))")
	public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		RequestMapping classRequestMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
		RequestMapping methodRequestMapping = signature.getMethod().getAnnotation(RequestMapping.class);
		Class returnTypetype = signature.getReturnType();
		if (validationService.isServerEnable() && RespModel.class.isAssignableFrom(returnTypetype)) {

			ReqModel reqModel = (ReqModel) joinPoint.getArgs()[0];
			String uri = arrayToString(classRequestMapping.value());
			String intPath = arrayToString(methodRequestMapping.value());
			String intMethod = arrayToString(methodRequestMapping.method());
			ValidationDefine reqRuleModel = validationService.getIntRule(uri,intPath,intMethod);
			RespModel resp = validationService.validate(reqRuleModel, reqModel);
			if (resp != null) {
				return resp;
			}
		}
		return joinPoint.proceed();
	}

	private String arrayToString(Object[] array) {
		
		int iMax = array.length - 1;
		if (array == null || iMax == -1) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i != iMax)
				builder.append(", ");
		}
		return builder.toString();
	}

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}
}
