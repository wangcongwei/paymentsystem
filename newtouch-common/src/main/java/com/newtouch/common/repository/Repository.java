package com.newtouch.common.repository;

import com.newtouch.common.model.QueryParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


/**
 * JPA数据访问接口
 *
 * @author dongfeng.zhang
 */
public interface Repository {

    /**
     * 保存实体
     *
     * @param entity 实体id
     */
    <T> void save(T entity);

    /**
     * 更新实体
     *
     * @param entity 实体
     */
    <T> void update(T entity);

    /**
     * 获取实体
     *
     * @param entityId 实体id
     * @return
     */
    <T> T getById(Class<T> entityClass, Object entityId);


    /**
     * 删除实体
     *
     * @param id 实体id
     */
    <T> void deleteById(Class<T> entityClass, Object id);

    /**
     * 删除实体
     *
     * @param id 实体id
     */
    <T> void logicDeleteById(Class<T> entityClass, Object id);


    /**
     * 根据查询对象
     *
     * @param queryParam 查询条件
     * @return 查询结果对象
     */
    public <T> T findOneByParam(Class<T> entityClass, QueryParams queryParam);

    /**
     * 返回所有记录
     *
     * @return 查询结果（限制返回行数）
     */
    public <T> List<T> findAll(Class<T> entityClass);

    /**
     * 根据查询对象
     *
     * @return 查询结果（限制返回行数）
     */
    public <T> List<T> findByParam(Class<T> entityClass, QueryParams queryParam);

    /**
     * 根据查询对象
     *
     * @param sort 排序
     * @return 查询结果（限制返回行数）
     */
    public <T> List<T> findByParamAndSort(Class<T> entityClass, QueryParams queryParam, Sort sort);

    /**
     * 根据查询对象，查询语句模板查询分页对象
     *
     * @param pageable 分页请求
     * @return 查询结果对象分页
     */
    public <T> Page<T> pageByParam(Class<T> entityClass, QueryParams queryParam, Pageable pageable);


}
