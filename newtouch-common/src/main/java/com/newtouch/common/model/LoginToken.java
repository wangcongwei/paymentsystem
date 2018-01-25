package com.newtouch.common.model;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义TOKEN
 * 
 * @author dongfeng.zhang
 * */
public class LoginToken extends UsernamePasswordToken {
	private static final long serialVersionUID = -8463564344376467783L;
	private String sysType;// 系统类型
	private boolean thirdPartyLogin;
	private Long userId;

	public LoginToken(final String username, final String password, final String sysType, final boolean thirdPartyLogin) {
		super(username, password, true);
		this.sysType = sysType;
		this.thirdPartyLogin = thirdPartyLogin;
	}

	public LoginToken(final Long userId, final String username, final String sysType, boolean thirdPartyLogin) {
		super(username, "");
		this.userId = userId;
		this.sysType = sysType;
		this.thirdPartyLogin = thirdPartyLogin;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public boolean isThirdPartyLogin() {
		return thirdPartyLogin;
	}

	public void setFromP13(boolean fromP13) {
		this.thirdPartyLogin = fromP13;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
