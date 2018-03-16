package com.newtouch.payment.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.service.KuaiqianPayCallBackService;
import com.newtouch.payment.service.PayCallBackService;


@Service("kuaiqianPlatformPayCallBackService")
public class KuaiqianPlatformPayCallBackServiceImpl implements PayCallBackService {
	@Resource(name = "kuaiqianCallBackContainer")
	private Map<String, KuaiqianPayCallBackService> kuaiqianCallBackContainer;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		KuaiqianPayCallBackService kuaiqianPayCallBackService = kuaiqianCallBackContainer.get(req.getAttribute("payType"));
		if (kuaiqianPayCallBackService == null) {
			throw new ServiceException("快钱平台商户号：" + req.getAttribute("merchantId") + "对应的支付方式:" + req.getAttribute("payType") + "不存在！");
		}
		kuaiqianPayCallBackService.callBackPayment(req, resp);
	}
}
