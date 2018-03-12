package com.newtouch.payment.service;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/08
 */
public interface SignatureService {
	/**
	 * 加密数据
	 * 
	 * @param dataToSign
	 * @return
	 */
	public String signature(byte[] dataToSign);

	/**
	 * 根据获得的加密签名串来验证 待加密原文是否被非法改动
	 * 
	 * @param dataToSign
	 * @param signedData
	 * @return
	 */
	public boolean verifySign(byte[] dataToSign, byte[] signedData);
}
