package com.newtouch.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.demo.model.Product;

/**
 * 增删改查演示服务
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/4
 */
public interface CurdService {
	/**
	 * 编辑Product
	 * 
	 * @param id
	 * @return
	 */
	Product get(Long id);

	/**
	 * 查询Product
	 * 
	 * @param product
	 * @param pageable
	 * @return
	 */
	Page<Product> page(QueryParams queryParams, Pageable pageable);

	/**
	 * 删除Product
	 * 
	 * @param productId
	 */
	void delete(Long id);

	/**
	 * 保存Product
	 */
	void create(Product product);
	/**
	 * 保存Product
	 */
	void patch(Product product);
	/**
	 * 查询页面 select数据
	 * 
	 * @return
	 */
	List<CodeCate> listCodeCate();

	/**
	 * 查询页面 select数据
	 * 
	 * @return
	 */
	List<CodeEntry> listCodeEntry(String codeCateCode);

}
