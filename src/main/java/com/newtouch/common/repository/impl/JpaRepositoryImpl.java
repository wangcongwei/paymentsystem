package com.newtouch.common.repository.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import com.newtouch.common.AppContext;
import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Updates;
import com.newtouch.common.model.BaseEntity;
import com.newtouch.common.model.PageData;
import com.newtouch.common.model.QueryDefinition;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.QueryDefinitionService;
import com.newtouch.common.repository.Repository;
import com.newtouch.common.repository.UpdateDefinitionService;

/**
 * repository 实现，所有自定义的repository接口将通过该类执行
 * 
 * @author dongfeng.zhang
 *
 */
public class JpaRepositoryImpl implements Repository {
	private static final String FIND_JPQL = "select o from ? o ";
	private static final String COUNT_JPQL = "select count(*) from ? o ";
	private static final String OBJECT_ALIAS = "o";
	private static final String POS = "?";
	private static final String EMPTY = "";
	private Logger logger = LoggerFactory.getLogger(JpaRepositoryImpl.class);
	private EntityManager entityManager;
	private QueryDefinitionService queryDefinitionService;
	private UpdateDefinitionService updateDefinitionService;
	private int maxResults = 150;

	@Override
	public <T> void save(T entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity be = ((BaseEntity) entity);
			if (be.getCrtDttm() == null) {
				be.setCrtDttm(new Timestamp(System.currentTimeMillis()));
			}
			if (be.getCrtUserId() == null) {
				be.setCrtUserId(AppContext.currentUserToken().getUserId());
			}
			if (be.getLastuptDttm() == null) {
				be.setLastuptDttm(new Timestamp(System.currentTimeMillis()));
			}
			if (be.getLastuptUserId() == null) {
				be.setLastuptUserId(AppContext.currentUserToken().getUserId());
			}
		}
		entityManager.persist(entity);
	}

	@Override
	public <T> void update(T entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity be = ((BaseEntity) entity);
			if (be.getLastuptDttm() == null) {
				be.setLastuptDttm(new Timestamp(System.currentTimeMillis()));
			}
			if (be.getLastuptUserId() == null) {
				be.setLastuptUserId(AppContext.currentUserToken().getUserId());
			}
		}
		entityManager.merge(entity);
	}

	@Override
	public <T> void deleteById(Class<T> entityClass, Object id) {
		entityManager.remove(this.getById(entityClass, id));
	}

	@Override
	public <T> void logicDeleteById(Class<T> entityClass, Object id) {
		Object model = this.getById(entityClass, id);
		try {
			if (model instanceof BaseEntity) {
				BaseEntity be = ((BaseEntity) model);
				if (be.getLastuptDttm() == null) {
					be.setLastuptDttm(new Timestamp(System.currentTimeMillis()));
				}
				if (be.getLastuptUserId() == null) {
					be.setLastuptUserId(AppContext.currentUserToken().getUserId());
				}
			}
			PropertyUtils.setProperty(model, "enable", false);
			entityManager.merge(model);
		} catch (Exception ex) {
			throw new RuntimeException("logic delete Error!", ex);
		}
	}

	@Override
	public <T> T getById(Class<T> entityClass, Object id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public <T> T findOneByParam(Class<T> entityClass, QueryParams queryParams) {
		String jpql = buildJpql(entityClass, queryParams, null);
		TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		fillParams(query, queryParams);
		T entity = null;
		try {
			entity = query.getSingleResult();
		} catch (RuntimeException ex) {
			// return null
		}
		return entity;
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return findByParamAndSort(entityClass, new QueryParams(), null);
	}

	@Override
	public <T> List<T> findByParam(Class<T> entityClass, QueryParams queryParam) {
		return findByParamAndSort(entityClass, queryParam, null);
	}

	@Override
	public <T> List<T> findByParamAndSort(Class<T> entityClass,
			QueryParams queryParam, Sort sort) {
		String jpql = buildJpql(entityClass, queryParam, sort);
		TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		fillParams(query, queryParam);
		query.setMaxResults(this.maxResults);// 限制返回记录行数
		return query.getResultList();
	}

	@Override
	public <T> Page<T> pageByParam(Class<T> entityClass,
			QueryParams queryParams, Pageable pageable) {
		String jpql = buildJpql(entityClass, queryParams, pageable.getSort());
		TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		fillParams(query, queryParams);

		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());// 限制返回记录行数
		List<T> list = query.getResultList();
		Long rows = count(entityClass, queryParams);
		return new PageData<T>(list, pageable, rows);
	}

	/**
	 * 拼接where
	 *
	 * @param prefix
	 * @param queryParams
	 * @return
	 */
	private String where(String prefix, QueryParams queryParams) {
		if (queryParams == null || queryParams.isEmpty())
			return EMPTY;
		StringBuilder sb = new StringBuilder(" where 1=1 ");
		for (String property : queryParams.keySet()) {
			Object value = queryParams.get(property);
			if (value == null)
				sb.append(" and ").append(prefix).append(".").append(property)
						.append(" is null ");
			else if (value instanceof String
					&& ((String) value).indexOf("%") != -1)
				sb.append(" and ").append(prefix).append(".").append(property)
						.append(" like ").append(POS);
			else
				sb.append(" and ").append(prefix).append(".").append(property)
						.append(" = ").append(POS);
		}
		return sb.toString();
	}

	/**
	 * 拼接order
	 *
	 * @param prefix
	 * @param sort
	 * @return
	 */

	private String orderBy(String prefix, Sort sort) {
		if (sort == null)
			return EMPTY;
		StringBuilder sb = new StringBuilder(" order by ");
		Iterator<Sort.Order> iterator = sort.iterator();
		while (iterator.hasNext()) {
			Sort.Order order = iterator.next();
			sb.append(prefix).append(".").append(order.getProperty())
					.append(" ").append(order.getDirection().name())
					.append(",");
		}
		return sb.toString().substring(0, sb.toString().lastIndexOf(","));
	}

	/**
	 * 拼接count语句，并计算
	 *
	 * @param queryParams
	 * @param <T>
	 * @return
	 */

	private <T> Long count(Class<T> entityClass, QueryParams queryParams) {
		String jpql = COUNT_JPQL.replace(POS, entityClass.getName());
		String where = where(OBJECT_ALIAS, queryParams);
		TypedQuery<Long> query = entityManager.createQuery(jpql + where,
				Long.class);
		fillParams(query, queryParams);
		return query.getSingleResult();
	}

	/**
	 * 填充条件
	 *
	 * @param query
	 * @param queryParams
	 * @return
	 */

	private void fillParams(Query query, QueryParams queryParams) {
		if (queryParams == null || queryParams.isEmpty())
			return;
		int i = 1;
		for (String property : queryParams.keySet()) {
			if (queryParams.get(property) == null)
				continue;
			query.setParameter(i++, queryParams.get(property));
			if (logger.isDebugEnabled())
				logger.debug("parameter[" + i + "]="
						+ queryParams.get(property));
		}
	}

	/**
	 * 拼接jpql
	 *
	 * @param queryParams
	 * @param sort
	 * @param <T>
	 * @return
	 */

	private <T> String buildJpql(Class<T> entityClass, QueryParams queryParams,
			Sort sort) {
		String jpql = FIND_JPQL.replace(POS, entityClass.getName())
				+ where(OBJECT_ALIAS, queryParams)
				+ orderBy(OBJECT_ALIAS, sort);
		logger.debug(jpql);
		return jpql;
	}

	/**
	 * 根据查询对象，查询语句模板查询单个对象
	 *
	 * @param queryParams
	 *            根据对象属性进行查询
	 * @return 查询结果对象
	 */
	public <V> V findOneByAnnotation(Class<V> returnClass,
			QueryParams queryParams, Inquiries inquiries) {
		V object = null;
		List<V> values = this.pageByAnnotation(returnClass, queryParams, null,
				inquiries).getContent();
		if (values.size() > 0)
			object = values.get(0);
		return object;
	}

	/**
	 * 根据查询对象，查询语句模板查询分页对象
	 *
	 * @param queryParams
	 *            根据对象属性进行查询
	 * @return 查询结果对象分页
	 */
	public <V> List<V> findByAnnotation(Class<V> returnClass,
			QueryParams queryParams, Inquiries inquiries) {
		List<V> returnObjectList = this.pageByAnnotation(returnClass,
				queryParams, null, inquiries).getContent();
		return returnObjectList;
	}

	/**
	 * 根据查询对象，查询语句模板查询分页对象
	 *
	 * @param queryParams
	 *            根据对象属性进行查询
	 * @param sort
	 *            排序
	 * @return 查询结果对象分页
	 */
	public <V> List<V> findByAnnotationAndSort(Class<V> returnClass,
			QueryParams queryParams, Sort sort, Inquiries inquiries) {
		List<V> returnObjectList = this.pageByAnnotation(returnClass,
				queryParams, new PageRequest(0, Integer.MAX_VALUE, sort),
				inquiries).getContent();
		return returnObjectList;
	}

	/**
	 * 根据查询对象，查询语句模板查询分页对象
	 *
	 * @param queryParams
	 *            根据对象属性进行查询
	 * @param pageable
	 *            分页请求
	 * @return 查询结果对象分页
	 */
	public <V> Page<V> pageByAnnotation(Class<V> returnClass,
			QueryParams queryParams, Pageable pageable, Inquiries inquiries) {
		try {
			Assert.notNull(queryParams, "query params object is null");
			List<QueryDefinition> defs = queryDefinitionService
					.getQueryDefinnetions(queryParams, inquiries);
			List<V> returnObjectList = new ArrayList<V>();
			long total = 0;
			for (int i = 0; i < defs.size(); i++) {
				if (i == 0) {
					QueryDefinition master = defs.get(i);
					total = queryCount(master.getCountJpqlString(),
							master.getParamMap());// 主分页
					returnObjectList = buildReturnObjectList(master,
							returnClass, pageable);
				} else {
					if (returnObjectList != null) {
						for (V returnObject : returnObjectList) {
							updateReturnObject(defs.get(i), returnObject);
						}
					}
				}
			}
			return new PageData(returnObjectList, pageable, total);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 根据查询对象，查询语句模板查询分页对象
	 *
	 * @param queryParams
	 *            根据对象属性进行查询
	 * @param pageable
	 *            分页请求
	 * @return 查询结果对象分页
	 */
	public int updateByAnnotation(QueryParams queryParams, Updates updates) {
		int updateCount = 0;
		try {
			Assert.notNull(queryParams, "update params object is null");
			List<QueryDefinition> defs = updateDefinitionService
					.getUpdateDefinnetions(queryParams, updates);
			for (int i = 0; i < defs.size(); i++) {
				QueryDefinition def = defs.get(i);
				String jpql = def.getJpqlString();
				QueryParams paramMap = def.getParamMap();

				Query query = entityManager.createQuery(jpql);
				for (String key : paramMap.keySet()) {
					query.setParameter(key, paramMap.get(key));
				}
				updateCount = query.executeUpdate();// 最后一次update
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return updateCount;
	}

	/**
	 * 单条查询JPQL语句转换为返回对象
	 *
	 * @param def
	 *            查询语句及配置
	 * @param returnClass
	 *            返回对象类型
	 * @param returnClass
	 *            返回对象类型
	 * @param pageable
	 *            分页
	 * @return 查询结果组装后的list
	 */
	private <V> List<V> buildReturnObjectList(QueryDefinition def,
			Class<V> returnClass, Pageable pageable) {
		try {
			List results = queryList(def.getJpqlString(), def.getParamMap(),
					pageable);
			List values = new ArrayList();

			List<String> fieldNames = def.getFieldNames();
			for (Object record : results) {// 组装对象列表
				Object value = returnClass.newInstance();
				record2bean(value, fieldNames, record);
				values.add(value);
			}
			return values;
		} catch (Exception e) {
			throw new RuntimeException("jpql result fill simple object error:",
					e);
		}
	}

	/**
	 * 通过查询语句更新返回对象数据
	 *
	 * @param def
	 *            查询语句及配置
	 * @param returnObject
	 *            返回对象本身
	 */
	private <V> void updateReturnObject(QueryDefinition def, V returnObject) {
		try {
			String target = def.getTarget();

			boolean isList = false;
			PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(returnObject, target);

			if (target!=null && !target.equals("this")) {
				isList = propertyDescriptor.getPropertyType().isAssignableFrom(List.class);
			}

			// 获取需要更新的属性对象如果是list获取到list包含的对象类型
			Object targetObject = null;
			Class targetListSubClass = null;
			if (target==null || target.length()==0 ||target.equals("this")) {// this的属性对象是本身
				targetObject = returnObject;
			} else {
				targetObject = PropertyUtils.getProperty(returnObject, target);

				if (!isList) {
					if (targetObject == null) {
						targetObject = propertyDescriptor.getPropertyType().newInstance();
					}
				} else {
					if (targetObject == null) {
						targetObject = new ArrayList();
					}
					Field field = returnObject.getClass().getDeclaredField(target);
					Type fieldGenericType = field.getGenericType();
					if (fieldGenericType == null)
						throw new RuntimeException("list not defined generic parameter");

					targetListSubClass = (Class) ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];
				}

			}
			
			
			Iterator<Map.Entry<String, Object>> iterator=def.getParamMap().entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Object> entry=iterator.next();
				String key=entry.getKey();
				if (key.startsWith("this.") || key.startsWith("this_")) {
					Object value = PropertyUtils.getProperty(returnObject, key.substring(5));
					String newKey = key.replace("this.", "this_");
					iterator.remove();
					def.getParamMap().put(newKey, value);
					def.setJpqlString(def.getJpqlString().replace(key, newKey));
				}
			}

			List results = queryList(def.getJpqlString(), def.getParamMap(), null);

			Object bean = targetObject;

			for (Object record : results) {// 组装对象列表
				List<String> fieldNames = def.getFieldNames();
				if (isList) {
					bean = targetListSubClass.newInstance();
				}
				record2bean(bean, fieldNames, record);
				if (isList) {
					((List) targetObject).add(bean);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("jpql result fill simple object error:", e);
		}
	}

	/**
	 * 转换记录行为对象
	 *
	 * @param bean
	 *            需要转换的目标对象
	 * @param fieldNames
	 *            待转换字段列表
	 * @param record
	 *            查询结果记录
	 * @throws IllegalAccessException
	 * @throws java.lang.reflect.InvocationTargetException
	 */
	private void record2bean(Object bean, List<String> fieldNames, Object record)
			throws IllegalAccessException, InvocationTargetException {
		if (!record.getClass().isArray()) {
			record = new Object[] { record };
		}
		for (int index = 0; index < fieldNames.size(); index++) {// 组装属性列表
			String fieldName = fieldNames.get(index);
			fieldName = fieldName.replaceAll("\\$", ".");
			Object value = ((Object[]) record)[index];
			if (value != null) {
				BeanUtils.setProperty(bean, fieldName.trim(), value);
			}
		}
	}

	/**
	 * 统计行数
	 *
	 * @param countJpql
	 * @param paramMap
	 * @return
	 */
	private Long queryCount(String countJpql, QueryParams paramMap) {
		Query query = entityManager.createQuery(countJpql);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		return (Long) query.getSingleResult();
	}

	/**
	 * 获取根据JPQL语句查询对象
	 *
	 * @param jpql
	 * @param paramMap
	 * @param pageable
	 * @return
	 */
	private <V> List<V> queryList(String jpql, QueryParams paramMap,
			Pageable pageable) {
		Query query = entityManager.createQuery(jpql);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		if (pageable == null) {
			pageable = new PageRequest(0, maxResults);
		}
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		return (List<V>) query.getResultList();
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setQueryDefinitionService(
			QueryDefinitionService queryDefinitionService) {
		this.queryDefinitionService = queryDefinitionService;
	}

	public void setUpdateDefinitionService(
			UpdateDefinitionService updateDefinitionService) {
		this.updateDefinitionService = updateDefinitionService;
	}

}
