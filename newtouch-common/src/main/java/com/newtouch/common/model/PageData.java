package com.newtouch.common.model;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/23
 */
public class PageData<T> extends org.springframework.data.domain.PageImpl<T> {

	private static final long serialVersionUID = -2863200445234949158L;

	/**
     * 模糊计算行数
     *
     * @param content   数据
     * @param pageable  分页
     * @param totalPage 总页数
     */
    public PageData(List<T> content, Pageable pageable, int totalPage) {
        super(content, pageable, totalPage * pageable.getPageSize());//总条数不精确
    }

    /**
     * 重载构造器
     *
     * @param content  数据
     * @param pageable 分页
     * @param total    总行数
     */
    public PageData(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    /**
     * 重载构造器
     *
     * @param content 数据
     * @param page    页码
     * @param size    页大小
     * @param total   总页数
     */
    public PageData(List<T> content, int page, int size, long total) {
        super(content, new PageRequest(page, size), total);
    }

    /**
     * 重载构造器
     *
     * @param content 数据
     */
    public PageData(List<T> content) {
        super(content);
    }
}
