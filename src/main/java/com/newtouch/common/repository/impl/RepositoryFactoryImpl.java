package com.newtouch.common.repository.impl;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Updates;
import com.newtouch.common.repository.Repository;
import com.newtouch.common.repository.RepositoryFactory;

/**
 * repository工厂，代理方式生成repository，将接口转到jpaRepository执行
 * 
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/4/2
 */
public class RepositoryFactoryImpl implements RepositoryFactory {
	private boolean singleton = true;
	private Class<?> repoClass;
	private EntityManager entityManager;
	private Repository jpaRepository;

	@Override
	public void setRepoClass(Class<?> clazz) {
		this.repoClass = clazz;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Repository getObject() throws Exception {
		ProxyFactory proxyFactory = new ProxyFactory(repoClass,
				new MethodInterceptor() {
					@Override
					public Object invoke(MethodInvocation invocation)
							throws Throwable {
						String name = invocation.getMethod().getName();
						Object[] vargs = invocation.getArguments();
						Inquiries inquiries = invocation.getMethod()
								.getAnnotation(Inquiries.class);
						Updates updates = invocation.getMethod().getAnnotation(
								Updates.class);

						Class returnType = invocation.getMethod()
								.getReturnType();

						if (inquiries != null && vargs != null
								&& (vargs.length == 2 || vargs.length == 3)) {
							if (List.class.isAssignableFrom(returnType)) {
								// 调用find
								if (vargs != null && vargs.length == 3) {
									name = "findByAnnotationAndSort";
								} else if (vargs != null && vargs.length == 2) {
									name = "findByAnnotation";
								}
							} else if (Page.class.isAssignableFrom(returnType)) {
								// 调用page
								name = "pageByAnnotation";
							} else {
								// 调用findone
								name = "findOneByAnnotation";
							}
							Object[] newVargs = new Object[vargs.length + 1];
							for (int i = 0; i < vargs.length; i++) {
								newVargs[i] = vargs[i];
							}
							newVargs[vargs.length] = inquiries;
							vargs = newVargs;
						} else if (updates != null && vargs != null
								&& vargs.length == 1) {
							name = "updateByAnnotation";
							vargs = new Object[] { vargs[0], updates };
						}

						Method method = BeanUtils
								.findMethodWithMinimalParameters(
										JpaRepositoryImpl.class, name);
						Object ret = method.invoke(jpaRepository, vargs);

						return ret;
					}
				});
		return (Repository) proxyFactory.getProxy();
	}

	@Override
	public Class<?> getObjectType() {
		return repoClass;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}

	@Override
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		jpaRepository = new JpaRepositoryImpl();
		((JpaRepositoryImpl) jpaRepository).setEntityManager(entityManager);
		((JpaRepositoryImpl) jpaRepository)
				.setQueryDefinitionService(new QueryDefinitionServiceImpl());
		((JpaRepositoryImpl) jpaRepository)
				.setUpdateDefinitionService(new UpdateDefinitionServiceImpl());
	}

}
