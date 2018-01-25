package com.newtouch.common.view.validation;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.StringUtils;

import com.newtouch.common.model.ValidationFieldRule;
import com.newtouch.common.view.ReqModel;
/**
 * 请求校验
 * @author dongfeng.zhang
 *
 */
public class ValidateUtil {

	public static Map<String, String> validate(ReqModel reqModel, List<ValidationFieldRule> validations) {
		Map<String, String> errs = new LinkedHashMap<String, String>();
		if (validations == null || validations.size() == 0)
			return errs;
		
		Object content = reqModel.getContent();
		for (ValidationFieldRule v : validations) {
			
			String value = null;
			boolean pass = true;
			try {
				value = BeanUtils.getProperty(content, v.getName());
			} catch (Exception e) {
				pass = false;
			}
			pass = pass && vali(value, v.getType(), v);
			if (!pass) {
				errs.put(v.getName(), v.getErrMsg());
			}
		}

		return errs;
	}

	private static boolean vali(String value, String type, ValidationFieldRule v) {
		boolean pass = true;
		pass = checkRequird(value, v.isRequired());// 可选或必填有值
		if (pass && (value == null)) {// null值无需后续校验
			return true;
		} else {
			// 非空值继续校验
			pass = pass
					&& (value != null && checkType(value, type) && checkLen(value, v.getMinLen(), v.getMaxLen())
							&& checkRange(value, type, v.getRangeStart(), v.getRangeEnd()) && checkPattern(value, buildPattern(v)));
			return pass;
		}
	}

	/*
	 * email url pin mobile-number phone-number integer long float float-2 float-4 date time datetime ip letter uppercase lowercase
	 */
	private static String buildPattern(ValidationFieldRule v) {
		if (StringUtils.hasText(v.getPattern())) {
			return v.getPattern();
		}
		if ("email".equals(v.getType())) {
			return "^([a-z0-ArrayA-Z]+[-|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$";
		} else if ("url".equals(v.getType())) {
			return "^(https?|ftp|file|https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		} else if ("pin".equals(v.getType())) {
			return "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9xX])";
		} else if ("mobile-number".equals(v.getType())) {
			return "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		} else if ("phone-number".equals(v.getType())) {
			return "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
		} else if ("integer".equals(v.getType())) {
			return "^\\+{0,1}[1-9]\\d*";
		} else if ("float".equals(v.getType())) {
			return "^((0)|([1-9][0-9]*))\\.[0-9]+$";
		} else if ("float-2".equals(v.getType())) {
			return "^((0)|([1-9][0-9]*))\\.[0-9]{2}";
		} else if ("float-4".equals(v.getType())) {
			return "^((0)|([1-9][0-9]*))\\.[0-9]{4}";
		} else if ("date".equals(v.getType())) {
			return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		} else if ("time".equals(v.getType())) {
			return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		} else if ("datetime".equals(v.getType())) {
			return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		} else if ("ip".equals(v.getType())) {
			return "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		} else if ("letter".equals(v.getType())) {
			return "^[a-zA-Z_\\-0-9]+$";
		} else if ("uppercase".equals(v.getType())) {
			return "^[A-Z]+$";
		} else if ("lowercase".equals(v.getType())) {
			return "^[a-z]+$";
		}
		return "";
	}

	private static boolean checkRange(String value, String type, String rangeStart, String rangeEnd) {
		if (StringUtils.hasText(rangeStart) && StringUtils.hasText(rangeEnd)) {
			if ((value.hashCode() > rangeStart.hashCode() && value.hashCode() < rangeEnd.hashCode()) || value.hashCode() == rangeStart.hashCode()
					|| value.hashCode() == rangeEnd.hashCode()) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

	private static boolean checkPattern(String value, String pattern) {
		if (StringUtils.hasText(pattern)) {
			Pattern patterns = Pattern.compile(pattern);
			Matcher matcher = patterns.matcher(value);
			return matcher.matches();
		} else {
			return true;
		}

	}

	private static boolean checkLen(String value, int i, int j) {
		if (StringUtils.hasText(value)) {
			if (value.length() >= i && value.length() <= j) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean checkRequird(String value, boolean requird) {
		if (requird) {
			if (StringUtils.hasText(value)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean checkType(Object value, String type) {
		try {
			if ("long".equals(type)) {
				ConvertUtils.convert(value, Long.class);
			} else if ("int".equals(type)) {
				ConvertUtils.convert(value, Integer.class);
			} else if ("time".equals(type)) {
				ConvertUtils.convert(value, Time.class);
			} else if ("date".equals(type)) {
				ConvertUtils.convert(value, Date.class);
			} else if ("datetime".equals(type)) {
				ConvertUtils.convert(value, Timestamp.class);
			} else if ("double".equals(type)) {
				ConvertUtils.convert(value, Double.class);
			} else if ("integer".equals(type)) {
				ConvertUtils.convert(value, Integer.class);
			} else if ("float".equals(type)) {
				ConvertUtils.convert(value, Float.class);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
