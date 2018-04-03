package com.hfjh.dams.util;

import java.util.HashMap;
import java.util.Map;

public class HTMLConverUtil {
	public final static Map<String, String> HTML_CHAR = new HashMap<String, String>();   
	static {   
		HTML_CHAR.put("\"", "'");
		HTML_CHAR.put("\n","");
		HTML_CHAR.put("\r","");
	}   
	public static final String toHTMLChar(String str) {   
		if (str == null) {   
			return new StringBuilder().toString();   
		}          
		StringBuilder sb = new StringBuilder(str);   
	
		char tempChar;   
		String tempStr;   
		for (int i = 0; i < sb.length(); i++) {   
			tempChar = sb.charAt(i);   
			if (HTML_CHAR.containsKey(Character.toString(tempChar))) {   
				tempStr = (String) HTML_CHAR.get(Character.toString(tempChar));   
				sb.replace(i, i + 1, tempStr);   
				i += tempStr.length() - 1;   
			}   
		}
		return sb.toString();  
	}
}
