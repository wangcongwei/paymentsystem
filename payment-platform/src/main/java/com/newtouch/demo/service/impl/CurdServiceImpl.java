package com.newtouch.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.common.view.PropertyUtils;
import com.newtouch.demo.model.Product;
import com.newtouch.demo.repository.CurdRepo;
import com.newtouch.demo.service.CurdService;

/**
 * 增删改查演示服务
 * 
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/4
 */
@Service
public class CurdServiceImpl implements CurdService {
	private static Logger logger = LoggerFactory
			.getLogger(CurdServiceImpl.class);

	@Autowired
	private CurdRepo repo;

	public Product get(Long id) {
		return repo.getById(Product.class, id);
	}

	public Page<Product> page(QueryParams queryParams, Pageable pageable) {
		return repo.pageProduct(Product.class, queryParams, pageable);
	}

	public void delete(Long id) {
		logger.trace("logic delete product id:{}", id);
		repo.deleteById(Product.class, id);
	}

	public void create(Product product) {
		logger.trace("insert product={}", product);
		repo.save(product);
	}

	public void patch(Product product) {
		Product existProduct = this.get(product.getId());
		if (existProduct == null) {
			logger.error("not exist product={}", product);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existProduct, product);
			logger.trace("update product={}", product);
			repo.update(product);
		}
	}

	// 演示用模拟数据
	public List<CodeCate> listCodeCate() {
		List<CodeCate> codeCatelist = new ArrayList<CodeCate>();
		for (int i = 1; i <= 20; i++) {
			CodeCate codeCate = new CodeCate();
			codeCate.setId(1002L + i);
			codeCate.setCode("0032" + i);
			codeCate.setName("分类XA" + i);
			codeCatelist.add(codeCate);
		}
		return codeCatelist;
	}

	public List<CodeEntry> listCodeEntry(String codeCateCode) {
		List<CodeEntry> codeEntrylist = new ArrayList<CodeEntry>();
		for (int i = 1; i <= 20; i++) {
			CodeEntry codeEntry = new CodeEntry();
			codeEntry.setId(1002L + i);
			codeEntry.setCode(codeCateCode + "0032" + i);
			codeEntry.setName(codeCateCode + "分类XA" + i);
			codeEntrylist.add(codeEntry);
		}
		return codeEntrylist;
	}

}
