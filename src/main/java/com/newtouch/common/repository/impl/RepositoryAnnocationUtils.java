package com.newtouch.common.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.annotation.repository.Update;
import com.newtouch.common.model.QueryParams;
/**
 * repository注解工具，用于解析注解获取标准jpql，参数等
 * @author dongfeng.zhang
 */
public class RepositoryAnnocationUtils {

    static Pattern PARAM_PATTERN = Pattern.compile("(\\{[A-Za-z0-9_\\.]+?\\})");
    static Pattern COND_PATTERN = Pattern.compile("(/\\*(.+?)\\*/)");
    /**
     * 获取查询参数集合
     *
     * @param jpqlTemplate
     * @param params
     * @return
     */
    static QueryParams getParamMap(String jpqlTemplate, Map<String,Object> params) {
        QueryParams paramsMap = new QueryParams();
        Matcher PARAM_MATCHER = PARAM_PATTERN.matcher(jpqlTemplate);
        while (PARAM_MATCHER.find()) {
            String paramName = PARAM_MATCHER.group(1);
            String name = paramName.replaceAll("\\{", "").replaceAll("\\}", "");
            try {
            	if(name.startsWith("this.")){
            		 paramsMap.put(name, "{}");
            	}else{
	                Object value = PropertyUtils.getProperty(params, name);
	                paramsMap.put(name, value);
            	}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return paramsMap;
    }


    /**
     * 从jpqlTemplate计算获取jpql语句
     *
     * @param jpqlTemplate
     * @param paramMap
     *            参数列表
     * @return
     */
    static  String getJpql(String jpqlTemplate, Map<String, ?> paramMap) {
        String removedJpqlTemplate = fixEmptyCondition(jpqlTemplate, paramMap);
        return fixParam(removedJpqlTemplate, paramMap.keySet());
    }

    /**
     * 修复jpql语句中的动态条件,如果条件中包含了空值（null或""),摘除该条件
     *
     * @param jpqlTemplate
     *            jpql语句
     * @param paramMap
     *            参数名称列表
     * @return 修复后的jpql
     */
    static  String fixEmptyCondition(String jpqlTemplate, Map<String, ?> paramMap) {
        String fixedJpql = jpqlTemplate;
        Matcher COND_MATCHER = COND_PATTERN.matcher(jpqlTemplate);
        while (COND_MATCHER.find()) {
            String cond = COND_MATCHER.group(1);
            Matcher PARAM_MATCHER = PARAM_PATTERN.matcher(cond);
            while (PARAM_MATCHER.find()) {
                String paramName = PARAM_MATCHER.group(1);
                String name = paramName.replace(Update.NAME_PREFIX, "").replace(Update.NAME_SUFFIX, "");
                Object value = paramMap.get(name);
                // 非空对象和非空串
                if (value != null) {
                    fixedJpql = fixedJpql.replaceFirst("/\\*", "").replaceFirst("\\*/", "");
                } else {
                    fixedJpql = fixedJpql.replace(cond, "");
                    paramMap.remove(name);
                }
            }
        }
        return fixedJpql;
    }


    /**
     * 修复jpql语句中的条件参数为正确的参数表达方式
     *
     * @param jpqlTemplate
     *            jpql语句
     * @param paramNames
     *            参数名称列表
     * @return 修复后的jpql
     */
    static  String fixParam(String jpqlTemplate, Set<String> paramNames) {
        String fixedJpql = jpqlTemplate;
        for (String paramName : paramNames) {
            fixedJpql = fixedJpql.replace(Inquiry.NAME_PREFIX + paramName + Inquiry.NAME_SUFFIX, ":" + paramName);
            
        }
        return fixedJpql;
    }

    /**
     * 计算获取count jpql语句
     *
     * @param jpql
     * @return
     */
    static  String getCountJpql(String jpql) {
        int startIndex = jpql.toLowerCase().indexOf("from") + 4;
        int endIndex = jpql.toLowerCase().indexOf("order by");// 用于摘除排序
        if (startIndex < 6) {
            throw new RuntimeException("jpql string error!");
        }
        if (endIndex == -1) {
            endIndex = jpql.length();
        }
        String endJpqlString = jpql.substring(startIndex, endIndex);
        return "select count(*)  from " + endJpqlString;
    }

    /**
     * 获取jpql语句返回的字段列表
     *
     * @param jpql
     * @return
     */
    static  List<String> getReturnFieldNames(String jpql) {
        List<String> fieldNameList = new ArrayList<String>();

        int startIndex = jpql.toLowerCase().indexOf("select") + 6;
        int endIndex = jpql.toLowerCase().indexOf("from");
        if (startIndex < 6 || endIndex < startIndex + 4) {
            throw new RuntimeException("jpql string error!");
        }

        String fieldString = jpql.substring(startIndex, endIndex);
        String[] tempFieldNames = fieldString.split(",");

        for (int index = 0; index < tempFieldNames.length; index++) {// 组装属性列表
            String fieldName = tempFieldNames[index];
            int asIndex = fieldName.toLowerCase().indexOf(" as ");
            if (asIndex != -1) {
                fieldName = fieldName.substring(asIndex + 4);
            }

            fieldNameList.add(fieldName.trim());
        }

        return fieldNameList;
    }
}
