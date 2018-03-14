package com.newtouch.payment.service.kuaiqian.support.send;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.newtouch.payment.service.kuaiqian.support.core.PostTr1Processor;
import com.newtouch.payment.service.kuaiqian.support.core.PostTr1ProcessorImpl;
import com.newtouch.payment.service.kuaiqian.support.entity.MerchantInfo;
import com.newtouch.payment.service.kuaiqian.support.entity.TransInfo;
import com.newtouch.payment.service.kuaiqian.support.util.ParseUtil;
import com.newtouch.payment.utils.PropFile;


/**
 * @Description: 快钱VOP_CNP的Interface接口程序
 * @Copyright (c) 上海快钱信息服务有限公司
 * @version 2.0
 */

/**
 * 该类用来拼接XML串和解析XML
 * */
@SuppressWarnings("unchecked")
public class SendTR1  {
	
	//初始化配置文件
	private static Properties p=PropFile.getProps("/payment/quickpay/mgw.properties");
	
	private static Log logger = LogFactory.getLog(SendTR1.class);
	/**
	 * 该方法用于正常的交易拼接XML，返回StringBuffer
	 * sb：初始StringBuffer
	 * paraName：XML中的标签名
	 * paraValue：XML中标签对应的值
	 **/
	public static StringBuffer transParams(StringBuffer reqXml,String paraName,String paraValue ,TransInfo transInfo){
		//如果输入的值为null,那么使它们的值为""
		if(paraValue==null){
			paraValue="";
		}
		//下面开始组合XML
		if(reqXml==null){
			reqXml=new StringBuffer();
			String contentXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>"+p.getProperty("version")+"</version>";
			reqXml.append(contentXML).append("<").append(transInfo.getRecordeText_1()).append(">")
				  .append("<interactiveStatus>TR1</interactiveStatus>")
				  .append("<txnType>").append(transInfo.getTxnType()).append("</txnType>")
				  .append("<merchantId>").append(p.getProperty("merchantId")).append("</merchantId>")
//				  .append("<settleMerchantId>").append(p.getProperty("settleMerchantId")).append("</settleMerchantId>")
				  .append("<terminalId>").append(p.getProperty("terminalId")).append("</terminalId>")
				  .append("<tr3Url>").append(p.getProperty("tr3Url")).append("</tr3Url>")
				  .append("<entryTime>").append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).append("</entryTime>");
				  if(!"".equals(paraValue)){
					  reqXml.append("<").append(paraName).append(">").append(paraValue).append("</").append(paraName).append(">");
				  }
		}else if((!"".equals(paraName)) && (!"".equals(paraValue))){
			reqXml.append("<").append(paraName).append(">").append(paraValue).append("</").append(paraName).append(">");
		}else if(("".equals(paraName)) && ("".equals(paraValue))){
			reqXml.append("</").append(transInfo.getRecordeText_1()).append(">").append("</MasMessage>");
		}
		return reqXml;
	}
	
	/**
	 *查询的提交报文XML 
	 * **/
	public static StringBuffer appendParam(StringBuffer reqXml,String paraName,String paraValue,TransInfo transInfo){
		//如果输入的值为null,那么使它们的值为""
		if(paraValue==null){
			paraValue="";
		}
		//下面开始组合XML
		if(reqXml==null){
			reqXml=new StringBuffer();
			String contentXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>"+p.getProperty("version")+"</version>";
			reqXml.append(contentXML).append("<").append(transInfo.getRecordeText_1()).append(">");
			if(!"".equals(paraValue)){
				reqXml.append("<").append(paraName).append(">").append(paraValue).append("</").append(paraName).append(">");
			}
		}else if((!"".equals(paraName)) && (!"".equals(paraValue))){
			reqXml.append("<").append(paraName).append(">").append(paraValue).append("</").append(paraName).append(">");
		}else if(("".equals(paraName)) && ("".equals(paraValue))){
			reqXml.append("</").append(transInfo.getRecordeText_1()).append(">").append("</MasMessage>");
		}
		return reqXml;
	}
	
	/**
	 * 提交TR1信息，并且获取TR2
	 * */
	public static HashMap sendTR1(String tr1XML , TransInfo transInfo){
		MerchantInfo merchantInfo=null;
		try {
			merchantInfo=new MerchantInfo();
			merchantInfo.setCertPass(p.getProperty("certPassword"));
			merchantInfo.setCertPath(p.getProperty("certFileName"));
			merchantInfo.setMerchantId(p.getProperty("merchantId"));
			merchantInfo.setPassword(p.getProperty("merchantLoginKey"));
			merchantInfo.setTimeOut(p.getProperty("timeout"));
			merchantInfo.setDomainName(p.getProperty("domainName"));
			merchantInfo.setSslPort(p.getProperty("sslPort"));
			merchantInfo.setUrl(transInfo.getPostUrl());
			merchantInfo.setXml(tr1XML);
		} catch (Exception e) {
			System.out.println("读取配置文件出错!");
			e.printStackTrace();
		}
		
		//初始化服务器类
		PostTr1Processor ptp =  new PostTr1ProcessorImpl();
		//得到服务端返回
		HashMap respXml = null;
		try {
			//返回TR2信息
			InputStream is = ptp.post(merchantInfo);
			if(is!=null){
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] receiveBuffer = new byte[2048];
				int readBytesSize = is.read(receiveBuffer);
				while(readBytesSize != -1){
					bos.write(receiveBuffer, 0, readBytesSize);
					readBytesSize = is.read(receiveBuffer);
				}
				String reqData = new String(bos.toByteArray(), "UTF-8");
				logger.info("================快钱TR2报文："+reqData);
				respXml= ParseUtil.parseXML(reqData,transInfo);//给解析XML的函数传递快钱返回的TR2的XML数据流
			}
		} catch (Exception e) {
			logger.info("发送TR1失败");
			e.printStackTrace();
		}
		return respXml;
	}
}