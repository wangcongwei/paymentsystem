/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-22 上午11:14:34 MjunLee
 */
package com.newtouch.payment.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.im.PaymentTransactionStatus;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.model.PaymentTransaction;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.repository.PaymentTransactionRepo;
import com.newtouch.payment.service.KuaiqianGatewayCallBackProcessService;
import com.newtouch.payment.service.SignatureService;


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
@Service("kuaiqianOnlineCallBackProcessService")
public class KuaiqianGatewayCallBackProcessServiceImpl implements KuaiqianGatewayCallBackProcessService {
	private static final Logger logger = LoggerFactory.getLogger(KuaiqianGatewayCallBackProcessServiceImpl.class);
	@Resource(name = "kuaiqianSignatureService")
	private SignatureService kuaiqianSignatureService;
	
	@Autowired
	private PaymentRepo paymentRepo;
	
	@Autowired
	private PaymentTransactionRepo paymentTransactionRepo;

	private String signMsgOrder = "merchantAcctId,version,language,signType,payType,bankId,orderId,orderTime,orderAmount,dealId,bankDealId,dealTime,payAmount,fee,ext1,ext2,payResult,errCode,key";

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveCallBackPayment(HttpServletRequest req) throws Exception {
		String ext = req.getParameter("ext2");
		JSONObject jo = JSONObject.parseObject(ext);
		String reqNo = (String) jo.get("reqNo");
		// 检查信息是否被串改
		StringBuffer checkMsgSB = new StringBuffer();
		for (String k : signMsgOrder.split(",")) {
			if (StringUtils.isBlank(req.getParameter(k)))
				continue;
			if (StringUtils.isNotBlank(checkMsgSB.toString())) {
				checkMsgSB.append("&").append(k).append("=").append(req.getParameter(k));
			} else {
				checkMsgSB.append(k).append("=").append(req.getParameter(k));
			}
		}
		logger.info("============快钱网关回调信息为：" + checkMsgSB.toString());
		String signMsg = req.getParameter("signMsg");

		if (StringUtils.isBlank(signMsg)) {
			logger.info("=========快钱网关支付通知，加密信息为空！");
			throw new Exception("加密信息为空！");
		}
		logger.info("============快钱网关回调加密串信息为：" + signMsg.toString());
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			if (!kuaiqianSignatureService.verifySign(checkMsgSB.toString().getBytes(), decoder.decodeBuffer(signMsg))) {
				logger.info("=========快钱网关支付通知，信息已被串改！");
				throw new Exception("信息已被串改！");
			}
		} catch (IOException e) {
			logger.info("BASE64Decoder失败", e);
			throw new Exception("信息已被串改！");
		}
		String paymentNo = req.getParameter("orderId");
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", paymentNo);
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		// 校验支付金额
		BigDecimal bd = BigDecimal.valueOf(payment.getPaymentAmount() * 100);
		if (!bd.setScale(0, BigDecimal.ROUND_HALF_UP).toString().equals(req.getParameter("payAmount"))) {
			logger.info("=========快钱网关支付通知，支付通知金额：" + req.getParameter("payAmount") + "分与支付申请金额：" + payment.getPaymentAmount() + "元不一致！");
			throw new Exception("支付通知金额：" + req.getParameter("payAmount") + "分与支付申请金额：" + payment.getPaymentAmount() + "元不一致！");
		}
		if (!PaymentStatus.PS_PAYING.equals(payment.getPaymentStatus()) && !PaymentStatus.PS_WAITPAY.equals(payment.getPaymentStatus()) && !PaymentStatus.PS_NOEFFITIVE.equals(payment.getPaymentStatus())) {
			logger.info("=========快钱网关支付通知，支付通知支付号：" + req.getParameter("orderId") + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
			throw new Exception("支付通知支付号：" + req.getParameter("orderId") + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
		}
		Date date = new Date();
		// 更新支付信息
		payment.setDealNo(req.getParameter("dealId"));// 快钱交易号
		Date dealTime = null;
		try {
			dealTime = DateUtils.parseDate(req.getParameter("dealTime"), new String[] {CommonConst.datePatternYYMMDDHHMMSS});
		} catch (Exception e) {
			logger.error("============快钱网关支付通知，快钱交易时间" + req.getParameter("dealTime") + "转换日期错误", e);
		}
		payment.setPayTime(dealTime);// 快钱交易时间,14位数字年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		payment.setPayAmount(Double.valueOf(req.getParameter("payAmount")) / 100);// 实际支付金额
		payment.setPaymentStatus(PaymentStatus.PS_SUCCESS);
		payment.setMerchantNo(req.getAttribute("merchantIdCCIC").toString());
		payment.setUpdateTime(new Date());
		queryParams.put("reqNo", reqNo);
		PaymentTransaction pt = paymentTransactionRepo.findOneByParam(PaymentTransaction.class, queryParams);// 扩展字段2，对应支付平台的交易记录交易号。
		if (pt != null) {
			pt.setStatus(PaymentTransactionStatus.SUCCESS);
			pt.setDealNo(payment.getDealNo());// 快钱交易号
			pt.setPayTime(payment.getPayTime());// 快钱交易时间
			pt.setPayAmount(payment.getPayAmount());// 实际支付金额
			paymentTransactionRepo.update(pt);
		}
		paymentRepo.update(payment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveOnlineCallBackPayment(HttpServletRequest req) throws Exception {

		String ext = req.getParameter("ext2");
		JSONObject jo = JSONObject.fromObject(ext);
		String transNo = (String) jo.get("transNo");
			// 检查信息是否被串改
			StringBuffer checkMsgSB = new StringBuffer();
			for (String k : signMsgOrder.split(",")) {
				if (StringUtils.isBlank(req.getParameter(k)))
					continue;
				if (StringUtils.isNotBlank(checkMsgSB.toString())) {
					checkMsgSB.append("&").append(k).append("=").append(req.getParameter(k));
				} else {
					checkMsgSB.append(k).append("=").append(req.getParameter(k));
				}
			}
			log.info("============快钱网关回调信息为：" + checkMsgSB.toString());
			String signMsg = req.getParameter("signMsg");

			if (StringUtils.isBlank(signMsg)) {
				log.info("=========快钱网关支付通知，加密信息为空！");
				throw new CallBackBussinessException("加密信息为空！");
			}
			log.info("============快钱网关回调加密串信息为：" + signMsg.toString());
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			try {
				if (!kuaiqianSignatureService.verifySign(checkMsgSB.toString().getBytes(), decoder.decodeBuffer(signMsg))) {
					log.info("=========快钱网关支付通知，信息已被串改！");
					throw new CallBackBussinessException("信息已被串改！");
				}
			} catch (IOException e) {
				log.info("BASE64Decoder失败", e);
				throw new CallBackBussinessException("信息已被串改！");
			}
//		}
		Payment pm = pa.getPayment();
		// 校验支付金额
		BigDecimal bd = BigDecimal.valueOf(pm.getPaymentAmount() * 100);
		if (!bd.setScale(0, BigDecimal.ROUND_HALF_UP).toString().equals(req.getParameter("payAmount"))) {
			log.info("=========快钱网关支付通知，支付通知金额：" + req.getParameter("payAmount") + "分与支付申请金额：" + pm.getPaymentAmount() + "元不一致！");
			throw new CallBackBussinessException("支付通知金额：" + req.getParameter("payAmount") + "分与支付申请金额：" + pm.getPaymentAmount() + "元不一致！");
		}
		if (!OrderStatus.PAYING.equals(pm.getPaymentStatus()) && !OrderStatus.CREATENOTPAY.equals(pm.getPaymentStatus()) && !OrderStatus.INVALID.equals(pm.getPaymentStatus())) {
			log.info("=========快钱网关支付通知，支付通知支付号：" + req.getParameter("orderId") + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
			throw new CallBackBussinessException("支付通知支付号：" + req.getParameter("orderId") + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
		}
		Date date = new Date();
		// 更新支付信息
		pm.setDealNo(req.getParameter("dealId"));// 快钱交易号
		Date dealTime = null;
		try {
			dealTime = DateUtil.convertStringToDate("yyyyMMddHHmmss", req.getParameter("dealTime"));
		} catch (Exception e) {
			log.error("============快钱网关支付通知，快钱交易时间" + req.getParameter("dealTime") + "转换日期错误", e);
		}
		pm.setDealTime(dealTime);// 快钱交易时间,14位数字年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		pm.setPayAmount(Double.valueOf(req.getParameter("payAmount")) / 100);// 实际支付金额
		pm.setBankDealNo(req.getParameter("bankDealId"));// 银行交易号
		pm.setPaymentStatus(OrderStatus.PAYSUCCESS);
		pm.setPaySuccessDate(date);
		if (pa.getShPaymet() != true) {
			pm.setLeafMerchantId((String) req.getParameter("merchantAcctId").trim());
			pm.setBankName("");// 银行名称
		} else {
			pm.setLeafMerchantId(req.getAttribute("merchantIdCCIC").toString().trim());
			pm.setBankName(req.getParameter("issuer"));// 银行名称

		}
		pm.setUpdateTime(new Date());
		PaymentTransaction pt = payNoService.findPayTransByTransNo(transNo);// 扩展字段2，对应支付平台的交易记录交易号。
		if (pt != null) {
			pt.setTransStatus(TransStatus.SUCCESS);
			pt.setDealNo(pm.getDealNo());// 快钱交易号
			pt.setDealTime(pm.getDealTime());// 快钱交易时间
			pt.setPayAmount(pm.getPayAmount());// 实际支付金额
			baseRepository.update(pt);
			pm.setPayType(pt.getPayType());
			pm.setPlatform(pt.getPlatform());

		}
		baseRepository.update(pm);
	}
}
