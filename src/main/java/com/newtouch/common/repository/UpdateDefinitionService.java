package com.newtouch.common.repository;

import java.util.List;
import java.util.Map;

import com.newtouch.common.annotation.repository.Updates;
import com.newtouch.common.model.QueryDefinition;

/**
 * jpql更新注解工具类
 *
 * @author dongfeng.zhang
 */
public interface UpdateDefinitionService {

    /**
     * 获取查询语句数组注解
     *
     * @param params
     * @param Updates  查询对象
     * @return 查询语句数组注解
     */
    public List<QueryDefinition> getUpdateDefinnetions(Map<String,Object>  params, Updates updates);
}

