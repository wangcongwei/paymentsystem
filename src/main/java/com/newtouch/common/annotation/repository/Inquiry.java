package com.newtouch.common.annotation.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义在查询对象上面的注解，用于声明查询的实体及之间的关系和固定条件等
 * 
 * @author dongfeng.zhang
 * 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Inquiry {
	public static final String NAME_PREFIX = "{";
	public static final String NAME_SUFFIX = "}";
	public static final String COND_PREFIX = "/*";
	public static final String COND_SUFFIX = "*/";

	String target() default "";

	// 用于查询的jpa查询语句模板，支持动态条件，支持参数
	// 查询结果字段：字段名
	// 参数使用例子：user.type={type},"{}"包含的部分被解析为本对象的某个字段
	// 动态条件支持例子：/* and username like {username}*/，/*..*/"包含的部分
	String value();

}
