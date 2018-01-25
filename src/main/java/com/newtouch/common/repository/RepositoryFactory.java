package com.newtouch.common.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * JPA数据访问接口
 *
 * @author dongfeng.zhang
 */
public interface RepositoryFactory extends FactoryBean<Repository>,
		InitializingBean {
	/**
	 * 设置仓储接口
	 *
	 * @param clazz
	 */
	public void setRepoClass(Class<?> clazz);

	/**
	 * 设置是否单例模式
	 *
	 * @param singleton
	 */
	public void setSingleton(boolean singleton);

	/**
	 * 获取EntityManager对象
	 *
	 * @return
	 */
	public void setEntityManager(EntityManager entityManager);

}
