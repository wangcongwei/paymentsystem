package com.newtouch.common.service.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.base.Objects;
import com.newtouch.common.annotation.cache.Cacheable;
import com.newtouch.common.annotation.cache.CacheableConfig;
import com.newtouch.common.annotation.cache.CacheableData;
import com.newtouch.common.annotation.cache.CacheableDictionary;
import com.newtouch.common.annotation.cache.Cacheable.Catalog;

/**
 * 拦截PutCache注解进行缓存保存
 * 
 * @author dongfeng.zhang
 */
@Aspect
public class CacheAdvice {
	@Autowired
	private CacheInspect cacheInspect;

	/**
	 * 拦截putCache注解，并存储到缓存，根据数据库配置该缓存有效期失效立即清除该缓存
	 * 
	 * @param joinPoint
	 * @return
	 * @throws java.lang.Throwable
	 */
	@Around("@annotation(com.newtouch.common.annotation.cache.Cacheable)")
	public Object cacheable(ProceedingJoinPoint joinPoint) throws java.lang.Throwable {
		return process(joinPoint, Cacheable.class);
	}

	@Around("@annotation(com.newtouch.common.annotation.cache.CacheableConfig)")
	public Object cacheableConfig(ProceedingJoinPoint joinPoint) throws java.lang.Throwable {
		return process(joinPoint, CacheableConfig.class);
	}

	@Around("@annotation(com.newtouch.common.annotation.cache.CacheableData)")
	public Object cacheableData(ProceedingJoinPoint joinPoint) throws java.lang.Throwable {
		return process(joinPoint, CacheableData.class);
	}

	@Around("@annotation(com.newtouch.common.annotation.cache.CacheableDictionary)")
	public Object cacheableDictionary(ProceedingJoinPoint joinPoint) throws java.lang.Throwable {
		return process(joinPoint, CacheableDictionary.class);
	}

	/**
	 * 根据切面表达式和注解类型，进行调用缓存
	 * 
	 * @param joinPoint
	 * @param annotation
	 * @return
	 * @throws java.lang.Throwable
	 */
	private Object process(ProceedingJoinPoint joinPoint, Class annotation) throws java.lang.Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation anno = method.getAnnotation(annotation);
		Cacheable.Catalog catalog = null;
		String name = "";
		String key = "";

		Method catalogMethod = anno.getClass().getMethod("catalog");
		Method nameMethod = anno.getClass().getMethod("name");
		Method keyMethod = anno.getClass().getMethod("key");

		catalog = (Catalog) catalogMethod.invoke(anno);
		name = (String) nameMethod.invoke(anno);
		key = (String) keyMethod.invoke(anno);
		Assert.notNull(catalog);

		if (name.length() < 1) {
			Class clazz = joinPoint.getTarget().getClass();
			String methodName = method.getName();
			name = clazz.getName() + "." + methodName;
		}

		if (key.length() < 1) {

			String args = "";
			if(joinPoint.getArgs() != null && joinPoint.getArgs().length>0){
				args = args + Objects.hashCode(joinPoint.getArgs());
			}
			key = args;
		}

		Object returnValue = cacheInspect.getCachedData(catalog, name, key);
		if (returnValue == null) {
			returnValue = joinPoint.proceed(joinPoint.getArgs());
			if (returnValue != null)
				cacheInspect.putCachedData(catalog, name, key, returnValue);
		}
		return returnValue;
	}
}
