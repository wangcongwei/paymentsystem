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
import com.newtouch.payment.im.PayType;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.im.PaymentTransactionStatus;
import com.newtouch.payment.im.Platform;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.model.PaymentTransaction;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayRequestDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.repository.PaymentTransactionRepo;
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
	private PaymentTransactionRepo paymentTransactionRepo;
	@Autowired
	private PaymentRepo paymentRepo;

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

	private PaymentTransaction saveRequestMsg(KuaiqianQuickPayRequestDTO request) {
		// 根据支付流水好查询订单信息
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", request.getPaymentNo());
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);

		// 创建支付交易记录
		PaymentTransaction pt = new PaymentTransaction();
		payment.setPaymentStatus(PaymentStatus.PS_PAYING);
		payment.setCurrency(CommonConst.CURRENCY);
		pt.setPayment(payment);
		pt.setPaymentAmount(payment.getPaymentAmount());
		pt.setPaymentNo(payment.getPaymentNo());
		pt.setReqNo(payment.getPaymentNo());
		pt.setStatus(PaymentTransactionStatus.CREATE);
		pt.setPlatform(Platform.KUAIQIAN);
		pt.setPayType(PayType.QUICK);
		pt.setCardNo(request.getCardNo());
		if (StringUtils.isNotBlank(request.getExpiredMonth()) && StringUtils.isNotBlank(request.getExpiredYear())) {
			pt.setExpireDate(request.getExpiredMonth() + request.getExpiredYear());
		}
		pt.setCvv(request.getCvv2());
		pt.setBankCode(request.getBankCode());
		pt.setTelePhone(request.getTelePhone());
		paymentRepo.update(payment);
		paymentTransactionRepo.save(pt);

		return pt;
	}

	@Override
	public KuaiqianQuickPayResponseDTO quickPay(KuaiqianQuickPayRequestDTO request) {
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", request.getPaymentNo());
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		if (PaymentStatus.PS_NOEFFITIVE.equals(payment.getPaymentStatus())) {
			throw new ServiceException("支付号已失效，无法支付！");
		}
		if (!PaymentStatus.PS_WAITPAY.equals(payment.getPaymentStatus()) && !PaymentStatus.PS_PAYING.equals(payment.getPaymentStatus())) {
			throw new ServiceException("此支付号已支付过，请勿重复支付！");
		}
		PaymentTransaction pt = this.saveRequestMsg(request);
		return this.quickPay(pt, request);
	}

	@Override
	public KuaiqianQuickPayResponseDTO quickPay(PaymentTransaction pt, KuaiqianQuickPayRequestDTO request) {

		KuaiqianQuickPayResponseDTO response = new KuaiqianQuickPayResponseDTO();
		// 封装支付请求信息
		String requestMsg = this.createRequestMsg(pt,request);
		// 发送请求信息
		Map<String, String> resMap = this.sendQuickPayRequest(requestMsg);

		// 快钱tr2应答码如果为C0则表示非实时到款，返回支付成功，等待快钱TR3 到款通知，生效服务。如果非C0则表示实时到款操作
		response.setErrorCode("0");
		if ("C0".equals(resMap.get("responseCode"))) {
			response.setErrorCode("9");
			response.setErrorMessage("待通知支付状态");
			return response;
		}
		// 返回非成功状态
		if (!"00".equals(resMap.get("responseCode"))) {
			response.setErrorCode("9");
			if (resMap.get("responseCode") == null) {
				pt.setResponseMessage(resMap.get("errorMessage"));
				response.setResponseCode(resMap.get("errorCode"));
				response.setErrorMessage(resMap.get("errorMessage"));
			} else {
				pt.setResponseMessage(resMap.get("responseTextMessage"));
				response.setResponseCode(resMap.get("responseCode"));
				response.setErrorMessage(resMap.get("responseTextMessage"));
			}
			pt.setStatus("FAILED");
			paymentTransactionRepo.update(pt);
			return response;
		}
		if (!(pt.getPaymentNo()).equals(resMap.get("externalRefNumber"))) {
			pt.setResponseMessage("已支付但支付返回的单号不一致。");
			response.setErrorCode("2");
			response.setErrorMessage("已支付但支付返回的单号不一致。");
			paymentTransactionRepo.update(pt);
			return response;
		}
		if (BigDecimal.valueOf(pt.getPaymentAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(
				BigDecimal.valueOf(Double.valueOf(resMap.get("amount"))).setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {

			pt.setResponseMessage("已支付但支付金额不一致。");
			response.setErrorCode("2");
			response.setErrorMessage("已支付但支付金额不一致。");
			paymentTransactionRepo.update(pt);
			return response;
		}
		try {
			Double payAmount = BigDecimal.valueOf(Double.valueOf(resMap.get("amount")))
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Date dealTime = DateUtils.parseDate(resMap.get("transTime"), new String[]{CommonConst.datePatternyyyyMMddHHmmss});
			pt.setStatus("SUCCESS");
			pt.setDealNo(resMap.get("refNumber"));
			pt.setPayTime(dealTime);
			pt.setPayAmount(payAmount);
			pt.setCardNo(resMap.get("storableCardNo"));
			pt.setBankCode(resMap.get("cardOrg"));
			pt.setBankName(resMap.get("issuer"));
			Payment payment = pt.getPayment();
			payment.setDealNo(resMap.get("refNumber"));
			payment.setPayTime(dealTime);
			payment.setPayAmount(payAmount);
			payment.setPaymentStatus(PaymentStatus.PS_SUCCESS);
			payment.setMerchantNo(resMap.get("merchantId"));
			payment.setPlatform(pt.getPlatform());
			payment.setPayType(pt.getPayType());
			paymentTransactionRepo.update(pt);
			paymentRepo.update(payment);
		} catch (Exception e) {
			response.setErrorCode("1");
			response.setErrorMessage("已支付但支付信息数据处理失败。");
			return response;
		}

		return response;
	}

	private String createRequestMsg(PaymentTransaction pt, KuaiqianQuickPayRequestDTO request) {
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
		if (StringUtils.isNotBlank(pt.getCardNo()))
			msgContent.addElement("cardNo").setText(pt.getCardNo());
		// 卡效期
		if (StringUtils.isNotBlank(pt.getExpireDate()))
			msgContent.addElement("expiredDate").setText(pt.getExpireDate());
		// 卡校验码
		if (StringUtils.isNotBlank(pt.getCvv()))
			msgContent.addElement("cvv2").setText(pt.getCvv());
		// 金额
		msgContent.addElement("amount").setText(BigDecimal.valueOf(pt.getPaymentAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		// 商户号
		msgContent.addElement("merchantId").setText(merchantId);
		// 终端号
		msgContent.addElement("terminalId").setText(terminalId);
		// 持卡人姓名
		if (StringUtils.isNotBlank(request.getAccountName()))
			msgContent.addElement("cardHolderName").setText(request.getAccountName());
		// 持卡人证件号
		if (StringUtils.isNotBlank(request.getCardHolderId()))
			msgContent.addElement("cardHolderId").setText(request.getCardHolderId());
		// 证件类型
		if (StringUtils.isNotBlank(request.getIdType())) {
			msgContent.addElement("idType").setText(request.getIdType());
		}
		// 商户端交易时间
		String txnTime = DateFormatUtils.format(new Date(), CommonConst.datePatternyyyyMMddHHmmss);
		msgContent.addElement("entryTime").setText(txnTime);
		// 外部跟踪编号
		msgContent.addElement("externalRefNumber").setText(pt.getPaymentNo());
		// 特殊交易标志 QuickPay快捷支付
		msgContent.addElement("spFlag").setText("QuickPay");
		// 快捷支付模式下 extDate 包含一下字段
		Element extMap = msgContent.addElement("extMap");
		// 手机号
		if (StringUtils.isNotBlank(pt.getTelePhone())) {
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("phone");
			ext.addElement("value").setText(pt.getTelePhone());
		}
		// 手机验证码
		if (StringUtils.isNotBlank(request.getValidCode())) {
			Element ext = extMap.addElement("extDate");
			ext.addElement("key").setText("validCode");
			ext.addElement("value").setText(request.getValidCode());
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
			 String token = ehcacheManager.getCache("ODCache").get("token_"+ pt.getPaymentNo()).toString();
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
