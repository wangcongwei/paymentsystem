package com.newtouch.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.ApplyDetails;
import com.newtouch.payment.repository.ApplyDetailsRepo;
import com.newtouch.payment.service.ApplyDetailsService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018年3月6日
 */
@Service("applyDetailsService")
public class ApplyDetailsServiceImpl implements ApplyDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(ApplyDetailsServiceImpl.class);
	
	@Autowired
	private ApplyDetailsRepo applyDetailsRepo;

	@Override
	public ApplyDetails get(Long id) {
		return applyDetailsRepo.getById(ApplyDetails.class, id);
	}

	@Override
	public void delete(Long id) {
		logger.trace("logic delete ApplyDetails id:{}", id);
		applyDetailsRepo.deleteById(ApplyDetails.class, id);
	}

	@Override
	public void create(ApplyDetails applyDetails) {
		logger.trace("insert ApplyDetails={}", applyDetails);
		applyDetailsRepo.save(applyDetails);
	}

	@Override
	public void patch(ApplyDetails applyDetails) {
		ApplyDetails existApplyDetails = this.get(applyDetails.getId());
		if (existApplyDetails == null) {
			logger.error("not exist applyDetails={}", applyDetails);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existApplyDetails, applyDetails);
			logger.trace("update applyDetails={}", applyDetails);
			applyDetailsRepo.update(applyDetails);
		}
	}
	
}
