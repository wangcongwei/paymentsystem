
package com.newtouch.payment.service.impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.service.KuaiqianGatewayCallBackProcessService;
import com.newtouch.payment.service.KuaiqianPayCallBackService;


/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/12
 */
@Service("kuaiqianGatewayPayCallBackService")
public class KuaiqianGatewayPayCallBackServiceImpl implements KuaiqianPayCallBackService {
	private static final Logger logger = LoggerFactory.getLogger(KuaiqianGatewayPayCallBackServiceImpl.class);
	@Resource
	private KuaiqianGatewayCallBackProcessService kuaiqianGatewayCallBackProcessService;
	
	@Autowired
	private PaymentRepo paymentRepo;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String ext2 = req.getParameter("ext2");
		String paymentNo = StringUtils.isNotBlank(req.getParameter("orderId")) ? req.getParameter("orderId") : "";
		JSONObject jo = JSONObject.parseObject(ext2);
		String reqNo = (String) jo.get(CommonConst.REQNO);
		QueryParams queryParams = new QueryParams();
		queryParams.put(CommonConst.PAYMENTNO, paymentNo);
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		try {
			if (!"10".equals(req.getParameter("payResult"))) {
				logger.info("=========快钱网关支付通知，通知状态：" + req.getParameter("payResult") + "非成功状态！");
				throw new ServiceException("通知状态：" + req.getParameter("payResult") + "非成功状态！");
			}
			// 获取支付信息
			if (payment == null) {
				logger.info("=========快钱网关支付通知，支付号：" + req.getParameter("orderId") + "无对应信息！");
				throw new ServiceException("支付号：" + req.getParameter("orderId") + "无对应信息！");
			}
		} catch (ServiceException e) {
			logger.info("=====网关回调通知信息校验失败", e);
			String processResult = "<result>1</result><redirecturl></redirecturl>";
			resp.getWriter().write(processResult);
			return;
		}

		String reUrl = "";
		String cooSystemUrl = "";// 要返回的合作系统url
		String ext = "";

		try {
			kuaiqianGatewayCallBackProcessService.saveOnlineCallBackPayment(req);
			String processResult = "<result>1</result><redirecturl>" + reUrl + "?msg=success&ext=" + ext + "&paymentNo=" + paymentNo + "&url=" + cooSystemUrl
					+ "</redirecturl>";
			logger.info("支付回调返回给快钱信息为: " + processResult);
			resp.getWriter().write(processResult);
		} catch (Exception e) {
			logger.info("=====网关回调处理异常2", e);
			// 如果处理回调结果发生非业务校验等异常，返回快钱失败,重复再送此通知。
			String processResult = "<result>0</result><redirecturl>" + reUrl + "?msg=error&ext=" + ext + "&paymentNo=" + paymentNo + "&url=" + cooSystemUrl
					+ "</redirecturl>";
			logger.info("支付回调返回给快钱信息为: " + processResult);
			resp.getWriter().write(processResult);
			return;
		}
	}
}
