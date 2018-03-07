package com.newtouch.payment.view.paymentno;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.common.view.Views;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.model.ApplyDetails;
import com.newtouch.payment.model.AuthPro;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.repository.ApplyDetailsRepo;
import com.newtouch.payment.repository.AuthProRepo;
import com.newtouch.payment.repository.PaymentRepo;

@RequestMapping(value = "/paymentno")
@Controller
public class PaymentNoApplyController {
	private static Logger logger = LoggerFactory.getLogger(PaymentNoApplyController.class);
	
	@Autowired
	private AuthProRepo authProRepo;
	
	@Autowired
	private ApplyDetailsRepo applyDetailsRepo;
	
	@Autowired
	private PaymentRepo paymentRepo;
	
	/**
	 * ajax 支付号申请
	 * 
	 * @param reqModel
	 *            请求模型
	 * @return json数据
	 */
	@ResponseBody
	@RequestMapping(value = "/apply", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public RespModel paymentNoApply(@RequestBody ReqModel reqModel) {
		String paymentNoApply = reqModel.getContent().toString();
		logger.trace("paymentNoApply:",paymentNoApply);
		JSONObject jo = JSON.parseObject(paymentNoApply);
		String consumerId = jo.getString(CommonConst.CONSUMERID);
		QueryParams qp = new QueryParams();
		qp.put(CommonConst.CONSUMERID, consumerId);
		AuthPro ap = authProRepo.findAuthProByConsumerId(AuthPro.class, qp);
		if (ap == null) {
			return Views.getErrorModel("Request.NoData");
		}
		String sumAmount = jo.getString(CommonConst.SUMAMOUNT);
		JSONArray applyDetails = jo.getJSONArray(CommonConst.APPLYDETAILS);
		Date now = new Date();
		BigDecimal amountSum = new BigDecimal("0");
		for(int i=0;i<applyDetails.size();i++) {
			JSONObject applyDetail = applyDetails.getJSONObject(i);
			BigDecimal goodsPrice = new BigDecimal(applyDetail.getString(CommonConst.GOODSPRICE));
			BigDecimal goodsCount = new BigDecimal(applyDetail.getString(CommonConst.GOODSCOUNT));
			amountSum = amountSum.add(goodsPrice.multiply(goodsCount));
		}
		if (amountSum.compareTo(new BigDecimal(sumAmount)) != 0) {
			return Views.getErrorModel("Request.ValidateErr");
		}
		Payment payment = new Payment();
		payment.setCreateDate(now);
		payment.setPaymentStatus(PaymentStatus.PS_WAITPAY);
		payment.setPaymentNo("123456789");
		payment.setCheckNo("123456");
		payment.setPaymentAmount(Double.valueOf(sumAmount));
		payment.setComCode(jo.getString(CommonConst.COMCODE));
		try {
			if(StringUtils.isNotBlank(jo.getString(CommonConst.PAYEFFSTART))) {
				payment.setPayEffStart(DateUtils.parseDate(jo.getString(CommonConst.PAYEFFSTART), new String[]{CommonConst.datePatternYYYYMMDDHHMMSS}));
			}
			if(StringUtils.isNotBlank(jo.getString(CommonConst.PAYEFFEND))) {
				payment.setPayEffEnd(DateUtils.parseDate(jo.getString(CommonConst.PAYEFFEND), new String[]{CommonConst.datePatternYYYYMMDDHHMMSS}));
			}
		} catch (ParseException e) {
			return Views.getErrorModel("Request.ValidateErr");
		}
		payment.setCurrency(CommonConst.CURRENCY);
		payment.setIsvalid(CommonConst.CONST_ONE);
		paymentRepo.save(payment);
		for(int i=0;i<applyDetails.size();i++) {
			JSONObject applyDetail = applyDetails.getJSONObject(i);
			ApplyDetails ads = new ApplyDetails();
			ads.setCreateDate(now);
			ads.setGoodsCode(applyDetail.getString(CommonConst.GOODSCODE));
			ads.setGoodsDesc(applyDetail.getString(CommonConst.APPLYDETAILS));
			ads.setGoodsPrice(Double.valueOf(applyDetail.getString(CommonConst.GOODSPRICE)));
			ads.setGoodsCount(applyDetail.getString(CommonConst.GOODSCOUNT));
			ads.setPayment(payment);
			applyDetailsRepo.save(ads);
		}
		RespModel rm = new RespModel();
		JSONObject paymentMessage = new JSONObject();
		paymentMessage.put(CommonConst.PAYMENTNO, payment.getPaymentNo());
		paymentMessage.put(CommonConst.CHECKNO, payment.getCheckNo());
		rm.setContent(paymentMessage);
		return rm;
	}

}
