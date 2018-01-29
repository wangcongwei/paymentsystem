package com.newtouch.payment.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import com.newtouch.common.view.JsonUtils;
import com.newtouch.payment.view.model.orderpay.OrderPayReqVo;
import com.newtouch.payment.view.model.orderquery.OrderQueryReqVo;
import com.newtouch.payment.view.model.quickorder.QuickOrderReqVo;
import com.newtouch.payment.view.model.quickorderSMS.QuickOrderSMSReqVo;
import com.newtouch.payment.view.model.quickorderpay.QuickOrderPayReqVo;

/**
 * 
 * @author xiangzhe.zeng
 * @date 2015年9月22日 20:23:36
 * @version V1.0
 */

public class HttpClientTest {

	private static Header[] headers = {
			new BasicHeader("Host", "localhost:8080"),
			new BasicHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0"),
			new BasicHeader("Accept", "application/json, text/plain, */*"),
			new BasicHeader("Accept-Language",
					"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"),
			new BasicHeader("Accept-Encoding", "gzip, deflate"),
			new BasicHeader("Content-Type", "application/json;charset=utf-8"),

			// new BasicHeader("Content-Length","116"),
			new BasicHeader("Connection", "keep-alive"),
			new BasicHeader("Pragma", "no-cache"),
			new BasicHeader("Cache-Control", "no-cache") };

	public static void main(String[] args) throws Exception {
//		String str = "{\"timestamp\":1442886471933,\"content\":{\"startTime$$type\":\"String\",\"endTime$$type\":\"String\",\"orderNo$$type\":\"String\"}}";
		String url = "http://localhost:8080/payment-platform/orderpay/netpay/orderPay";
		try{
			//测试订单支付
//			String testOrderPay = "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testOrderPay()+"}";
//			String html =postString(testOrderPay,url);
//			System.out.println(html);
//			//创建快捷支付支付订单
//			url = "http://localhost:8080/payment-platform/orderpay/netpay/quickOrder";
//			String testDoQuickOrder = "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testDoQuickOrder()+"}";
//			System.out.println(postString(testDoQuickOrder,url));
//			//获取快捷支付验证码
			url = "http://localhost:8080/payment-platform/orderpay/netpay/quickOrderSMS";
			String testDoQuickOrderSMS = "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testDoQuickOrderSMS()+"}";
			System.out.println(postString(testDoQuickOrderSMS,url));
//			//进行快捷支付  
//			url = "http://localhost:8080/payment-platform/orderpay/netpay/quickOrderPay";
//			String testDoQuickOrderPay = "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testDoQuickOrderPay()+"}";
//			System.out.println(postString(testDoQuickOrderPay,url));
//			//订单列表查询
//			url = "http://localhost:8080/payment-platform/orderpay/netpay/orderListQuery";
//			String testOrderListQuery= "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testOrderListQuery()+"}";
//			System.out.println(postString(testOrderListQuery,url));
//			//订单状态查询
//			url = "http://localhost:8080/payment-platform/orderpay/netpay/orderQuery";
//			String testOrderQuery= "{\"timestamp\":"+new Date().getTime()+",\"content\":"+testOrderListQuery()+"}";
//			System.out.println(postString(testOrderQuery,url));
		
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}

	/**
	 * 测试订单支付
	 * @return
	 */
	public static String testOrderPay(){
		String json = "";
		OrderPayReqVo orderPayReqVo = new OrderPayReqVo();
		orderPayReqVo.setOrderDetail("测试"+new Date().getTime());
		orderPayReqVo.setAmount(new BigDecimal(new Date().getTime()+""));
		orderPayReqVo.setBusNum("99");
		orderPayReqVo.setBusOrderNo(new Date().getTime()+"");
		json = JsonUtils.toJson(orderPayReqVo);
		return  json;
	}
	

	/**
	 * 创建快捷支付支付订单
	 * @param orderPayReqVo
	 * @return
	 */
	public static String testDoQuickOrder(){
		String json = "";
		QuickOrderReqVo quickOrderReqVo= new QuickOrderReqVo();
		quickOrderReqVo.setAmount(new BigDecimal(new Date().getTime()+""));
		quickOrderReqVo.setBusNum("99");
		quickOrderReqVo.setBusOrderNo(new Date().getTime()+"");
		json = JsonUtils.toJson(quickOrderReqVo);
		return  json;
		
	}
	
	/**
	 * 获取快捷支付验证码
	 * @param orderPayReqVo
	 * @return
	 */
	public static String testDoQuickOrderSMS(){
		String json = "";
		QuickOrderSMSReqVo quickOrderSMSReqVo= new QuickOrderSMSReqVo();
		quickOrderSMSReqVo.setAmount(new BigDecimal(new Date().getTime()+""));
		quickOrderSMSReqVo.setBusNum("99");
		quickOrderSMSReqVo.setBusOrderNo(new Date().getTime()+"");
		quickOrderSMSReqVo.setOrderNo("c3ef985a79c24223991aed11213bb703");
		quickOrderSMSReqVo.setCardType("1");
		quickOrderSMSReqVo.setCvv2("090");
		quickOrderSMSReqVo.setUserAccount("622505732222");
		quickOrderSMSReqVo.setUserCerNum("36072311111111");
		quickOrderSMSReqVo.setUserCerType("0");
		quickOrderSMSReqVo.setUserMobile("13564882039");
		quickOrderSMSReqVo.setUserName("userName"+new Date().getTime());
		json = JsonUtils.toJson(quickOrderSMSReqVo);
		return  json;
		
	}
	
	
	/**
	 * 进行快捷支付
	 * @param orderPayReqVo
	 * @return
	 */
	public static String testDoQuickOrderPay(){
		String json = "";
		QuickOrderPayReqVo quickOrderPayReqVo = new QuickOrderPayReqVo();
		quickOrderPayReqVo.setAmount(new BigDecimal(new Date().getTime()+""));
		quickOrderPayReqVo.setBusNum("99");
		quickOrderPayReqVo.setBusOrderNo("1443098293827");
		quickOrderPayReqVo.setCardType("1");
		quickOrderPayReqVo.setCvv2("090");
		quickOrderPayReqVo.setUserAccount("622505732222");
		quickOrderPayReqVo.setUserCerNum("36072311111111");
		quickOrderPayReqVo.setUserCerType("0");
		quickOrderPayReqVo.setUserMobile("13566666666");
		quickOrderPayReqVo.setUserName("userName"+new Date().getTime());
		quickOrderPayReqVo.setOrderNo("f9a7fddd24514220a268cf15614f49c7");
		json = JsonUtils.toJson(quickOrderPayReqVo);
		return  json;
	}
	
	/**
	 * 进行快捷支付
	 * @param orderPayReqVo
	 * @return
	 */
	public static String testOrderListQuery(){
		String json = "{\"timestamp\":1443151716591,\"content\":{\"createTime$$type\":\"Date\",\"endTime$$type\":\"Date\",\"orderNo$$type\":\"String\",\"busNum$$type\":\"String\",\"orderStatus$$type\":\"String\",\"createTime\":\"2015-09-01\",\"endTime\":\"2015-09-25\",\"orderNo\":\"1\",\"busNum\":\"99\",\"orderStatus\":\"1\"}}";
		return  json;
		
	}
	/**
	 * 进行快捷支付
	 * @param orderPayReqVo
	 * @return
	 */
	public static String testOrderQuery(){
		String json = "";
		OrderQueryReqVo orderQueryReqVo = new OrderQueryReqVo();
		orderQueryReqVo.setBusOrderNo("8aff42bfae7e466681cedcae642d6019");
		orderQueryReqVo.setBusNum("99");
		json = JsonUtils.toJson(orderQueryReqVo);
		return  json;
	}
	
	public static String postString(String json,String url) throws Exception{
		HttpClient httpClient = HttpClientBuilder.create().build();
		// HttpPatch httpPost = new HttpPatch(url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeaders(headers);
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("data",str));
		// httpPost.setEntity(new UrlEncodedFormEntity(params));
		StringEntity params = null;
		params = new StringEntity(json, "UTF-8");
		// httpPost.addHeader("charset",charset);
		httpPost.setEntity(params);
		System.out.println("executing: " + httpPost.getRequestLine());

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity responseEntity = response.getEntity();
		String responseXml = inputStream2String(responseEntity.getContent());
		if (responseEntity != null) {
			System.out.println("Response content: "+ responseXml);
		}
		return responseXml;
	}
	
	
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

}
