package com.newtouch.payment.service;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.newtouch.payment.constant.CommonConst;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/08
 */
public class SerialNumberService {
	/** 
	 * 生成业务流水号 
	 * 渠道代码+时间（12位，年月日时分秒）+6位随机数 
	 *  
	 * @param comCode 系统标识 
	 * @return 
	 */  
	public static synchronized String createSerial(String comCode) {
		String strDate = DateFormatUtils.format(new Date(), CommonConst.datePatternYYMMDDHHMMSS);
		return comCode + strDate + RandomStringUtils.randomNumeric(6);  
	}
	
	/** 
	 * 生成验证码
	 * 6位随机数
	 * 
	 * @return 
	 */  
	public static synchronized String createCheckNo() {
		return RandomStringUtils.randomNumeric(6);  
	}
}
