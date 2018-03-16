/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-21 上午10:00:41 MjunLee
 */
package com.newtouch.payment.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.model.Merchant;
import com.newtouch.payment.repository.MerchantRepo;
import com.newtouch.payment.service.PayCallBackProxyService;
import com.newtouch.payment.service.PayCallBackService;
import com.newtouch.payment.service.kuaiqian.support.send.InitTrans;
import com.newtouch.payment.service.kuaiqian.support.util.ParseUtil;
import com.newtouch.payment.utils.StreamUtil;


/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/13
 */
@Service("payCallBackProxyService")
public class PayCallBackProxyServiceImpl implements PayCallBackProxyService {
	private final Log log = LogFactory.getLog(this.getClass());
	@Resource(name = "payCallBackServiceContainer")
	private Map<String, PayCallBackService> payCallBackServiceContainer;
	
	@Autowired
	private MerchantRepo merchantRepo;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String merchantId = "";
		if (StringUtils.isNotBlank(req.getParameter("merchantAcctId"))) {
			// 快钱网关的商户号
			merchantId = req.getParameter("merchantAcctId");
//			merchantId = merchantId.substring(0, merchantId.length() - 2);
		} else if (StringUtils.isNotBlank(req.getParameter("merchantId"))) {
			// 快钱pos、银联pos
			merchantId = req.getParameter("merchantId");
		} else if (StringUtils.isNotBlank(req.getParameter("seller_id"))) {
			// 支付宝的商户号
			merchantId = req.getParameter("seller_id");
		}else {
			// 快钱无卡的商户号
			req.setCharacterEncoding("UTF-8");
			String receiveXml = null;
			receiveXml = StreamUtil.changeISToStr(req.getInputStream());
			@SuppressWarnings("unchecked")
			Map<String, String> payInfo = ParseUtil.parseXML(receiveXml, new InitTrans("PUR").getTransInfo());
			if (payInfo == null || payInfo.get("merchantId") == null) {
				log.error("=============商户号为空，拒绝回调请求。");
				throw new ServiceException("商户号为空，拒绝回调请求!");
			}
			merchantId = payInfo.get("merchantId");
			req.setAttribute("_receiveXml_", receiveXml);
		}
		req.setAttribute("merchantId", merchantId);
		// 根据商户号判断是哪个平台的回调以及支付方式
		// ********设置支付的方式，快钱平台会根据这个值判断为网关、无卡还是POS***********
		QueryParams queryParams = new QueryParams();
		queryParams.put(CommonConst.MERCHANTID, merchantId);
		Merchant merchant = merchantRepo.findOneByParam(Merchant.class, queryParams);
		if (merchant == null) {
			log.error("=============商户号:" + merchantId + "不存在!");
			throw new ServiceException("商户号:" + merchantId + "不存在!");
		}
		req.setAttribute("payType", merchant.getPayTypeCode());
		// ********设置支付的方式，快钱平台会根据这个值判断为网关、无卡还是POS***********
		PayCallBackService payCallBackService = payCallBackServiceContainer.get(merchant.getPlatformCode());
		if (payCallBackService == null) {
			log.error("=============商户号:" + merchantId + "对应的平台不存在!");
			throw new ServiceException("商户号:" + merchantId + "对应的平台不存在!");
		}
		payCallBackService.callBackPayment(req, resp);
	}

}
