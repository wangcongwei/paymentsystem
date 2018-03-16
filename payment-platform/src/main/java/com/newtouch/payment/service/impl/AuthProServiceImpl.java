package com.newtouch.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.view.PropertyUtils;
import com.newtouch.payment.model.AuthPro;
import com.newtouch.payment.repository.AuthProRepo;
import com.newtouch.payment.service.AuthProService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
@Service("authProService")
public class AuthProServiceImpl implements AuthProService {
	
	private static Logger logger = LoggerFactory.getLogger(AuthProServiceImpl.class);
	
	@Autowired
	private AuthProRepo authProRepo;

	@Override
	public AuthPro get(Long id) {
		return authProRepo.getById(AuthPro.class, id);
	}

	@Override
	public void delete(Long id) {
		logger.trace("logic delete AuthPro id:{}", id);
		authProRepo.deleteById(AuthPro.class, id);
	}

	@Override
	public void create(AuthPro authPro) {
		logger.trace("insert AuthPro={}", authPro);
		authProRepo.save(authPro);		
	}

	@Override
	public void patch(AuthPro authPro) {
		AuthPro existAuthPro = this.get(authPro.getId());
		if (existAuthPro == null) {
			logger.error("not exist AuthPro={}", existAuthPro);
		} else {
			PropertyUtils.copyPropertiesSkipNull(existAuthPro, authPro);
			logger.trace("update AuthPro={}", authPro);
			authProRepo.update(authPro);
		}		
	}
	
}
