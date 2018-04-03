package com.hfjh.dams.common.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hfjh.dams.common.constant.IndexConstant;
import com.hfjh.dams.core.constant.ResultConstant;
import com.hfjh.dams.core.constant.SysConstant;
import com.hfjh.dams.core.controller.BaseController;
import com.hfjh.dams.util.DateStyle;
import com.hfjh.dams.util.DateUtil;
import com.hfjh.dams.util.FileUtil;
import com.hfjh.dams.util.NumberUtil;

@Controller("indexController")
@RequestMapping("index")
public class IndexController extends BaseController{
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String tologinPage(HttpServletRequest request) throws Exception{
		return "common/index";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void reload(HttpServletRequest request , HttpServletResponse response) throws Exception{
		String fromPath = request.getParameter("fromPath");
		String toPath = request.getParameter("toPath");
		int dpiNum = NumberUtil.toInt(request.getParameter("dpiNum"));
		
		fromPath = fromPath.replaceAll("\\\\", "/");
		toPath = toPath.replaceAll("\\\\", "/");
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(FileUtil.isPathExist(fromPath) && FileUtil.isPathExist(toPath)){//判断路径是否存在
			HttpSession session = request.getSession();
			
			//生成临时目录
			String rootPath = request.getSession().getServletContext().getRealPath(SysConstant.TEMP_DIR);
			String tempPath = UUID.randomUUID().toString().replaceAll("-", "");
			//创建日志文件
			String logPath = rootPath+"/"+tempPath+"/"+IndexConstant.LOG_FILE_NAME;
			FileUtil.createPath(rootPath+"/"+tempPath);
			
			//挂接状态置为正在挂接
			session.setAttribute(IndexConstant.DPI_SESSION_STOP, IndexConstant.DPI_NOT_STOP_VALUE);
			//挂接进度置为0
			session.setAttribute(IndexConstant.DPI_SESSION_PROCESS, "0");
			
			//初始化日志文件
			cleanLog(logPath);
			
			//总条数
			int dataCount = 0;
			//有问题条数
			int failCount = 0;
			//判断失败条数
			int errorCount = 0;
			//数据索引
			int dataIndex = 1;
			//挂接进度
			int progress = 0;
			
			int result = -1;
			
			//获取文件总数，用户显示文件挂接进度
			File file = new File(fromPath);
			LinkedList<File> list = new LinkedList<File>();
	        File[] files = file.listFiles();
	        for (File file2 : files) {//循环第一层文件夹
	            if (file2.isDirectory()) {//文件夹
	                list.add(file2);
	            } else {
	            	dataCount++;
	            }
	        }
	        
	        File temp_file;
	        while (!list.isEmpty()) {//循环第二层及以下文件夹
	            temp_file = list.removeFirst();
	            files = temp_file.listFiles();
	            for (File file2 : files) {
	                if (file2.isDirectory()) {
	                    list.add(file2);
	                } else {
	                	dataCount++;
	                }
	            }
	        }
	        
	        
	        //循环判断dpi原件
	        file = new File(fromPath);
			list = new LinkedList<File>();
	        files = file.listFiles();
	        for (File file2 : files) {//循环第一层文件夹
	            if (file2.isDirectory()) {//文件夹
	                list.add(file2);
	            } else {
	            	//挂接操作取消
					if (session.getAttribute(IndexConstant.DPI_SESSION_STOP).equals(IndexConstant.DPI_STOP_VALUE)) {
						break;
					}
					result = checkDpi(fromPath, toPath, dpiNum, file2, logPath);
					
	            	if(result == 1){//有问题
	            		failCount++;
	            	}else if(result == 2){//判断失败
	            		errorCount++;
	            	}
	            	
	            	int temp = (int) dataIndex * 100 / dataCount;
					if (temp > progress) {
						temp = temp>=100?100:temp;
						progress = temp;
						session.setAttribute(IndexConstant.DPI_SESSION_PROCESS, progress);
					}
					dataIndex++;
	            }
	        }
	        
	        while (!list.isEmpty()) {//循环第二层及以下文件夹
	            temp_file = list.removeFirst();
	            files = temp_file.listFiles();
	            for (File file2 : files) {
	                if (file2.isDirectory()) {
	                    list.add(file2);
	                } else {
	                	//挂接操作取消
	    				if (session.getAttribute(IndexConstant.DPI_SESSION_STOP).equals(IndexConstant.DPI_STOP_VALUE)) {
	    					break;
	    				}
	    				
	    				result = checkDpi(fromPath, toPath, dpiNum, file2, logPath);
						
		            	if(result == 1){//有问题
		            		failCount++;
		            	}else if(result == 2){//判断失败
		            		errorCount++;
		            	}
		            	
	                	int temp = (int) dataIndex * 100 / dataCount;
	    				if (temp > progress) {
	    					temp = temp>=100?100:temp;
	    					progress = temp;
	    					session.setAttribute(IndexConstant.DPI_SESSION_PROCESS, progress);
	    				}
	    				dataIndex++;
	                }
	            }
	        }
			
			
	        map.put("dataCount", dataCount);
			map.put("failCount", failCount);
			map.put("errorCount", errorCount);
			map.put("logPath", logPath);
			map.put("result", ResultConstant.RESULT_SUCCESS);
		}else{
			map.put("result", IndexConstant.RESULT_PATH_NOT_EXIST);
			map.put("errorInfo", IndexConstant.RESULT_PATH_NOT_EXIST_MESSAGE);
		}
		
		writeJson(response, objectMapper.writeValueAsString(map));
	}
	
	private int checkDpi(String fromPath, String toPath, int dpiNum, File file, String logPath) {
		String fileName = file.getName();
		String[] temp = fileName.split("\\.");
		String objType = "";//文件类型
		if (temp.length > 1) {
			objType = temp[temp.length - 1];
		}
		if(objType.equalsIgnoreCase("jpg")) {
			ImageInfo imageInfo =null;
			try {
				imageInfo = Sanselan.getImageInfo(file);
				if(imageInfo.getPhysicalWidthDpi()>=dpiNum && imageInfo.getPhysicalWidthDpi()>=dpiNum) {//成功
					return 0;
				}else {//失败
					copyFile(fromPath, toPath, file);//拷贝文件
					return 1;
				}
			} catch (ImageReadException | IOException e) {
				WriteToLog("文件：【"+file.getPath()+"】判断失败。" , logPath);
				e.printStackTrace();
				return 2;//异常
			}
		}

		return 0;
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
		Object o = request.getSession().getAttribute(IndexConstant.DPI_SESSION_PROCESS);
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
		request.getSession().setAttribute(IndexConstant.DPI_SESSION_STOP, IndexConstant.DPI_STOP_VALUE);
		
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
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	
	
	private void WriteToLog(String str , String logPath) {
		File filename1 = new File(logPath);
		String timeStr = DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS) + "\r\n";
		RandomAccessFile mm = null;
		str = str + "\r\n"
				+ "======================================================"
				+ "\r\n";
		try {
			mm = new RandomAccessFile(filename1, "rw");
			mm.seek(mm.length());
			mm.write(timeStr.getBytes());
			mm.write(str.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("", e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("", e);
		}
	}
	
	private void cleanLog(String logPath) {
		// 先清理之前的挂接错误日志
		try {
			File log = new File(logPath);
			if (!log.exists()) {
				log.createNewFile();
			} else {
				FileWriter fw = new FileWriter(log);
				fw.write("");
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}
	}
}
