package com.newtouch.payment.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ccic.eb.service.paycallback.PayCallBackProxyService;

public class PayCallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 6502711264301185866L;

	private PayCallBackProxyService payCallBackProxyService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		payCallBackProxyService = (PayCallBackProxyService) WebApplicationContextUtils.getWebApplicationContext(this.getServletContext()).getBean("payCallBackProxyService");
		payCallBackProxyService.callBackPayment(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
