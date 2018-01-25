package com.newtouch.common.repository;

import javax.persistence.EntityManager;

/**
 * 扫描respository接口并注册到spring上下文
 * 
 * @author dongfeng.zhang
 */

public interface ScanRepositoryInterface {

	/**
	 * 扫描包路径
	 * 
	 * @param configLocations
	 */
	public void setClassToScan(String... classToScan);

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
