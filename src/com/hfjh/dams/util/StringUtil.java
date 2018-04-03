package com.hfjh.dams.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @instructions: 包含了常用的字符串的处理类
 * @version:
 */
public class StringUtil {

	/**
	 * @instructs:判断str1与str2在trimToEmpty后是否相等
	 * @param str1
	 * @param str2
	 * @return true false
	 */

	public static boolean equalsOfEmpty(String str1, String str2) {
		return StringUtils.trimToEmpty(str1).equals(
				StringUtils.trimToEmpty(str2));
	}

	/**
	 * @instructs: 字符串数组[]转list
	 * @param array
	 * @return
	 */
	public static List<String> arrayToList(String[] array) {
		return Arrays.asList(array);
	}

	/**
	 * @instructs: obj阿拉伯数字转大写
	 * @param obj
	 * @return
	 */
	public static String objToCapital(Object obj) {
		if ("".equals(toString(obj))
				|| NumberUtil.toInteger(toString(obj)) > 9)
			return null;

		String[] b = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		return b[Integer.parseInt(toString(obj))];
	}

	/**
	 * @instructs: obj转String 字符串"null"视为无效
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null || obj.toString().equalsIgnoreCase("null")) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}
	public static String toStringIgnoreNull(Object obj) {
		return obj == null?"":obj.toString().trim();	
	}

	public static String toString(Object obj,String defaultValue) {
		String s = "";
		if(obj != null) {
			s = obj.toString().trim();
		}
		return s.isEmpty()? toString(defaultValue) : s;
	}

	/**
	 * @instructs:字符串数组[]转字符串，无分隔符
	 * @param str
	 * @return
	 */
	public static String arrayToString(String[] array) {
		return arrayToString(array, "");
	}

	/**
	 * @instructs: 字符串数组[]转字符串，用特定分隔符
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String arrayToString(String[] array, String separator) {
		if (array == null || array.length < 1) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(",").append(separator).append(array[i]).append(separator);
		}
		return sb.substring(1).toString();

	}

	/**
	 * @instructs: 字符串转字符串数组[]，用","作为分隔符
	 * @param array
	 * @return
	 */
	public static String[] stringToArray(String str) {
		return stringToArray(str, ",");

	}

	/**
	 * @instructs: 字符串转字符串数组[]，用特定分隔符
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String[] stringToArray(String str, String separator) {
		str = toString(str);
		StringTokenizer st = new StringTokenizer(str, separator);
		String[] strArr = { "", "", "", "" };
		for (int i = 0; st.hasMoreTokens(); i++) {
			strArr[i] = st.nextToken();
		}
		return strArr;
	}

	/**
	 * @instructs:从字符串尾部截取指定的字符串
	 * @param sourceStr
	 *            源字符串
	 * @param str
	 *            截取字符串
	 * @return
	 */
	public static String trimStringOfStart(String sourceStr, String str) {
		if (sourceStr.endsWith(str)) {
			sourceStr = sourceStr.substring(0, sourceStr.length()
					- str.length());
		}
		return sourceStr;
	}

	/**
	 * @instructs:从字符串头部截取指定的字符串
	 * @param sourceStr
	 *            源字符串
	 * @param str
	 *            截取字符串
	 * @return
	 */

	public static String trimStringOfEnd(String sourceStr, String str) {
		if (sourceStr.startsWith(str)) {
			sourceStr = sourceStr.substring(str.length(), sourceStr.length());
		}
		return sourceStr;
	}

	/**
	 * @instructs:按照str生成指定重复个数的字符串
	 * @param str
	 * @param length
	 * @return
	 */
	public static String repeatString(String str, int length) {
		if (str == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * @instructs:判断数组是否包含指定字符串
	 * @param array
	 * @param string
	 * @return true false
	 */
	public static boolean isArrayExistString(String[] array, String str) {
		return arrayToList(array).contains(str);
	}
	
	/**
	 * 替换指定的字符串内容
	 * @param sourceString
	 * @param object
	 * @return
	 */
	public static String replace(final String sourceString,Object... object) {
		String temp = sourceString;
		for (int i = 0; i < object.length; i++) {
			if (object[i] != null) {
				Pattern pattern = Pattern.compile("\\{" + i + "\\}");
				Matcher matcher = pattern.matcher(temp);
				temp = matcher.replaceAll(object[i].toString());
			}
		}
		return temp;
	}
	public static String removeBlank(String source) {
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(source);
		return matcher.replaceAll("");
	}

	/** 生成符合UUID字符序列.
	 * @return
	 */
	public static String getUUID(){
		String uuid = UUID.randomUUID().toString();
		//去'-'
		return uuid.replaceAll("-", "");
	}
	/** 生成符合UUID字符序列.
	 * @return
	 */
	public static String getUUIDWithHyphen(){
		return UUID.randomUUID().toString();
	}
	
	public static boolean isNotBlank(String str) {
		return false == isBlank(str);
	}
	public static boolean isBlank(String str) {
		return null == str || str.trim().length() == 0;
	}
}
