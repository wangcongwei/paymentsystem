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
@Target({ ElementType.METHOD })
@Documented
public @interface Updates {
	Update[] update() default {};// 从查询语句用于对查询结果的更新填充
}
