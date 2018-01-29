package com.newtouch.payment.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.OrderPayRequest;
import com.newtouch.payment.model.TPayPlatformTransation;
import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;
import com.newtouch.payment.repository.OrderPayRequestRepo;
import com.newtouch.payment.repository.OrderRepo;
import com.newtouch.payment.service.KuaiqianGateWayService;
import com.newtouch.payment.utils.MD5Util;

@Service(value="kuaiqianGateWayService")
public class KuaiqianGateWayServiceImpl implements KuaiqianGateWayService {

	private static final Logger logger = LoggerFactory.getLogger(KuaiqianGateWayServiceImpl.class);
	private static Properties pro;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private OrderPayRequestRepo orderPayRequestRepo;

	@Override
	public KuaiqianGateWayResponseDTO gateWayPay(String userTransactionNo, String bankNo) {

		KuaiqianGateWayResponseDTO response = new KuaiqianGateWayResponseDTO();

		Date now = new Date();
		// 根据支付流水好查询订单信息
		QueryParams queryParams = new QueryParams();
		queryParams.put("paySeriNo", userTransactionNo);
		OrderPayRequest orderPayRequest = orderPayRequestRepo.findByPaySeriNo(OrderPayRequest.class, queryParams);
		queryParams.put("orderNo", orderPayRequest.getOrderNo());
		Order order = orderRepo.findByOrderNo(Order.class, queryParams);

		// 创建支付交易记录
		TPayPlatformTransation tppt = new TPayPlatformTransation();
		tppt.setCreateDate(now);
		tppt.setAmount(order.getAmount().doubleValue());
		tppt.setOrderNo(order.getOrderNo());
		tppt.setPayType(pro.getProperty("payType"));
		tppt.setPayTime(new Date());
		tppt.setReqNo(userTransactionNo);
		tppt.setStatus("CREATE");

		// 人民币网关账户号
		/// 请登录快钱系统获取用户编号，用户编号后加01即为人民币网关账户号。
		String merchantAcctId = pro.getProperty("merchatAcctId").trim() + "01"; // "1000300079901";

		// 人民币网关密钥
		/// 区分大小写.请与快钱联系索取
		String key = pro.getProperty("key"); // "GQWCL37T2IYEBYGF";

		// 字符集.固定选择值。可为空。
		/// 只能选择1、2、3.
		/// 1代表UTF-8; 2代表GBK; 3代表gb2312
		/// 默认值为1
		String inputCharset = "1";

		// 接受支付结果的页面地址.与[bgUrl]不能同时为空。必须是绝对地址。
		/// 如果[bgUrl]为空，快钱将支付结果Post到[pageUrl]对应的地址。
		/// 如果[bgUrl]不为空，并且[bgUrl]页面指定的<redirecturl>地址不为空，则转向到<redirecturl>对应的地址
		String pageUrl = "";

		// 服务器接受支付结果的后台地址.与[pageUrl]不能同时为空。必须是绝对地址。
		/// 快钱通过服务器连接的方式将交易结果发送到[bgUrl]对应的页面地址，在商户处理完成后输出的<result>如果为1，页面会转向到<redirecturl>对应的地址。
		/// 如果快钱未接收到<redirecturl>对应的地址，快钱将把支付结果post到[pageUrl]对应的页面。
		String bgUrl = pro.getProperty("bgUrl"); // "http://www.yoursite.com/receive.jsp";

		// 网关版本.固定值
		/// 快钱会根据版本号来调用对应的接口处理程序。
		/// 本代码版本号固定为v2.0 // v2.1.1(上海)
		String version = "v2.0";

		// 语言种类.固定选择值。
		/// 只能选择1、2、3
		/// 1代表中文；2代表英文
		/// 默认值为1
		String language = "1";

		// 签名类型.固定值
		/// 1代表MD5签名
		/// 当前版本固定为1
		String signType = "1";

		// 支付人姓名
		/// 可为中文或英文字符
		String payerName = "";

		// 支付人联系方式类型.固定选择值
		/// 只能选择1
		/// 1代表Email
		String payerContactType = "1";

		// 支付人联系方式
		/// 只能选择Email或手机号
		String payerContact = "";

		// 商户订单号
		/// 由字母、数字、或[-][_]组成
		String orderId = order.getOrderNo();

		// 订单金额
		/// 以分为单位，必须是整型数字
		/// 比方2，代表0.02元
		String doubleAmount = order.getAmount().multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP)
				.toString();
		// if(doubleAmount.length() - doubleAmount.indexOf(".") -1 > 2){
		// doubleAmount = doubleAmount.substring(0, doubleAmount.indexOf(".")) +
		// doubleAmount.substring(doubleAmount.indexOf(".")+1,doubleAmount.indexOf(".")+3);
		// }else if(doubleAmount.length() - doubleAmount.indexOf(".") -1 == 2){
		// doubleAmount = doubleAmount.substring(0, doubleAmount.indexOf(".")) +
		// doubleAmount.substring(doubleAmount.indexOf(".")+1,doubleAmount.length());
		// }else{
		// doubleAmount = doubleAmount.substring(0, doubleAmount.indexOf(".")) +
		// doubleAmount.substring(doubleAmount.indexOf(".")+1,doubleAmount.length())+"0";
		// }
		// while(doubleAmount.startsWith("0")){
		// doubleAmount = doubleAmount.substring(1,doubleAmount.length());
		// }

		String orderAmount = doubleAmount;

		// 订单提交时间
		/// 14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
		/// 如；20080101010101
		String orderTime = DateFormatUtils.format(now, CommonConst.datePatternyyyyMMddHHmmss);

		// 商品名称
		/// 可为中文或英文字符
		String productName = "";

		// 商品数量
		/// 可为空，非空时必须为数字
		String productNum = "";

		// 商品代码
		/// 可为字符或者数字
		String productId = "";

		// 商品描述
		String productDesc = "";

		// 扩展字段1
		/// 在支付结束后原样返回给商户
		String ext1 = userTransactionNo;

		// 扩展字段2
		String ext2 = "";
		// 支付方式.固定选择值
		/// 只能选择00、10、11、12、13、14
		/// 00：组合支付（网关支付页面显示快钱支持的各种支付方式，推荐使用）10：银行卡支付（网关支付页面只显示银行卡支付）.11：电话银行支付（网关支付页面只显示电话支付）.12：快钱账户支付（网关支付页面只显示快钱账户支付）.13：线下支付（网关支付页面只显示线下支付方式）.14：B2B支付（网关支付页面只显示B2B支付，但需要向快钱申请开通才能使用）
		String payType = pro.getProperty("payType");

		// 银行代码
		/// 实现直接跳转到银行页面去支付,只在payType=10时才需设置参数
		/// 具体代码参见 接口文档银行代码列表
		String bankId = bankNo;

		// 同一订单禁止重复提交标志
		/// 固定选择值： 1、0
		/// 1代表同一订单号只允许提交1次；0表示同一订单号在没有支付成功的前提下可重复提交多次。默认为0建议实物购物车结算类商户采用0；虚拟产品类商户采用1
		String redoFlag = "0";

		// 快钱的合作伙伴的账户号
		/// 如未和快钱签订代理合作协议，不需要填写本参数
		String pid = "";

		// 生成加密签名串
		/// 请务必按照如下顺序和规则组成加密串！
		String signMsgVal = "";
		signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
		signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
		signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
		signMsgVal = appendParam(signMsgVal, "version", version);
		signMsgVal = appendParam(signMsgVal, "language", language);
		signMsgVal = appendParam(signMsgVal, "signType", signType);
		signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
		signMsgVal = appendParam(signMsgVal, "payerName", payerName);
		signMsgVal = appendParam(signMsgVal, "payerContactType", payerContactType);
		signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
		signMsgVal = appendParam(signMsgVal, "orderId", orderId);
		signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
		signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
		signMsgVal = appendParam(signMsgVal, "productName", productName);
		signMsgVal = appendParam(signMsgVal, "productNum", productNum);
		signMsgVal = appendParam(signMsgVal, "productId", productId);
		signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
		signMsgVal = appendParam(signMsgVal, "ext1", ext1);
		signMsgVal = appendParam(signMsgVal, "ext2", ext2);
		signMsgVal = appendParam(signMsgVal, "payType", payType);

		if ("10".equals(payType)) {
			signMsgVal = appendParam(signMsgVal, "bankId", bankId);
		}

		signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
		signMsgVal = appendParam(signMsgVal, "pid", pid);
		signMsgVal = appendParam(signMsgVal, "key", key);

		logger.info("加密前的字符串 before md5 log :" + signMsgVal.toString());

		String signMsg = "";
		try {
			signMsg = MD5Util.md5Hex(signMsgVal.getBytes("UTF-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.info("加密失败:" + e);
		}
		logger.info("signMsg's value is :" + signMsg);

		// 生成支付请求参数，发送到快钱。
		Map<String, String> params = new HashMap<String, String>();
		params.put("inputCharset", inputCharset);
		params.put("bgUrl", bgUrl);
		params.put("pageUrl", pageUrl);
		params.put("version", version);
		params.put("language", language);
		params.put("signType", signType);
		params.put("merchantAcctId", merchantAcctId);
		params.put("payerName", payerName);
		params.put("payerContactType", payerContactType);
		params.put("payerContact", payerContact);
		params.put("orderId", orderId);
		params.put("orderAmount", orderAmount);
		params.put("orderTime", orderTime);
		params.put("productName", productName);
		params.put("productNum", productNum);
		params.put("productId", productId);
		params.put("productDesc", productDesc);
		params.put("ext1", ext1);
		params.put("ext2", ext2);
		params.put("payType", payType);
		params.put("redoFlag", redoFlag);
		params.put("pid", pid);
		if ("10".equals(payType)) {
			params.put("bankId", bankId);
		}
		params.put("bankId", bankId);
		params.put("signMsg", signMsg);

		response.setUrl(pro.getProperty("kuaiqianGateWayUrl"));
		response.setParams(params);

		return response;
	}

	// 功能函数。将变量值不为空的参数组成字符串
	private String appendParam(String returnStr, String paramId, String paramValue) {
		if (paramValue == null) {
			paramValue = "";
		}

		if (!returnStr.equals("")) {
			if (!paramValue.equals("")) {
				returnStr = returnStr + "&" + paramId + "=" + paramValue;
			}
		} else {
			if (!paramValue.equals("")) {
				returnStr = paramId + "=" + paramValue;
			}
		}
		return returnStr;
	}

	public static void setPro(Properties pro) {
		KuaiqianGateWayServiceImpl.pro = pro;
	}

}
