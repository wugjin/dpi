package com.hfjh.dams.common.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hfjh.dams.common.constant.QueryConstant;
import com.hfjh.dams.core.constant.ResultConstant;
import com.hfjh.dams.core.controller.BaseController;
import com.hfjh.dams.util.FileUtil;

@Controller("queryController")
@RequestMapping("query")
public class QueryController extends BaseController{
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String tologinPage(HttpServletRequest request) throws Exception{
		return "common/query";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void reload(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String fromPath = request.getParameter("fromPath");
		String toPath = request.getParameter("toPath");
		
		fromPath = fromPath.replaceAll("\\\\", "/");
		toPath = toPath.replaceAll("\\\\", "/");
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(FileUtil.isPathExist(fromPath) && FileUtil.isPathExist(toPath)){//判断路径是否存在
			HttpSession session = request.getSession();
			
			//挂接状态置为正在挂接
			session.setAttribute(QueryConstant.QUERY_SESSION_STOP, QueryConstant.QUERY_NOT_STOP_VALUE);
			//挂接进度置为0
			session.setAttribute(QueryConstant.QUERY_SESSION_PROCESS, "0");
			
			//总条数
			int dataCount = 0;
			//数据索引
			int dataIndex = 1;
			//挂接进度
			int progress = 0;
			
			boolean flag = true;
			
			//获取文件夹总数，用户显示文件挂接进度
			File file = new File(fromPath);
			LinkedList<File> list = new LinkedList<File>();
	        File[] files = file.listFiles();
	        for (File file2 : files) {//循环第一层文件夹
	            if (file2.isDirectory()) {//文件夹
	                list.add(file2);
	            }else {//目标文件
	            	if(flag) {
	            		dataCount++;
	            		flag = false;
	            	}
	            }
	        }
	        
	        File temp_file;
	        while (!list.isEmpty()) {//循环第二层及以下文件夹
	            temp_file = list.removeFirst();
	            files = temp_file.listFiles();
	            flag = true;
	            for (File file2 : files) {
	                if (file2.isDirectory()) {
	                    list.add(file2);
	                }else {//目标文件
	                	if(flag) {
		            		dataCount++;
		            		flag = false;
		            	}
	                }
	            }
	        }
	        
	        
	        //循环判断原件
	        file = new File(fromPath);
			list = new LinkedList<File>();
	        files = file.listFiles();
	        flag = true;
	        for (File file2 : files) {//循环第一层文件夹
	            if (file2.isDirectory()) {//文件夹
	                list.add(file2);
	            } else {
	            	//挂接操作取消
					if (session.getAttribute(QueryConstant.QUERY_SESSION_STOP).equals(QueryConstant.QUERY_STOP_VALUE)) {
						break;
					}
					if(flag) {
						copyFile(fromPath, toPath, file2);
						flag = false;
						int temp = (int) dataIndex * 100 / dataCount;
						if (temp > progress) {
							temp = temp>=100?100:temp;
							progress = temp;
							session.setAttribute(QueryConstant.QUERY_SESSION_PROCESS, progress);
						}
						dataIndex++;
					}
	            }
	        }
	        
	        while (!list.isEmpty()) {//循环第二层及以下文件夹
	            temp_file = list.removeFirst();
	            files = temp_file.listFiles();
	            flag = true;
	            for (File file2 : files) {
	                if (file2.isDirectory()) {
	                    list.add(file2);
	                } else {
	                	//挂接操作取消
	    				if (session.getAttribute(QueryConstant.QUERY_SESSION_STOP).equals(QueryConstant.QUERY_STOP_VALUE)) {
	    					break;
	    				}
	    				
	    				if(flag) {
							copyFile(fromPath, toPath, file2);
							flag = false;
							int temp = (int) dataIndex * 100 / dataCount;
							if (temp > progress) {
								temp = temp>=100?100:temp;
								progress = temp;
								session.setAttribute(QueryConstant.QUERY_SESSION_PROCESS, progress);
							}
							dataIndex++;
						}
	                }
	            }
	        }
			
			
	        map.put("dataCount", dataCount);
			map.put("result", ResultConstant.RESULT_SUCCESS);
		}else{
			map.put("result", QueryConstant.RESULT_PATH_NOT_EXIST);
			map.put("errorInfo", QueryConstant.RESULT_PATH_NOT_EXIST_MESSAGE);
		}
		
		writeJson(response, objectMapper.writeValueAsString(map));
	}
	
	private void copyFile(String fromPath, String toPath, File file) {
		String path = file.getAbsolutePath();
		path = path.replaceAll("\\\\", "/");
		String dstPath = toPath+path.substring(fromPath.length());
		File dstFile = new File(dstPath);
		FileUtil.createPath(dstFile.getParentFile().getAbsolutePath());
		
		FileUtil.copy(file, dstFile);
	}
	
	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public void process(HttpServletRequest request , HttpServletResponse response) throws Exception{
		Object o = request.getSession().getAttribute(QueryConstant.QUERY_SESSION_PROCESS);
		String process = "0";
		if(o != null){
			process = o.toString();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("process", process);
		
		writeJson(response, objectMapper.writeValueAsString(map));
	}
	
	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public void stop(HttpServletRequest request , HttpServletResponse response) throws Exception{
		request.getSession().setAttribute(QueryConstant.QUERY_SESSION_STOP, QueryConstant.QUERY_STOP_VALUE);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("resutl", 0);
		
		writeJson(response, objectMapper.writeValueAsString(map));
	}
	
	@RequestMapping(value = "/download_log", method = RequestMethod.POST)
	public void downLoadLog(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String logPath = request.getParameter("logPath");
		try {
			response.setContentType("application/octet-stream");
			response.setContentType("text/html;charset=gb2312");
			response.reset();
			File file = new File(logPath);
			String userAgent = request.getHeader("User-Agent");
	    	userAgent = userAgent.toLowerCase();
	    	if (userAgent.indexOf("safari") != -1 ){
	    		response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(file.getName().getBytes("UTF-8"), "ISO8859_1"));
	    	}else{
	    		response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(file.getName().getBytes("gbk"), "ISO8859_1"));
	    	}
			
			response.addHeader("Content-Length", "=" + file.length());
			InputStream is = new FileInputStream(file);
			byte[] buf = new byte[1048576];
			OutputStream toClient = new BufferedOutputStream(response
					.getOutputStream());
			response.setContentType("application/octet-stream");
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				toClient.write(buf, 0, len);
			}
			is.close();
			toClient.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
