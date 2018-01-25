package com.newtouch.common.repository.impl;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.newtouch.common.repository.ScanRepositoryInterface;
/**
 * 扫描并注册Repository到spring上下文
 * 
 * @author dongfeng.zhang
 * 
 */
public class ScanRepositoryInterfaceImpl implements ScanRepositoryInterface, ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(ScanRepositoryInterfaceImpl.class);
	private boolean singleton = true;
	private EntityManager entityManager;
	private ApplicationContext context;
	private String[] classToScan;

	@Override
	public void setClassToScan(String... classToScan) {
		this.classToScan = classToScan;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void init() {
		for (String className : classToScan) {
			try {
				logger.info("starting register repo: {}",className);
				registerBean(className);
			} catch (Exception e) {
				logger.error("扫描并注册repository错误:{}！", className);
			}
		}
	}

	/**
	 * @desc 向spring容器注册bean
	 * @param beanName
	 * @param beanDefinition
	 * @throws ClassNotFoundException
	 */
	private void registerBean(String repositoryClassName) throws ClassNotFoundException {
		if (!context.containsBean(repositoryClassName)) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RepositoryFactoryImpl.class);
			beanDefinitionBuilder.addPropertyValue("entityManager", entityManager);
			beanDefinitionBuilder.addPropertyValue("repoClass", Class.forName(repositoryClassName));
			beanDefinitionBuilder.addPropertyValue("singleton", singleton);
			ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
			BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
			beanDefinitonRegistry.registerBeanDefinition(repositoryClassName, beanDefinitionBuilder.getRawBeanDefinition());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
