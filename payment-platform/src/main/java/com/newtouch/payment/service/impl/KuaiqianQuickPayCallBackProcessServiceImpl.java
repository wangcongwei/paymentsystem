/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-25 下午2:12:24 MjunLee
 */
package com.newtouch.payment.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ccic.eb.constant.payment.OrderStatus;
import com.ccic.eb.constant.payment.Paytype;
import com.ccic.eb.constant.payment.PlatForm;
import com.ccic.eb.constant.payment.TransStatus;
import com.ccic.eb.model.payment.Payment;
import com.ccic.eb.model.payment.PaymentApplication;
import com.ccic.eb.model.payment.transaction.PaymentTransaction;
import com.ccic.eb.service.onlinepayment.PayNoService;
import com.ccic.eb.service.paycallback.exception.CallBackBussinessException;
import com.ccic.eb.service.paycallback.kuaiqianpaycallback.KuaiqianNoCardCallBackProcessService;
import com.newtouch.repository.BaseRepository;
import com.newtouch.util.DateUtil;

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
@Service("kuaiqianNoCardCallBackProcessService")
public class KuaiqianQuickPayCallBackProcessServiceImpl implements KuaiqianNoCardCallBackProcessService {
	private final static Log log = LogFactory.getLog(KuaiqianQuickPayCallBackProcessServiceImpl.class);
	@Resource(name = "payNoService")
	private PayNoService payNoService;
	@Resource(name = "baseRepository")
	private BaseRepository baseRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveCallBackPayment(Map<String, String> payInfo, PaymentApplication pa) throws CallBackBussinessException {
		Payment pm = pa.getPayment();
		Date date = new Date();
		// 更新支付信息
		pm.setDealNo(payInfo.get("refNumber"));// 快钱交易号
		Date dealTime = null;
		try {
			dealTime = DateUtil.convertStringToDate("yyyyMMddHHmmss", payInfo.get("transTime"));
		} catch (Exception e) {
			log.error("============快钱无卡支付通知，快钱交易时间" + payInfo.get("transTime") + "转换日期错误", e);
		}
		pm.setDealTime(dealTime);// 快钱交易时间,14位数字年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		pm.setPaymentTime(dealTime);// 支付申请时间
		pm.setPayAmount(Double.valueOf(payInfo.get("amount")));// 实际支付金额
		pm.setBankDealNo(null);// 银行交易号
		pm.setPaymentStatus(OrderStatus.PAYSUCCESS);
		pm.setPaySuccessDate(date);
		pm.setPayType(Paytype.NOCARD_PAY);// 支付方式
		pm.setPlatform(PlatForm.KUAIQIAN);// 平台
		pm.setLeafMerchantId(payInfo.get("merchantId"));// 子商户号
		pm.setTerminalId(payInfo.get("terminalId"));// 终端号
		pm.setBankCardNumber(payInfo.get("storableCardNo"));// 银行卡号
		// 无卡相关信息
		pm.setBankName(payInfo.get("issuer"));// 银行名称
		pm.setUpdateTime(new Date());
		Date postDate = null;
		try {
			postDate = DateUtil.convertStringToDate("yyyyMMddHHmmss", payInfo.get("entryTime"));
		} catch (Exception e) {
			log.error("============快钱无卡支付通知，交易记录时间" + payInfo.get("entryTime") + "转换日期错误", e);
		}
		PaymentTransaction pt = payNoService.findPayTransByPayNoAndDate(pm.getPaymentNo(), postDate);
		if (pt != null) {
			pt.setTransStatus(TransStatus.SUCCESS);
			pt.setDealNo(pm.getDealNo());// 快钱交易号
			pt.setDealTime(pm.getDealTime());// 快钱交易时间
			pt.setPayAmount(pm.getPayAmount());// 实际支付金额
			// 无卡支付信息
			pt.setStorableCardNo(payInfo.get("storableCardNo"));// 卡号前6和后4位
			pt.setAuthorizationCode(payInfo.get("authorizationCode"));// 发卡机构给予被批准交易的授权号
			pt.setIssuer(payInfo.get("issuer"));// 发卡银行名称
			pt.setCardOrg(payInfo.get("cardOrg"));// 卡组织编号
			baseRepository.update(pt);
		}
		baseRepository.update(pm);
	}

}
