
package com.newtouch.payment.exception;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Newtouch
 * </p>
 * 
 * @version 1.0
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 333439073916654843L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable e) {
		super(e);
	}

	public ServiceException(String message, Throwable e) {
		super(message, e);
	}

}
