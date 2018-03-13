package com.newtouch.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.Merchant;
import com.newtouch.payment.repository.MerchantRepo;
import com.newtouch.payment.service.MerchantService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public class MerchantServiceImpl implements MerchantService {
	
	private static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
	
	@Autowired
	private MerchantRepo merchantRepo;

	@Override
	public Merchant get(Long id) {
		return merchantRepo.getById(Merchant.class, id);
	}

	@Override
	public void delete(Long id) {
		logger.trace("logic delete Merchant id:{}", id);
		merchantRepo.deleteById(Merchant.class, id);
	}

	@Override
	public void create(Merchant merchant) {
		logger.trace("insert Merchant={}", merchant);
		merchantRepo.save(merchant);		
	}

	@Override
	public void patch(Merchant merchant) {
		Merchant existMerchant = this.get(merchant.getId());
		if (existMerchant == null) {
			logger.error("not exist Merchant={}", existMerchant);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existMerchant, merchant);
			logger.trace("update Merchant={}", merchant);
			merchantRepo.update(merchant);
		}		
	}
	
}
