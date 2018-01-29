package com.newtouch.payment.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.model.TPayPlatformTransation;
import com.newtouch.payment.service.TransFlowSearch;
import com.newtouch.payment.utils.PropFile;
import com.newtouch.payment.utils.StreamUtil;
import com.newtouch.payment.utils.kuaiqian.utils.MyX509TrustManager;

@Service(value="transFlowSearch")
public class TransFlowSearchImpl implements TransFlowSearch{

	private static final Logger logger = LoggerFactory.getLogger(TransFlowSearchImpl.class);
	
	private static Properties pro=PropFile.getProps("/payment/quickpay/mgw.properties");
	private String merchantId;
	private String terminalId;
	private String merchantLoginKey;
	private String payUrl;
	
	{
		merchantId = pro.getProperty("merchantId");
		terminalId = pro.getProperty("terminalId");
		merchantLoginKey = pro.getProperty("merchantLoginKey");
		payUrl = pro.getProperty("domainName") + ":" + pro.getProperty("sslPort") + "/cnp/query_txn";
	}
	
	private HttpsURLConnection getUrlc (){
		
		// 加载https请求所需证书
		try {
			KeyStore ks;
			System.setProperty("jsse.enableSNIExtension", "false");
			ks = KeyStore.getInstance("JKS");
			ClassPathResource claspath = new ClassPathResource(pro.getProperty("certFileName"));
			ks.load(new FileInputStream(claspath.getFile()), "vpos123".toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, "vpos123".toCharArray());
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(kmf.getKeyManagers(), tm, null);
			SSLSocketFactory factory = sslContext.getSocketFactory();
			HttpsURLConnection urlc = (HttpsURLConnection) new URL(payUrl).openConnection();
			urlc.setSSLSocketFactory(factory);

			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setRequestProperty("Content-type", "text/html");
			urlc.setReadTimeout(60 * 1000);// 设置超时时间
			return urlc;
		} catch (Exception e) {
			logger.info("======快捷交易查询请求连接异常", e);
			throw new ServiceException("快捷交易查询请求连接异常！", e);
		}

	}
	
	/**
	 * 调用快钱交易流水查询接口获取交易状态
	 * @param tppt（交易流水信息）
	 * @return 交易流水状态（FAILED-失败；SUCCESS-成功；CREATE-处理中）
	 */
	@Override
	public String searchTransFlow(TPayPlatformTransation tppt) {
		String txnStatus = tppt.getStatus();
		//封装请求信息
		String requestMsg = this.createRequestMsg(tppt);
		//请求交易流水查询接口，获取返回信息
		String responseMsg = this.sendTransSearchRequest(requestMsg);
		
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(responseMsg);
		} catch (Exception e) {
			throw new ServiceException("交易流水查询返回报文格式错误！");
		}
		Element rootE = doc.getRootElement();
		Element msgContent = rootE.element("TxnMsgContent");
		if (msgContent != null) {
			Element txnStatusE = msgContent.element("txnStatus");
			if(txnStatusE != null){
				String txnStatusT = txnStatusE.getText();
				if("S".equals(txnStatusT)){
					txnStatus = "SUCCESS";
				}else if("F".equals(txnStatusT)){
					txnStatus = "FAILED";
				}
			}
		}
		
		return txnStatus;
	}
	
	private String createRequestMsg(TPayPlatformTransation tppt) {
		Document document = DocumentHelper.createDocument();
		// 创建一个根节点
		Element masMessage = document.addElement("MasMessage", "http://www.99bill.com/mas_cnp_merchant_interface");
		masMessage.addElement("version").setText("1.0");
		Element msgContent = masMessage.addElement("QryTxnMsgContent");
		// 交易类型
		msgContent.addElement("txnType").setText("PUR");
		// 商户号
		msgContent.addElement("merchantId").setText(merchantId);
		// 终端号
		msgContent.addElement("terminalId").setText(terminalId);
		// 外部跟踪编号
		msgContent.addElement("externalRefNumber").setText(tppt.getReqNo());
		
		return document.asXML();
	}
	
	private String sendTransSearchRequest(String rquestXML) {
		logger.info("======交易流水查询请求信息：" + rquestXML);
		String authString = merchantId + ":" + merchantLoginKey;
		String auth = "Basic " + new String(Base64.encodeBase64(authString.getBytes()));
		HttpsURLConnection urlc = this.getUrlc();
		urlc.setRequestProperty("Authorization", auth);
		String responseMsg = null;
		InputStream is = null;
		try {
			// 发送urlConnection请求
			OutputStream out = urlc.getOutputStream();
			out.write(rquestXML.getBytes());
			out.flush();
			out.close();
			// 获取返回结果
			is = urlc.getInputStream();
			responseMsg = StreamUtil.changeISToStr(is, "UTF-8");
			logger.info("======交易流水查询返回信息：" + responseMsg);
		} catch (IOException e) {
			logger.info("======交易流水查询请求失败", e);
			throw new ServiceException("交易流水查询请求失败",e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("======交易流水查询返回输入流关闭异常", e);
				}
			}
		}
		
		return responseMsg;
	}

	public static void setPro(Properties pro) {
		TransFlowSearchImpl.pro = pro;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setMerchantLoginKey(String merchantLoginKey) {
		this.merchantLoginKey = merchantLoginKey;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
}