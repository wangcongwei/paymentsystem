/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-21 下午5:05:50 MjunLee
 */
package com.newtouch.payment.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: NewTouch
 * </p>
 * 
 * @author MjunLee
 * @version 1.0
 */
public class StreamUtil {
	/**
	 * 输入流转换为字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String changeISToStr(InputStream is) throws IOException {
		int i = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		if (is != null) {
			is.close();
		}
		if (baos != null) {
			baos.close();
		}
		return baos.toString();
	}

	/**
	 * 输入流转换为指定字符集的字符串
	 * 
	 * @param is
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String changeISToStr(InputStream is, String charset) throws IOException {
		int i = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		if (is != null) {
			is.close();
		}
		if (baos != null) {
			baos.close();
		}
		return baos.toString(charset);
	}
}
