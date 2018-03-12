package com.newtouch.payment.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.Properties;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.service.DynamicVerifyCodeService;
import com.newtouch.payment.utils.kuaiqian.utils.Base64Binrary;
import com.newtouch.payment.utils.kuaiqian.utils.MyX509TrustManager;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/12
 */
@Service(value = "dynamicVerifyCodeService")
public class DynamicVerifyCodeServiceImpl implements DynamicVerifyCodeService {

	private static final Logger logger = LoggerFactory.getLogger(DynamicVerifyCodeServiceImpl.class);

	// 快钱-快捷支付手机动态鉴权访问地址
	private String quickPayUrl;
	// 快钱-快捷支付商户号
	private String merchantId;
	// 快钱-快捷支付认证密码
	private String quickPayPassword;
	// 配置文件
	private Properties pro;

	@Resource(name = "ehcacheManager")
	private CacheManager ehcacheManager;
	@Autowired
	private PaymentRepo paymentRepo;

	@Override
	public DynamicVerifyCodeResponseDTO getDynamicVerifyCode(DynamicVerifyCodeRequestDTO request) {
		// 返回结果对象
		DynamicVerifyCodeResponseDTO response = new DynamicVerifyCodeResponseDTO();
		// 非空字段逻辑校验
		if (StringUtils.isBlank(request.getPaymentNo())) {
			response.setErrorCode("2");
			response.setErrorMessage("支付号不能为空！");
			return response;
		} else if (StringUtils.isBlank(request.getPlatform())) {
			response.setErrorCode("2");
			response.setErrorMessage("第三方支付平台号不能为空！");
			return response;
		} else if (StringUtils.isBlank(request.getAccountNo())) {
			response.setErrorCode("2");
			response.setErrorMessage("银行账号不能为空！");
			return response;
		} else if (StringUtils.isBlank(request.getTelePhone())) {
			response.setErrorCode("2");
			response.setErrorMessage("手机号码不能为空！");
			return response;
		}
		//根据支付流水好查询订单信息
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", request.getPaymentNo());
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		if (payment != null && payment.getPaymentAmount() != null) {
		} else {
			response.setErrorCode("2");
			response.setErrorMessage("该支付号对应的支付金额为空，请确认！");
			return response;
		}

		try {
			System.setProperty("jsse.enableSNIExtension", "false");
			// 加载https请求所需证书
			KeyStore ks = KeyStore.getInstance("JKS");
			ClassPathResource claspath = new ClassPathResource(pro.getProperty("certFileName"));
			ks.load(new FileInputStream(claspath.getFile()), "vpos123".toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, "vpos123".toCharArray());
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(kmf.getKeyManagers(), tm, null);
			SSLSocketFactory factory = sslContext.getSocketFactory();
			// 认证的用户名和密码
			String authString = merchantId + ":" + quickPayPassword;
			String auth = "Basic " + Base64Binrary.encodeBase64Binrary(authString.getBytes());
			// 创建url
			URL url = new URL(quickPayUrl);
			HttpsURLConnection urlc = (HttpsURLConnection) url.openConnection();
			urlc.setSSLSocketFactory(factory);
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setRequestProperty("Content-type", "text/html");
			urlc.setReadTimeout(60 * 1000);// 设置超时时间
			// 封装头部认证信息
			urlc.setRequestProperty("Authorization", auth);
			// 发送urlConnection请求
			OutputStream out = urlc.getOutputStream();
			out.write(createRequestMsg(request, payment).getBytes());
			out.flush();
			out.close();
			// 获取返回结果
			InputStream is = urlc.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String resultXML = buffer.toString();
			logger.info("======getDnyNumResponse: " + resultXML);
			// dom4j解析返回XML报文
			Document doc = DocumentHelper.parseText(resultXML);
			Element rootElt = doc.getRootElement(); // 获取根节点
			Element getDynNumContent = rootElt.element("GetDynNumContent"); // 获取内容根节点
			if (getDynNumContent != null) {
				if ("00".equals(getDynNumContent.elementText("responseCode"))) {
					// memcahe中放入令牌
					String token = getDynNumContent.elementText("token");
					ehcacheManager.getCache("ODCache").put("token_" + payment.getPaymentNo(), token);
				} else {
					response.setErrorCode("1");
					response.setErrorMessage("调用服务失败！");
					response.setResponseCode(getDynNumContent.elementText("responseCode"));
					response.setResponseMessage(getDynNumContent.elementText("responseTextMessage"));
					return response;
				}
				response.setErrorCode("0");
				response.setErrorMessage("快捷支付手机动态码发送成功！");
			} else {
				response.setErrorCode("2");
				response.setErrorMessage("快捷支付手机动态鉴权失败！");
				return response;
			}
		} catch (Exception e) {
			logger.info("调用快捷支付手机动态码服务异常：", e);
			response.setErrorCode("2");
			response.setErrorMessage("调用快捷支付手机动态码服务异常！");
			return response;
		}
		return response;
	}

	private String createRequestMsg(DynamicVerifyCodeRequestDTO request, Payment payment) {
		String xmlContent = "";
		try {
			// 创建文档对象
			Document document = DocumentHelper.createDocument();
			// 创建一个根节点
			Element masMessage = document.addElement("MasMessage", "http://www.99bill.com/mas_cnp_merchant_interface");
			// 添加子元素对象
			Element version = masMessage.addElement("version");// 版本号
			version.setText("1.0");// 版本号赋值
			// 添加子元素对象
			Element getDynNumContent = masMessage.addElement("GetDynNumContent");
			// 添加二级子元素对象
			Element merchantIdNo = getDynNumContent.addElement("merchantId");// 商户号
			merchantIdNo.setText(merchantId);
			Element externalRefNumber = getDynNumContent.addElement("externalRefNumber");// 外部跟踪编号(即支付号)
			externalRefNumber.setText(request.getPaymentNo());
			if (StringUtils.isNotBlank(request.getAccountName())) {
				Element cardHolderName = getDynNumContent.addElement("cardHolderName");// 持卡人姓名
				cardHolderName.setText(request.getAccountName());
			}
			if (StringUtils.isNotBlank(request.getIdType())) {
				Element idType = getDynNumContent.addElement("idType");// 证件类型
				idType.setText(request.getIdType());// 证件类型转换
			}
			if (StringUtils.isNotBlank(request.getIdNo())) {
				Element cardHolderId = getDynNumContent.addElement("cardHolderId");// 持卡人身份证号
				cardHolderId.setText(request.getIdNo());
			}
			Element pan = getDynNumContent.addElement("pan");// 卡号
			pan.setText(request.getAccountNo());
			if (StringUtils.isNotBlank(request.getBankCode())) {
				Element bankId = getDynNumContent.addElement("bankId");// 银行代码
				bankId.setText(request.getBankCode());
			}
			if (StringUtils.isNotBlank(request.getExpiredMonth()) && StringUtils.isNotBlank(request.getExpiredYear())) {
				Element expiredDate = getDynNumContent.addElement("expiredDate");// 卡效期
				expiredDate.setText(request.getExpiredMonth()+request.getExpiredYear());
			}
			Element phoneNO = getDynNumContent.addElement("phoneNO");// 手机号码
			phoneNO.setText(request.getTelePhone());
			if (StringUtils.isNotBlank(request.getCvv2())) {
				Element cvv2 = getDynNumContent.addElement("cvv2");// 卡校验码
				cvv2.setText(request.getCvv2());
			}
			Element amount = getDynNumContent.addElement("amount");// 金额
			amount.setText(String.valueOf(payment.getPaymentAmount()));
			// 报文数据
			xmlContent = document.asXML();
			logger.info("getDynNumRequest：" + xmlContent);
		} catch (Exception e) {
			logger.error("封装快捷支付报文失败！", e);
		}
		// 返回XML报文数据
		return xmlContent;
	}

	public void setQuickPayUrl(String quickPayUrl) {
		this.quickPayUrl = quickPayUrl;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setQuickPayPassword(String quickPayPassword) {
		this.quickPayPassword = quickPayPassword;
	}

	public void setPro(Properties pro) {
		this.pro = pro;
	}

}
