package com.newtouch.payment.service.impl;
/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.ReconciliationDetail;
import com.newtouch.payment.repository.ReconciliationDetailRepo;
import com.newtouch.payment.service.ReconciliationDetailService;

@Service("reconciliationDetailService")
public class ReconciliationDetailServiceImpl implements ReconciliationDetailService {
	
	private static Logger logger = LoggerFactory.getLogger(ReconciliationDetailServiceImpl.class);
	
	@Autowired
	private ReconciliationDetailRepo reconciliationDetailRepo;

	@Override
	public ReconciliationDetail get(Long id) {
		return reconciliationDetailRepo.getById(ReconciliationDetail.class, id);
	}

	@Override
	public void delete(Long id) {
		reconciliationDetailRepo.deleteById(ReconciliationDetail.class, id);
	}

	@Override
	public void create(ReconciliationDetail reconciliationDetail) {
		reconciliationDetailRepo.save(reconciliationDetail);
	}

	@Override
	public void patch(ReconciliationDetail reconciliationDetail) {
		ReconciliationDetail existReconciliationDetail = this.get(reconciliationDetail.getId());
		if (existReconciliationDetail == null) {
			logger.error("not exist ReconciliationDetail={}", existReconciliationDetail);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existReconciliationDetail, reconciliationDetail);
			logger.trace("update ReconciliationDetail={}", reconciliationDetail);
			reconciliationDetailRepo.update(reconciliationDetail);
		}					
	}

}
