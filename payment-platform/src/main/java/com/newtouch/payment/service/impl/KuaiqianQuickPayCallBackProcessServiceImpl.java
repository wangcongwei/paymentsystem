
package com.newtouch.payment.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.im.PayType;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.im.PaymentTransactionStatus;
import com.newtouch.payment.im.Platform;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.model.PaymentTransaction;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.repository.PaymentTransactionRepo;
import com.newtouch.payment.service.KuaiqianQuickPayCallBackProcessService;


/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/12
 */
@Service("kuaiqianNoCardCallBackProcessService")
public class KuaiqianQuickPayCallBackProcessServiceImpl implements KuaiqianQuickPayCallBackProcessService {
	private final static Logger logger = LoggerFactory.getLogger(KuaiqianQuickPayCallBackProcessServiceImpl.class);
	
	@Autowired
	private PaymentRepo paymentRepo; 
	
	@Autowired
	private PaymentTransactionRepo paymentTransactionRepo;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void saveCallBackPayment(Map<String, String> payInfo) throws Exception {
		
		String paymentNo = payInfo.get(CommonConst.PAYMENTNO);
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", paymentNo);
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		// 更新支付信息
		payment.setDealNo(payInfo.get("refNumber"));// 快钱交易号
		Date dealTime = null;
		try {
			dealTime = DateUtils.parseDate(payInfo.get("transTime"), new String[] {CommonConst.datePatternYYMMDDHHMMSS});
		} catch (Exception e) {
			logger.error("============快钱无卡支付通知，快钱交易时间" + payInfo.get("transTime") + "转换日期错误", e);
		}
		payment.setPayTime(dealTime);// 快钱交易时间,14位数字年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		payment.setPayAmount(Double.valueOf(payInfo.get("amount")));// 实际支付金额
		payment.setPaymentStatus(PaymentStatus.PS_SUCCESS);
		payment.setPayType(PayType.QUICK);// 支付方式
		payment.setPlatform(Platform.KUAIQIAN);// 平台
		payment.setMerchantNo(payInfo.get("merchantId"));// 子商户号
		// 无卡相关信息
		payment.setUpdateTime(new Date());
		queryParams.put(CommonConst.REQNO, paymentNo);
		PaymentTransaction pt = paymentTransactionRepo.findOneByParam(PaymentTransaction.class, queryParams);
		if (pt != null) {
			pt.setStatus(PaymentTransactionStatus.SUCCESS);
			pt.setDealNo(payment.getDealNo());// 快钱交易号
			pt.setPayTime(payment.getPayTime());// 快钱交易时间
			pt.setPayAmount(payment.getPayAmount());// 实际支付金额
			// 无卡支付信息
			pt.setCardNo(payInfo.get("storableCardNo"));// 卡号前6和后4位
			pt.setBankName(payInfo.get("issuer"));// 发卡银行名称
			pt.setBankCode(payInfo.get("cardOrg"));// 卡组织编号
			paymentTransactionRepo.update(pt);
		}
		paymentRepo.update(payment);
	}

}
