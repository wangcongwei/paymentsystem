package com.newtouch.common.view;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.newtouch.common.service.core.CodeQueryService;

/**
 * 可以过滤null的bean copy
 * 
 * @author c_zhangdongfeng
 *
 */
public class PropertyUtils  extends org.apache.commons.beanutils.BeanUtils{
	private static PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
	private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
	private static CodeQueryService codeQueryService;
	private static ApplicationContext springContext;
	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void copyPropertiesSkipNull(Object dest, Object orig) {
		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		try {
			// Copy the properties, converting as necessary
			if (orig instanceof DynaBean) {
				DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass().getDynaProperties();
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					// Need to check isReadable() for WrapDynaBean
					// (see Jira issue# BEANUTILS-61)
					if (propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)) {
						Object value = ((DynaBean) orig).get(name);
						if (value != null) {
							copyProperty(dest, name, value);
						}
					}
				}
			} else if (orig instanceof Map) {
				@SuppressWarnings("unchecked")
				// Map properties are always of type <String, Object>
				Map<String, Object> propMap = (Map<String, Object>) orig;
				for (Map.Entry<String, Object> entry : propMap.entrySet()) {
					String name = entry.getKey();
					if (propertyUtilsBean.isWriteable(dest, name)) {
						Object value = entry.getValue();
						if (value != null) {
							copyProperty(dest, name, value);
						}
					}
				}
			} else /* if (orig is a standard JavaBean) */{
				PropertyDescriptor[] origDescriptors = propertyUtilsBean.getPropertyDescriptors(orig);
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					if ("class".equals(name)) {
						continue; // No point in trying to set an object's class
					}
					if (propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)) {
						try {
							Object value = propertyUtilsBean.getSimpleProperty(orig, name);
							if (value != null) {
								copyProperty(dest, name, value);
							}
						} catch (NoSuchMethodException e) {
							// Should not happen
						}
					}
				}
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static ApplicationContext getSpringContext() {
		return springContext;
	}
	
	public static <T> T getBeanByType(Class<T> type) {
		return (T) springContext.getBean(type);
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		springContext = applicationContext;
		codeQueryService = getBeanByType(CodeQueryService.class);
	}
}
