package com.newtouch.common.annotation.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.newtouch.common.annotation.cache.Cacheable.Catalog;

/**
 * 字典数据缓存，name为字典大类，key为字典条目
 * 
 * @author dongfeng.zhang
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheableDictionary {
	Catalog catalog() default Cacheable.Catalog.DDCache;// 缓存区域

	String name() default "";// 缓存中的名称

	String key() default "";// 缓存中的key
}
