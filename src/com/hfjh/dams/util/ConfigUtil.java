package com.hfjh.dams.util;

import java.util.ResourceBundle;

/**
 * Description:项目参数工具类
 * @author wugj
 * @version 2017-3-7 上午11:21:17
 */
public final class ConfigUtil {
	private ConfigUtil() {}
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("param");
	
	public static void main(String[] args) {
		String aa = ConfigUtil.getString("md5.salt");
		System.out.println(aa);
	}
	/**
	 * 通过key获取对应的值
	 * @Title: get  
	 * @param @param key
	 * @param @return      
	 * @return Object     
	 * @throws
	 */
	public static final Object get(String key){
		return bundle.getObject(key);
	}
	
	/**
	 * 获取String类型的值
	 * @Title: getString  
	 * @param @param key
	 * @param @return      
	 * @return String     
	 * @throws
	 */
	public static final String getString(String key) {
		return get(key) != null ? String.valueOf(get(key)) : null;
	}

	public static String getString(String key, String defaultValue) {
		try {
			return get(key) != null ? getString(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}
	/**
	 * 获取boolean类型的值
	 * @Title: getBoolean  
	 * @Description: TODO 
	 * @param @param key
	 * @param @return      
	 * @return Boolean     
	 * @throws
	 */
	public static Boolean getBoolean(String key) {
		return get(key) != null ? Boolean.valueOf(getString(key)) : false;
	}

	public static Boolean getBoolean(String key, Boolean defaultValue) {
		try {
			return get(key) != null ? getBoolean(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}
	/**
	 * 获取short类型的值
	 * @Title: getShort  
	 * @Description: TODO 
	 * @param @param key
	 * @param @return      
	 * @return Short     
	 * @throws
	 */
	public static Short getShort(String key) {
		return get(key) != null ? Short.valueOf(getString(key)) : null;
	}

	public static Short getShort(String key, Short defaultValue) {
		try {
			return get(key) != null ? getShort(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}
	/**
	 * 获取int类型的值
	 * @Title: getInt  
	 * @param @param key
	 * @param @return      
	 * @return int     
	 * @throws
	 */
	public static final Integer getInt(String key){
		return  get(key) != null ? Integer.valueOf(getString(key)) : null;
	}

	public static Integer getInt(String key, Integer defaultValue) {
		try {
			return get(key) != null ? getInt(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取Long类型的值
	 * @Title: getLong  
	 * @Description: TODO 
	 * @param @param key
	 * @param @return      
	 * @return Long     
	 * @throws
	 */
	public static Long getLong(String key) {
		return get(key) != null ? Long.valueOf(getString(key)) : null;
	}

	public static Long getLong(String key, Long defaultValue) {
		try {
			return get(key) != null ? getLong(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取Float类型的值
	 * @Title: getFloat  
	 * @Description: TODO 
	 * @param @param key
	 * @param @return      
	 * @return Float     
	 * @throws
	 */
	public static Float getFloat(String key) {
		return get(key) != null ? Float.valueOf(getString(key)) : null;
	}

	public static Float getFloat(String key, Float defaultValue) {
		try {
			return get(key) != null ? getFloat(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取double类型的值
	 * @Title: getDouble  
	 * @Description: TODO 
	 * @param @param key
	 * @param @return      
	 * @return Double     
	 * @throws
	 */
	public static Double getDouble(String key) {
		return get(key) != null ? Double.valueOf(getString(key)) : null;
	}

	public static Double getDouble(String key, Double defaultValue) {
		try {
			return get(key) != null ? getDouble(key) : defaultValue;
		} catch (Exception e) {
		}
		return defaultValue;
	}
}
