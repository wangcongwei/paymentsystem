package com.newtouch.common.view;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import com.newtouch.common.exception.ApplicationException;
import com.newtouch.common.model.QueryParams;

/**
 * 模型转换工具类
 * 
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/31
 */
public class ModelConvertUtils {
	private static Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
	private final static String TYPE_SUFF = "$$type";

	/**
	 * 转换请求内容为定义类型（传入的$$type定义，支持pojo转换）
	 * 
	 * @param content
	 * @return
	 */
	public static Object convertContent(Object content) {
		Object convertedContent = content;
		if (content instanceof Map) {
			convertedContent = updateMapType((Map<String, Object>) content);
		} else if (content instanceof List) {
			convertedContent = new ArrayList();
			for (Object o : (List) content) {
				Object value;
				if (o == null) {
					continue;
				}
				if (o instanceof Map) {
					value = updateMapType((Map<String, Object>) o);
				} else {
					value = o;
				}
				((List) convertedContent).add(value);
			}
		} else {
			convertedContent = content;
		}
		return convertedContent;
	}

	/**
	 * 获取请求对象中的分页参数
	 * 
	 * @param content
	 * @return
	 */
	public static Pageable getPageable(Object content) {
		if (content instanceof Map) {
			Map mapContent = (Map) content;
			Integer page = (Integer) mapContent.get("pageIndex");
			Integer size = (Integer) mapContent.get("pageSize");
			String directionStr = (String) mapContent.get("direction");
			String orderFieldsStr = (String) mapContent.get("orderFields");
			if (page == null) {
				page = 0;
			} else {
				page = page - 1;
			}
			if (size == null) {
				size = 10;
			}
			PageRequest pageRequest;
			if (directionStr != null) {
				Direction direction = Direction.fromString(directionStr);
				String[] properties = orderFieldsStr.split(",");
				pageRequest = new PageRequest(page, size, direction, properties);
			} else {
				pageRequest = new PageRequest(page, size);
			}
			return pageRequest;
		}
		throw new ApplicationException("err.request.InvalidFormat", "no Pageable field");
	}

	/**
	 * 获取请求对象中请求参数
	 * 
	 * @param content
	 * @return
	 */
	public static QueryParams getQueryParams(Object content) {
		QueryParams queryParams = new QueryParams();
		if (content == null || !(content instanceof Map)) {
			return queryParams;
		}

		Map mapContent = (Map) content;
		queryParams.putAll(mapContent);
		queryParams.remove("pageIndex");
		queryParams.remove("pageSize");
		return queryParams;
	}

	// 转换请求模型到POJO对象
	public static <T> T toObject(Object content, Class<T> clazz) {
		T object = null;
		try {
			if (HashMap.class.isAssignableFrom(clazz) && content instanceof Map)
				return (T) content;
			object = clazz.newInstance();
			PropertyUtils.copyPropertiesSkipNull(object, content);
		} catch (Exception e) {
			logger.error("ReqModel to Object Error:", e);
			throw new ApplicationException("err.request.InvalidFormat");
		}
		return object;
	}

	/**
	 * 更新数据类型（内部依赖方法）
	 * 
	 * @param oldMap
	 * @return
	 */
	private static Map<String, Object> updateMapType(Map<String, Object> oldMap) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		if (oldMap == null || oldMap.isEmpty()) {
			return newMap;
		}
		Iterator<Map.Entry<String, Object>> iterator = oldMap.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			if (key.endsWith(TYPE_SUFF)) {
				continue;
			}
			String typeKey = key + TYPE_SUFF;
			String t = (String) oldMap.get(typeKey);
			Object v = oldMap.get(key);
			if (v == null || v.toString().trim().equals("")) {
				continue;
			}

			v = convert(v, t);
			newMap.put((String) key, v);
		}
		return newMap;
	}

	/**
	 * 根据类型转换数据（内部依赖方法）
	 * 
	 * @param object
	 * @param type
	 * @return
	 */
	private static Object convert(Object object, String type) {
		// 无类型数据直接返回
		if (StringUtils.isEmpty(type)) {
			return object;// 不转换直接返回
		}

		// 断定类型是否基本类型
		Class clazz = null;
		if ("text".equalsIgnoreCase(type) || "string".equalsIgnoreCase(type)) {
			clazz = String.class;
		} else if ("long".equalsIgnoreCase(type)) {
			clazz = Long.class;
		} else if ("int".equalsIgnoreCase(type)) {
			clazz = Integer.class;
		} else if ("time".equalsIgnoreCase(type)) {
			clazz = Time.class;
		} else if ("date".equalsIgnoreCase(type)) {
			clazz = Date.class;
		} else if ("datetime".equalsIgnoreCase(type) || "timestamp".equalsIgnoreCase(type)) {
			clazz = Timestamp.class;
		} else if ("boolean".equalsIgnoreCase(type)) {
			clazz = Boolean.class;
		} else if ("double".equalsIgnoreCase(type)) {
			clazz = Double.class;
		} else {
			clazz = Object.class;// POJO
		}

		Object targetData = object;// 缺省值

		// pojo类型
		if (clazz.equals(Object.class)) {
			try {
				Object dest = Class.forName(type);
				BeanUtils.copyProperties(dest, targetData);
				targetData = dest;
				return targetData;// pojo类型返回
			} catch (Exception e) {
				logger.error("ReqModel to Object Error:", e);
				throw new ApplicationException("err.request.InvalidFormat");
			}
		}

		// 基本类型
		if (targetData instanceof List) {
			// 创建新集合，避免同步修改
			List convertedData = new ArrayList();
			// 处理集合
			for (Object o : (List) targetData) {
				convertedData.add(ConvertUtils.convert(o, clazz));
			}
			targetData = convertedData;
		}else if (targetData instanceof Map) {
			// 创建新集合，避免同步修改
			List convertedData = new ArrayList();
			// 处理集合
			for (Object key : ((Map) targetData).keySet()) {
				convertedData.add(ConvertUtils.convert(((Map) targetData).get(key), clazz));
			}
			targetData = convertedData;
		} else {
			targetData = ConvertUtils.convert(targetData, clazz);
		}
		return targetData;// 基本类型返回
	}
}
