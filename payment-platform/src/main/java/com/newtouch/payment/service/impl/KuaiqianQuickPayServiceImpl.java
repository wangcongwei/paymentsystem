package com.newtouch.payment.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.KeyStore;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.OrderPayRequest;
import com.newtouch.payment.model.TPayPlatformTransation;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayRequestDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.repository.OrderPayRequestRepo;
import com.newtouch.payment.repository.OrderRepo;
import com.newtouch.payment.repository.PayPlatformTransactionRepo;
import com.newtouch.payment.service.KuaiqianQuickPayService;
import com.newtouch.payment.utils.PropFile;
import com.newtouch.payment.utils.StreamUtil;
import com.newtouch.payment.utils.kuaiqian.utils.MyX509TrustManager;

/**
 * 快钱快捷支付请求发送
 * 
 * @author RenGL
 *
 *         2015年8月31日
 */
@Service(value = "kuaiqianQuickPayService")
public class KuaiqianQuickPayServiceImpl implements KuaiqianQuickPayService {

	private static final Logger logger = LoggerFactory.getLogger(KuaiqianQuickPayServiceImpl.class);
	private static Properties pro=PropFile.getProps("/payment/quickpay/mgw.properties");
	private String merchantId;
	private String terminalId;
	private String merchantLoginKey;
	private String tr3Url;
	private String payUrl;
	@Resource(name = "ehcacheManager")
	private CacheManager ehcacheManager;
	@Autowired
	private PayPlatformTransactionRepo payPlatformTransactionRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private OrderPayRequestRepo orderPayRequestRepo;

	private HttpsURLConnection urlc = null;

	{
		merchantId = pro.getProperty("merchantId");
		terminalId = pro.getProperty("terminalId");
		merchantLoginKey = pro.getProperty("merchantLoginKey");
		tr3Url = pro.getProperty("tr3Url");
		payUrl = pro.getProperty("domainName") + ":" + pro.getProperty("sslPort") + "/cnp/purchase";
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
			urlc = (HttpsURLConnection) new URL(payUrl).openConnection();
			urlc.setSSLSocketFactory(factory);

			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setRequestProperty("Content-type", "text/html");
			urlc.setReadTimeout(60 * 1000);// 设置超时时间

		} catch (Exception e) {
			logger.info("======快捷支付请求连接异常", e);
		}

	}

	private TPayPlatformTransation saveRequestMsg(KuaiqianQuickPayRequestDTO request) {
		// 根据支付流水好查询订单信息
		QueryParams queryParams = new QueryParams();
		queryParams.put("paySeriNo", request.getUserTransactionNo());
		OrderPayRequest orderPayRequest = orderPayRequestRepo.findByPaySeriNo(OrderPayRequest.class, queryParams);
		queryParams.put("orderNo", orderPayRequest.getOrderNo());
		Order order = orderRepo.findByOrderNo(Order.class, queryParams);

		// 创建支付交易记录
		TPayPlatformTransation tppt = new TPayPlatformTransation();
		tppt.setAccountName(orderPayRequest.getUserName());
		tppt.setAccountno(request.getCardNo());
		tppt.setValiddate(request.getExpiredDate());
		tppt.setCcv2(request.getCvv2());
		tppt.setAmount(Double.valueOf(order.getAmount().doubleValue()));
		tppt.setIdType(request.getIdType());
		tppt.setIdNo(request.getCardHolderId());
		tppt.setVerifycode(request.getValidCode());
		tppt.setCreateDate(new Date());
		tppt.setPayTime(new Date());
		tppt.setCurrency("CNY");
		tppt.setMobile(orderPayRequest.getUserMobile());
		tppt.setOrderNo(order.getOrderNo());
		tppt.setReqNo(request.getUserTransactionNo());
		tppt.setStatus("CREATE");
		order.setOrderStatus("3");
		orderRepo.update(order);
		payPlatformTransactionRepo.save(tppt);

		return tppt;
	}

	@Override
	public KuaiqianQuickPayResponseDTO quickPay(KuaiqianQuickPayRequestDTO request) {
		TPayPlatformTransation tppt = this.saveRequestMsg(request);
		return this.quickPay(tppt);
	}

	@Override
	public KuaiqianQuickPayResponseDTO quickPay(TPayPlatformTransation tppt) {

		KuaiqianQuickPayResponseDTO response = new KuaiqianQuickPayResponseDTO();
		// 封装支付请求信息
		String requestMsg = this.createRequestMsg(tppt);
		// 发送请求信息
		Map<String, String> resMap = this.sendQuickPayRequest(requestMsg);

		// 快钱tr2应答码如果为C0则表示非实时到款，返回支付成功，等待快钱TR3 到款通知，生效服务。如果非C0则表示实时到款操作
		response.setErrorCode("0");
		response.setOrderNo(tppt.getOrderNo());
		response.setOrderAmount(tppt.getAmount().toString());
		if ("C0".equals(resMap.get("responseCode"))) {
			tppt.setEnable_time(DateUtils.addHours(new Date(), 24));
			tppt.setQuery_time(DateUtils.addMilliseconds(new Date(), 15));
			tppt.setQuery_times(0);
			payPlatformTransactionRepo.update(tppt);
			return response;
		}
		// 返回非成功状态
		if (!"00".equals(resMap.get("responseCode"))) {
			response.setErrorCode("9");
			if (resMap.get("responseCode") == null) {
				tppt.setMessagecode(resMap.get("errorCode"));
				tppt.setMessage(resMap.get("errorMessage"));
				response.setResponseCode(resMap.get("errorCode"));
				response.setErrorMessage(resMap.get("errorMessage"));
			} else {
				tppt.setMessagecode(resMap.get("responseCode"));
				tppt.setMessage(resMap.get("responseTextMessage"));
				response.setResponseCode(resMap.get("responseCode"));
				response.setErrorMessage(resMap.get("responseTextMessage"));
			}
			tppt.setStatus("FAILED");
			payPlatformTransactionRepo.update(tppt);
			return response;
		}
		tppt.setStatus("SUCCESS");
		if (!(tppt.getReqNo()).equals(resMap.get("externalRefNumber"))) {
			tppt.setMessage("已支付但支付返回的单号不一致。");
			response.setErrorCode("2");
			response.setErrorMessage("已支付但支付返回的单号不一致。");
			payPlatformTransactionRepo.update(tppt);
			return response;
		}
		if (BigDecimal.valueOf(Double.valueOf(tppt.getAmount())).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(
				BigDecimal.valueOf(Double.valueOf(resMap.get("amount"))).setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {

			tppt.setMessage("已支付但支付金额不一致。");
			response.setErrorCode("2");
			response.setErrorMessage("已支付但支付金额不一致。");
			payPlatformTransactionRepo.update(tppt);
			return response;
		}
		try {
			Double payAmount = BigDecimal.valueOf(Double.valueOf(resMap.get("amount")))
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Date dealTime = DateUtils.parseDate(resMap.get("transTime"), new String[]{CommonConst.datePatternyyyyMMddHHmmss});
			Date now = new Date();
			tppt.setResNo(resMap.get("refNumber"));
			tppt.setReturnTime(now);
			tppt.setPaysuccessTime(dealTime);
			tppt.setPayAmount(payAmount);
			tppt.setStorableCardNo(resMap.get("storableCardNo"));
			tppt.setCardOrg(resMap.get("cardOrg")); // 卡组织编号
			tppt.setIssuer(resMap.get("issuer")); // 发卡银行名称
			// 订单表数据组装
			Order order = new Order();
			order.setOrderStatus("4"); // 支付成功
			order.setUpdateTime(now);
			payPlatformTransactionRepo.update(tppt);
			orderRepo.update(order);
		} catch (Exception e) {
			response.setErrorCode("1");
			response.setErrorMessage("已支付但支付信息数据处理失败。");
			return response;
		}

		return response;
	}

	private String createRequestMsg(TPayPlatformTransation tppt) {
		Document document = DocumentHelper.createDocument();
		// 创建一个根节点
		Element masMessage = document.addElement("MasMessage", "http://www.99bill.com/mas_cnp_merchant_interface");
		masMessage.addElement("version").setText("1.0");
		Element msgContent = masMessage.addElement("TxnMsgContent");
		// 交易类型
		msgContent.addElement("txnType").setText("PUR");
		// 消息状态
		msgContent.addElement("interactiveStatus").setText("TR1");
		// 卡号
		if (StringUtils.isNotBlank(tppt.getAccountno()))
			msgContent.addElement("cardNo").setText(tppt.getAccountno());
		// 卡效期
		if (StringUtils.isNotBlank(tppt.getValiddate()))
			msgContent.addElement("expiredDate").setText(tppt.getValiddate());
		// 卡校验码
		if (StringUtils.isNotBlank(tppt.getCcv2()))
			msgContent.addElement("cvv2").setText(tppt.getCcv2());
		// 金额
		msgContent.addElement("amount")
				.setText(BigDecimal.valueOf(tppt.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		// 商户号
		msgContent.addElement("merchantId").setText(merchantId);
		// 终端号
		msgContent.addElement("terminalId").setText(terminalId);
		// 持卡人姓名
		if (StringUtils.isNotBlank(tppt.getAccountName()))
			msgContent.addElement("cardHolderName").setText(tppt.getAccountName());
		// 持卡人证件号
		if (StringUtils.isNotBlank(tppt.getIdNo()))
			msgContent.addElement("cardHolderId").setText(tppt.getIdNo());
		// 证件类型
		if (StringUtils.isNotBlank(tppt.getIdType())) {
			msgContent.addElement("idType").setText(tppt.getIdType());
		}
		// 商户端交易时间
		String txnTime = DateFormatUtils.format(new Date(), CommonConst.datePatternyyyyMMddHHmmss);
		msgContent.addElement("entryTime").setText(txnTime);
		// 外部跟踪编号
		msgContent.addElement("externalRefNumber").setText(tppt.getReqNo());
		// 特殊交易标志 QuickPay快捷支付
		msgContent.addElement("spFlag").setText("QuickPay");
		// 快捷支付模式下 extDate 包含一下字段
		Element extMap = msgContent.addElement("extMap");
		// 手机号
		if (StringUtils.isNotBlank(tppt.getMobile())) {
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("phone");
			ext.addElement("value").setText(tppt.getMobile());
		}
		// 手机验证码
		if (StringUtils.isNotBlank(tppt.getVerifycode())) {
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("validCode");
			ext.addElement("value").setText(tppt.getVerifycode());
		}
		// 不保存鉴权信息
		{
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("savePciFlag");
			ext.addElement("value").setText("0");
		}
		// 手机验证码令牌
		{
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("token");
//			 String token = (String)phoneTokenCache.getObjectFromCache("token_"+ tppt.getOrderNo());
			 String token = ehcacheManager.getCache("ODCache").get("token_"+ tppt.getOrderNo()).toString();
//			String token = "1163702";
			ext.addElement("value").setText(token == null ? "" : token);
		}
		// 快捷支付批次
		{
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("payBatch");
			ext.addElement("value").setText("1");
		}
		// tr3Url
		if (tr3Url != null) {
			msgContent.addElement("tr3Url").setText(tr3Url);
		}
		return document.asXML();
	}

	private Map<String, String> sendQuickPayRequest(String rquestXML) {
		logger.info("======快捷支付请求信息：" + rquestXML);
		String authString = merchantId + ":" + merchantLoginKey;
		String auth = "Basic " + new String(Base64.encodeBase64(authString.getBytes()));
		urlc.setRequestProperty("Authorization", auth);
		int statusCode = 0;
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
			logger.info("======快捷支付返回信息：" + responseMsg);
		} catch (IOException e) {
			logger.info("======快捷支付请求失败", e);
			throw new ServiceException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("======快捷支付返回输入流关闭异常", e);
				}
			}
		}
		if (statusCode != HttpStatus.OK.value()) {
			logger.info("======快钱快捷支付，返回非成功状态，状态值：" + statusCode);
			throw new ServiceException("快捷支付请求失败");
		}
		Map<String, String> resMap = new LinkedHashMap<String, String>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(responseMsg);
		} catch (Exception e) {
			throw new ServiceException("快捷支付返回报文格式错误！");
		}
		Element rootE = doc.getRootElement();
		Element msgContent = rootE.element("TxnMsgContent");
		if (msgContent == null) {
			msgContent = rootE.element("ErrorMsgContent");
		}
		if (msgContent != null) {
			@SuppressWarnings("unchecked")
			List<Element> elements = (List<Element>) msgContent.elements();
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				resMap.put(e.getName(), e.getText());
			}
		}
		return resMap;
	}

	public static void setPro(Properties pro) {
		KuaiqianQuickPayServiceImpl.pro = pro;
	}

}
