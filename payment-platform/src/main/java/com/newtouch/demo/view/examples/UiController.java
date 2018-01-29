package com.newtouch.demo.view.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.demo.model.Product;
import com.newtouch.demo.service.CurdService;

/**
 * ui演示例子
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/1
 */

@RequestMapping(value = "/examples/ui")
@Controller
public class UiController{
	private static Logger logger = LoggerFactory.getLogger(UiController.class);
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
		
		QueryParams queryParams = reqModel.getQueryParams();
		logger.trace("queryParams={}", queryParams);
		Pageable pageable=new PageRequest(0,10);
		Page<Product> productPage = curdService.page(queryParams, pageable);
		return new RespModel(productPage.getContent());
	}
}
