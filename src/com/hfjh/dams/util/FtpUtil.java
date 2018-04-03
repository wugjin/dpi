package com.hfjh.dams.util;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.logging.LogFactory;

public class FtpUtil {
	
	public static FTPClient getClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException{
		FTPClient client = new FTPClient();
		client.connect(ConfigUtil.getString("ftp.ip"), ConfigUtil.getInt("ftp.port", 21));
		client.login(ConfigUtil.getString("ftp.user"), ConfigUtil.getString("ftp.password"));
		
		return client;
	}
	
	public static void uploadFile(String desPath, File srcFile) throws Exception{
		FTPClient client =null;
		try {
			client = getClient();
			client.changeDirectory(ConfigUtil.getString("ftp.upload.path"));
			try{
				client.createDirectory(desPath);
			}catch(Exception e){}
			client.changeDirectory(desPath);
			client.upload(srcFile);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException e) {
			e.printStackTrace();
			LogFactory.getLog(FtpUtil.class).error("",e);
			throw e;
		}finally{
			if(client != null){
				try {
					client.disconnect(true);
				} catch (IllegalStateException | IOException
						| FTPIllegalReplyException | FTPException e) {
					e.printStackTrace();
					LogFactory.getLog(FtpUtil.class).error("",e);
				}
			}
		}
		
	}
	
	public static void download(String ftpPath, File localFile) throws Exception{
		FTPClient client =null;
		try {
			client = getClient();
			client.download(ftpPath, localFile);
			
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException e) {
			e.printStackTrace();
			LogFactory.getLog(FtpUtil.class).error("",e);
			throw e;
		}finally{
			if(client != null){
				try {
					client.disconnect(true);
				} catch (IllegalStateException | IOException
						| FTPIllegalReplyException | FTPException e) {
					e.printStackTrace();
					LogFactory.getLog(FtpUtil.class).error("",e);
				}
			}
		}
		
	}
	
	public static void delete(String ftpPath) throws Exception{
		FTPClient client =null;
		try {
			client = getClient();
			client.deleteFile(ftpPath);
			
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException e) {
			e.printStackTrace();
			LogFactory.getLog(FtpUtil.class).error("",e);
			throw e;
		}finally{
			if(client != null){
				try {
					client.disconnect(true);
				} catch (IllegalStateException | IOException
						| FTPIllegalReplyException | FTPException e) {
					e.printStackTrace();
					LogFactory.getLog(FtpUtil.class).error("",e);
				}
			}
		}
	}
	
	public static void deleteDirectory(String directory) throws Exception{
		FTPClient client =null;
		try {
			client = getClient();
			client.changeDirectory(directory);
			FTPFile[] files = client.list();
			if(files!=null && files.length>0){
				for(FTPFile file : files){
					if(file.getType() == 0){
						client.deleteFile(file.getName());
					}
				}
			}
			client.changeDirectory("/");
			client.deleteDirectory(directory);
			
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException e) {
			e.printStackTrace();
			LogFactory.getLog(FtpUtil.class).error("",e);
			throw e;
		}finally{
			if(client != null){
				try {
					client.disconnect(true);
				} catch (IllegalStateException | IOException
						| FTPIllegalReplyException | FTPException e) {
					e.printStackTrace();
					LogFactory.getLog(FtpUtil.class).error("",e);
				}
			}
		}
	}
	
	public static void copyFile(String srcPath, String decPath) throws Exception{
		FTPClient client =null;
		String path = System.getProperty("user.dir")+"/"+UUID.randomUUID().toString();
	    FileUtil.createPath(path);
		try {
			client = getClient();
			client.changeDirectory(srcPath);
			FTPFile[] files = client.list();
			File directory = null;
			if(files!=null && files.length>0){
				for(FTPFile file : files){
					if(file.getType() == 0){
						directory = new File(path+"/"+file.getName());
						download(srcPath+"/"+file.getName(),directory);
						uploadFile(decPath, directory);
					}
				}
			}
			
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException e) {
			e.printStackTrace();
			LogFactory.getLog(FtpUtil.class).error("",e);
			throw e;
		}finally{
			if(client != null){
				try {
					client.disconnect(true);
				} catch (IllegalStateException | IOException
						| FTPIllegalReplyException | FTPException e) {
					e.printStackTrace();
					LogFactory.getLog(FtpUtil.class).error("",e);
				}
			}
			//删除临时文件
			FileUtil.deleteDirectory(path);
		}
		
	}
}
