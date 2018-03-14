/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-19 下午2:24:05 MjunLee
 */
package com.newtouch.payment.service.impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.newtouch.payment.service.KuaiqianPayCallBackService;


/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: NewTouch
 * </p>
 * 
 * @author MjunLee
 * @version 1.0
 */
@Service("kuaiqianOnlinePayCallBackService")
public class KuaiqianGatewayPayCallBackServiceImpl implements KuaiqianPayCallBackService {
	private final static Log log = LogFactory.getLog(KuaiqianGatewayPayCallBackServiceImpl.class);
	@Resource
	private KuaiqianOnlineCallBackProcessService kuaiqianOnlineCallBackProcessService;
	@Resource(name = "notifyPayResultService")
	private NotifyPayResultService notifyPayResultService;
	@Resource(name = "payNoService")
	private PayNoService payNoService;
	@Resource
	private TransErrorService transErrorService;
	@Resource(name = "payConsumerAuthCtlService")
	private PayConsumerAuthCtlService payConsumerAuthCtlService;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PaymentApplication pa;
		String ext2 = req.getParameter("ext2");
		String paymentNo = StringUtils.isNotBlank(req.getParameter("orderId")) ? req.getParameter("orderId") : "";
		JSONObject jo = JSONObject.fromObject(ext2);
		String transNo = (String) jo.get("transNo");
		String consumerID = (String) jo.get("payConsurmer");
		String ext = StringUtils.isNotBlank((String) jo.get("ext")) ? (String) jo.get("ext") : "";
		String systemSource = (String) jo.get("ssCode");
		try {
			if (!"10".equals(req.getParameter("payResult"))) {
				log.info("=========快钱网关支付通知，通知状态：" + req.getParameter("payResult") + "非成功状态！");
				transErrorService.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, "快钱网关支付通知，通知状态：" + req.getParameter("payResult")
						+ "非成功状态！");
				throw new CallBackBussinessException("通知状态：" + req.getParameter("payResult") + "非成功状态！");
			}
			// 获取支付信息
			pa = payNoService.findApplicationByPayNo(req.getParameter("orderId"));
			if (pa == null) {
				log.info("=========快钱网关支付通知，支付号：" + req.getParameter("orderId") + "无对应信息！");
				transErrorService
						.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, "快钱网关支付通知，支付号：" + req.getParameter("orderId") + "无对应信息！");
				throw new CallBackBussinessException("支付号：" + req.getParameter("orderId") + "无对应信息！");
			}
		} catch (CallBackBussinessException e) {
			log.info("=====网关回调通知信息校验失败", e);
			String processResult = "<result>1</result><redirecturl></redirecturl>";
			resp.getWriter().write(processResult);
			return;
		}

		String reUrl = "";
		String cooSystemUrl = "";// 要返回的合作系统url

		log.info("get systemSource's value is : " + systemSource + "get ext's value is" + ext + "get paymentNo's value is :" + paymentNo);

		log.info("服务消费方为===============================================" + consumerID);

		if (StringUtils.isNotBlank(consumerID)) {
			// 判断consumerID是否是网销，是网销就根据服务消费方获得成功页面，如果不是网销，就调到指定成功页面
			if (!"ebiz".equals(consumerID)) {
				reUrl = Constant.appResource.getString("successPage");
				cooSystemUrl = payConsumerAuthCtlService.getSuccessPage(consumerID, systemSource);
			} else {
				reUrl = payConsumerAuthCtlService.getSuccessPage(consumerID, systemSource);
			}
		} else {
			reUrl = Constant.appResource.getString("successPage");
		}
		
		if(StringUtils.isBlank(reUrl)){
			reUrl = Constant.appResource.getString("successPage");
		}

		if (!"ebiz".equals(consumerID)) {// 如果为非网销
			try {
				kuaiqianOnlineCallBackProcessService.saveOnlineCallBackPayment(req, pa);
				String processResult = "<result>1</result><redirecturl>" + reUrl + "?msg=success&ext=" + ext + "&paymentNo=" + paymentNo + "&url=" + cooSystemUrl
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
			} catch (CallBackBussinessException e) {
				log.info("=====网关回调处理异常1", e);
				// 如果处理回调结果发生业务校验等异常，返回快钱成功，不用再送此通知。支付结果也没标识为失败。
				transErrorService.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, e.getMessage());
				String processResult = "<result>1</result><redirecturl>" + reUrl + "?msg=error&ext=" + ext + "&paymentNo=" + paymentNo + "&url=" + cooSystemUrl
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
				return;
			} catch (Exception e) {
				log.info("=====网关回调处理异常2", e);
				// 如果处理回调结果发生非业务校验等异常，返回快钱失败,重复再送此通知。
				transErrorService.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, e.getMessage());
				String processResult = "<result>0</result><redirecturl>" + reUrl + "?msg=error&ext=" + ext + "&paymentNo=" + paymentNo + "&url=" + cooSystemUrl
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
				return;
			}
		} else {
			try {
				log.info("========网关回调处理成功");
				kuaiqianOnlineCallBackProcessService.saveOnlineCallBackPayment(req, pa);
				String processResult = "<result>1</result><redirecturl>" + reUrl + "?msg=success&systemSource=" + systemSource + "&ext=" + ext + "&paymentNo=" + paymentNo
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
			} catch (CallBackBussinessException e) {
				log.info("=====网关回调处理异常1", e);
				// 如果处理回调结果发生业务校验等异常，返回快钱成功，不用再送此通知。支付结果也没标识为失败。
				transErrorService.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, e.getMessage());
				String processResult = "<result>1</result><redirecturl>" + reUrl + "?msg=error&systemSource=" + systemSource + "&ext=" + ext + "&paymentNo=" + paymentNo
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
				return;
			} catch (Exception e) {
				log.info("=====网关回调处理异常2", e);
				// 如果处理回调结果发生非业务校验等异常，返回快钱失败,重复再送此通知。
				transErrorService.createTransError("KUAIQIAN", req.getParameter("orderId"), transNo, TransType.ONLINE_PAY, e.getMessage());
				String processResult = "<result>0</result><redirecturl>" + reUrl + "?msg=error&systemSource=" + systemSource + "&ext=" + ext + "&paymentNo=" + paymentNo
						+ "</redirecturl>";
				log.info("支付回调返回给快钱信息为: " + processResult);
				resp.getWriter().write(processResult);
				return;
			}
		}

		try {
			// 通知收付费回调结果
			notifyPayResultService.sendPayResultToPayment(pa.getPayApplyNo());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		try {
			// 通知合作网站支付成功结果
			if (SponsorSystem.WANGXIAO.equals(pa.getPayment().getSource())) {
				notifyPayResultService.sendPayResultToCoop(pa.getPayApplyNo());
			}
		} catch (Exception e1) {
			log.error(e1.getMessage(),e1);
		}
	}
}
