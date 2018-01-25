package com.newtouch.common.annotation.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.newtouch.common.annotation.cache.Cacheable.Catalog;

/**
 * 配置缓存，name为配置类型，key为配置项
 * 
 * @author dongfeng.zhang
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheableConfig {
	Catalog catalog() default Cacheable.Catalog.CDCache;// 缓存区域

	String name() default "";// 缓存中的名称

	String key() default "";// 缓存中的key
}
