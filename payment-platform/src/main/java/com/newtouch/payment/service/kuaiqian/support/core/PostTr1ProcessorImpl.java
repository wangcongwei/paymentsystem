package com.newtouch.payment.service.kuaiqian.support.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.newtouch.payment.service.kuaiqian.support.entity.MerchantInfo;
import com.newtouch.payment.utils.kuaiqian.utils.Base64Binrary;
import com.newtouch.payment.utils.kuaiqian.utils.MyX509TrustManager;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public class PostTr1ProcessorImpl implements PostTr1Processor{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	public InputStream post(MerchantInfo merchantInfo) throws Exception {
			//验证密钥源
//			File certFile = new File(merchantInfo.getCertPath());
//			KeyStore ks = KeyStore.getInstance("JKS");
//			ks.load(new FileInputStream(certFile), merchantInfo.getCertPass().toCharArray());
			
			//----修改文件为相对路径
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new ClassPathResource(merchantInfo.getCertPath()).getInputStream(), merchantInfo.getCertPass().toCharArray());	
//			//----修改文件为相对路径
//			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, merchantInfo.getCertPass().toCharArray());
//	
//			//同位体验证信任决策源
			TrustManager[] tm = { new MyX509TrustManager() }; 
//			
//			//初始化安全套接字
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(kmf.getKeyManagers(),tm, null);

			SSLSocketFactory factory = sslContext.getSocketFactory();

			URL url = new URL(merchantInfo.getUrl());
			logger.info("无卡支付url: " + url.toString());
			HttpsURLConnection urlc = (HttpsURLConnection) url.openConnection();
			urlc.setSSLSocketFactory(factory);
//			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setDoOutput(true); 
			urlc.setDoInput(true); 
			urlc.setReadTimeout(merchantInfo.getTimeOut()*1000);
			
			String authString = merchantInfo.getMerchantId() + ":" + merchantInfo.getPassword();
			String auth = "Basic " + Base64Binrary.encodeBase64Binrary(authString.getBytes());
			urlc.setRequestProperty("Authorization", auth);
			
			logger.info("无卡支付url: " + urlc.toString());
			OutputStream out = urlc.getOutputStream();
			out.write(merchantInfo.getXml().getBytes());
			out.flush(); 
			out.close();
			
			return urlc.getInputStream();
	}
}
