package com.newtouch.common.annotation.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.newtouch.common.annotation.cache.Cacheable.Catalog;

/**
 * 惰性数据缓存，name为数据描述，key为数据项
 * 
 * @author dongfeng.zhang
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheableData {
	Catalog catalog() default Cacheable.Catalog.IDCache;// 缓存区域

	String name() default "";// 缓存中的名称

	String key() default "";// 缓存中的key
}
