package com.newtouch.common.annotation.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用缓存注解catalog为缓存名字，name为缓存数据，key为缓存key
 * 
 * @author dongfeng.zhang
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
	public enum Catalog {
		DDCache("DDCache")/* 字典数据 */, CDCache("CDCache")/* 配置数据 */, IDCache("IDCache")/* 惰性数据 */;
		private String code = "";
		private String name = "";

		private Catalog(String name) {
			if (name.equals("DDCache")) {
				code = name;
				name = "字典数据";
			} else if (name.equals("CDCache")) {
				code = name;
				name = "配置数据";
			} else if (name.equals("IDCache")) {
				code = name;
				name = "惰性数据";
			}
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}

	Catalog catalog();// 缓存区域

	String name() default "";// 缓存中的名称

	String key();// 缓存中的key
}
