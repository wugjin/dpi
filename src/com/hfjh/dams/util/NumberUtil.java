package com.hfjh.dams.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 * @instructions: 包含了Integer、double、Object与String转换处理类
 * @version:
 */
public class NumberUtil {

	/**
	 * 日志信息
	 */
	private static Logger logger = Logger.getLogger(NumberUtil.class);

	/**
	 * @instructs:字符串转Integer
	 * @param str
	 *            待转换的字符串
	 * @param defaultValue
	 *            空字符串时的默认返回值
	 * @return
	 */
	public static Integer toInteger(String str, Integer defaultValue) {
		Integer result;
		// 空字符串
		if (StringUtils.isBlank(str)) {
			result = defaultValue;
		} else {
			try {
				result = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				logger.error("对不起！字符串转整型错误！", e);
				result = defaultValue;
				return result;
			}
		}
		return result;
	}

	/**
	 * @instructs:字符串转Integer
	 * @param str
	 *            待转换的字符串
	 * @return
	 */
	public static Integer toInteger(String str) {
		return toInteger(str, null);
	}

	/**
	 * @instructs: 字符串转Double型。
	 * @param str
	 *            待转换的数字
	 * @param defaultValue
	 *            空字符串时的默认返回值
	 * @return
	 */
	public static Double toDouble(String str, Double defaultValue) {
		Double result;
		if (StringUtils.isBlank(str)) {
			result = defaultValue;
		} else {
			try {
				result = Double.valueOf(str);
			} catch (NumberFormatException e) {
				logger.error("对不起！字符串转浮点型错误！", e);
				result = defaultValue;

			}
		}
		return result;
	}

	/**
	 * @instructs: 字符串转Double型。
	 * @param str
	 *            待转换的数字
	 * @param defaultValue
	 *            空字符串时的默认返回值
	 * @return
	 */
	public static Double toDouble(String str) {
		return toDouble(str, null);
	}

	/**
	 * @instructs:数字转字符串
	 * @param number
	 *            待转换的数字
	 * @param accuracy
	 *            精确度
	 * @return
	 */
	public static String numberToString(Object number, int accuracy) {
		if (number == null)
			return "";

		if (NumberUtils.isNumber(number.toString()) == false)
			return "无效数字";

		double dbnumber = Double.parseDouble(number.toString());
		return numberToString(dbnumber, accuracy);
	}

	/**
	 * @instructs:数字转字符串
	 * @param number
	 *            待转换的数字
	 * @param accuracy
	 *            精确度
	 * @return
	 */
	public static String numberToString(double number, int accuracy) {
		double dbnumber = number * Math.pow(10, accuracy);
		long lnumber = Math.round(dbnumber);
		String str = Long.toString(lnumber);

		try {
			if (accuracy > 0) {
				str = StringUtils.leftPad(str, accuracy + 1, "0");
				int len = str.length() - accuracy;
				str = str.substring(0, len) + "." + str.substring(len);
			} else if (accuracy < 0 && lnumber != 0) {
				for (int i = 0; i > accuracy; i--) {
					str = str + "0";
				}
			}
		} catch (Exception e) {
			logger.error("对不起！数字转字符串错误！", e);
		}
		return str;
	}

	/**
	 * @instructs: 判断字符串是否全是数字
	 * @param str
	 * @return
	 */
	public static boolean isInt(String str) {
		if (StringUtils.isBlank(str))
			return false;

		int len = str.length();
		for (int i = 0; i < len; i++) {
			try {
				Integer.parseInt("" + str.charAt(i));
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return true;
	}
	/**
	 * 功能描述: 判断传入的字符串是否是合法的数字 输入参数: 被检验的字符串 输出值: 检查结果
	 */
	public static boolean isNumString(Object paraStr) {
		if(paraStr == null || paraStr.toString().isEmpty()) {
			return false;
		}	
		Pattern p = Pattern.compile("[0-9]*");
		if(p.matcher(paraStr.toString()).matches())
			return true;
		return false;
	}
	
	/**
	 * 转int,默认为0
	 */
	public static int toInt(Object obj) {
		return toInt(obj, 0);	
	}
	/**
	 * 转int
	 */
	public static int toInt(Object obj, int defaultValue) {
		if(obj == null || "".equals(obj.toString())) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}
}
