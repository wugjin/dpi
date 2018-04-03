/**
 * @Project: pfsc
 * Copyright 2016 www.1stquan.com
 */
package com.hfjh.dams.core.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
@Controller
public class BaseController {
	protected Log log = LogFactory.getLog(getClass());
	
	protected static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 返回数据
	 * @param result
	 */
	protected void writeJson(HttpServletResponse response, Object result) {
		try {
			response.setContentType("text/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(result.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.error("writeJson()", e);
		}
	}
}
