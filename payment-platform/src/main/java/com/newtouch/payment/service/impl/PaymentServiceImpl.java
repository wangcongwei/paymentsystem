package com.newtouch.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.service.PaymentService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	
private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	private PaymentRepo paymentRepo;

	@Override
	public Payment get(Long id) {
		return paymentRepo.getById(Payment.class, id);
	}

	@Override
	public void delete(Long id) {
		logger.trace("logic delete Payment id:{}", id);
		paymentRepo.deleteById(Payment.class, id);
	}

	@Override
	public void create(Payment payment) {
		logger.trace("insert Payment={}", payment);
		paymentRepo.save(payment);
	}

	@Override
	public void patch(Payment payment) {
		Payment existPayment = this.get(payment.getId());
		if (existPayment == null) {
			logger.error("not exist Payment={}", payment);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existPayment, payment);
			logger.trace("update Payment={}", payment);
			paymentRepo.update(payment);
		}
	}
}
