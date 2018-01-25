package com.newtouch.common.repository;

import java.util.List;
import java.util.Map;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.model.QueryDefinition;

/**
 * jpql查询注解工具类
 *
 * @author dongfeng.zhang
 */
public interface QueryDefinitionService {

    /**
     * 获取查询语句数组注解
     *
     * @param params
     * @param inquiries  查询对象
     * @return 查询语句数组注解
     */
    public List<QueryDefinition> getQueryDefinnetions(Map<String,Object>  params, Inquiries inquiries);
}

