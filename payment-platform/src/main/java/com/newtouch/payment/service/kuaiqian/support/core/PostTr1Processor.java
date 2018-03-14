package com.newtouch.payment.service.kuaiqian.support.core;


import java.io.InputStream;

import com.newtouch.payment.service.kuaiqian.support.entity.MerchantInfo;


/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface  PostTr1Processor {
	/**
	 * @param req 发送参数对象
	 * @return 服务返回
	 * @throws Exception
	 */
	public InputStream post(MerchantInfo req) throws  Exception;
}
