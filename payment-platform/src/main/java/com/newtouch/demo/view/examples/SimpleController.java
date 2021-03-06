package com.newtouch.demo.view.examples;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.demo.model.Product;
import com.newtouch.demo.service.CurdService;

/**
 * 单个页面的例子
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/1
 */

@RequestMapping(value = "/examples/simple")
@Controller
public class SimpleController{
	private static Logger logger = LoggerFactory.getLogger(SimpleController.class);
	@Autowired
	private CurdService curdService;

	/**
	 * ajax 列表查询 查询
	 * 
	 * @param reqModel
	 *            请求模型
	 * @return json数据
	 */
	@ResponseBody
	@RequestMapping(value = "/list", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public RespModel page(@RequestBody ReqModel reqModel) {
		Pageable pageable = reqModel.getPageable();
		QueryParams queryParams = reqModel.getQueryParams();
		logger.trace("pagePageNumber={}", pageable.getPageNumber());
		logger.trace("queryParams={}", queryParams);
		Page<Product> productPage = curdService.page(queryParams, pageable);
		return new RespModel(productPage);
	}

	/**
	 * form表单初始化数据
	 * 
	 * @return json数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list/code-cate", produces = "application/json", method = RequestMethod.POST)
	public RespModel listCodeCate() {
		// 获取所有商品
		List<CodeCate> codeCateList = curdService.listCodeCate();
		RespModel model = new RespModel();
		model.addAdditional("codeCate", codeCateList);
		return model;
	}

	/**
	 * 级联下拉列表例子
	 * 
	 * @param reqModel
	 *            请求模型
	 * @return json数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list/code-entry", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public RespModel listCodeEntry(@RequestBody ReqModel reqModel) {
		String codeCateCode = (String) reqModel.getContent();
		RespModel model = new RespModel();
		List<CodeEntry> codeEntryList = curdService.listCodeEntry(codeCateCode);
		model.addAdditional("codeEntry", codeEntryList);
		return model;
	}
}
