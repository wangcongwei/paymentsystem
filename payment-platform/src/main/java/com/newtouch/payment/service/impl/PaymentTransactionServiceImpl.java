package com.newtouch.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.PaymentTransaction;
import com.newtouch.payment.repository.PaymentTransactionRepo;
import com.newtouch.payment.service.PaymentTransactionService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
@Service("paymentTransactionService")
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
	
	private static Logger logger = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);
	
	@Autowired
	private PaymentTransactionRepo paymentTransactionRepo;

	@Override
	public PaymentTransaction get(Long id) {
		return paymentTransactionRepo.getById(PaymentTransaction.class, id);
	}

	@Override
	public void delete(Long id) {
		logger.trace("logic delete PaymentTransaction id:{}", id);
		paymentTransactionRepo.deleteById(PaymentTransaction.class, id);
	}

	@Override
	public void create(PaymentTransaction paymentTransaction) {
		logger.trace("insert PaymentTransaction={}", paymentTransaction);
		paymentTransactionRepo.save(paymentTransaction);
	}

	@Override
	public void patch(PaymentTransaction paymentTransaction) {
		PaymentTransaction existPaymentTransaction = this.get(paymentTransaction.getId());
		if (existPaymentTransaction == null) {
			logger.error("not exist PaymentTransaction={}", existPaymentTransaction);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existPaymentTransaction, paymentTransaction);
			logger.trace("update PaymentTransaction={}", paymentTransaction);
			paymentTransactionRepo.update(paymentTransaction);
		}				
	}

}
