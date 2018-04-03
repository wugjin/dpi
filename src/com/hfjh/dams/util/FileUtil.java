package com.hfjh.dams.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

/**
 * 文件处理共通函数
 * 
 */
public class FileUtil {


    private final static int BUFFER_SIZE = 16 * 1024;

    /**
     * 验证文件路径是否存在，如果存在返回true
     * 
     * @param filePath
     *            文件路径
     */
    public static boolean isPathExist(String filePath) {
        boolean temp = false;
        File baseFile = new File(filePath);
        temp = baseFile.isDirectory();
        return temp;
    }

    /**
     * 验证文件是否存在，如果存在返回true
     * 
     * @param fullFileName
     *            文件完整路径
     */
    public static boolean isFileExist(String fullFileName) {
        boolean temp = false;
        File baseFile = new File(fullFileName);
        temp = baseFile.isFile();
        return temp;
    }

    /**
     * 创建一个文件路径，可以创建很深的路径，而不是一级文件夹。
     * 
     * @param filePath
     *            文件路径
     */
    public static boolean createPath(String filePath) {
        String strpa = filePath.replace("\\", "/");
        boolean temp = false;
        File baseFile = new File(strpa);
        if (!baseFile.exists()) {
            temp = baseFile.mkdirs();
        }
        return temp;
    }

    /**
     * 判断输入的文件类型是否合法
     * 
     * @param fileType
     *            输入的文件类型
     * @param requestType
     *            合法的文件类型
     * @return
     */
    public static boolean isLegalType(String fileType, String requestType) {
        String[] types = requestType.split(",");
        for (String type : types) {

            if (type.equals(fileType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除文件
     * 
     * @param filePath
     *            文件路径
     */
    public static boolean deleteFile(String filePath) {
        boolean temp = false;
        try {
            File f = new File(filePath);
            if (f.isFile() && f.exists()) {
                temp = f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.getLog(FileUtil.class).error("",e);
        }
        return temp;
    }
    
    /**
     * 删除文件
     * 
     * @param filePath
     *            文件路径
     */
    public static boolean deleteFile(File file) {
        boolean temp = false;
        try {
            if (file.isFile() && file.exists()) {
                temp = file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.getLog(FileUtil.class).error("",e);
        }
        return temp;
    }
    
    public static boolean deleteDirectory(String filePath) {   
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符   
        if (!filePath.endsWith(File.separator)) {   
            filePath = filePath + File.separator;   
        }   
        File dirFile = new File(filePath);   
        //如果dir对应的文件不存在，或者不是一个目录，则退出   
        if (!dirFile.exists() || !dirFile.isDirectory()) {   
            return false;   
        }   
        boolean flag = true;   
        //删除文件夹下的所有文件(包括子目录)   
        File[] files = dirFile.listFiles();   
        for (int i = 0; i < files.length; i++) {   
            //删除子文件   
            if (files[i].isFile()) {   
                flag = deleteFile(files[i].getAbsolutePath());   
                if (!flag) break;   
            } //删除子目录   
            else {   
                flag = deleteDirectory(files[i].getAbsolutePath());   
                if (!flag) break;   
            }   
        }   
        if (!flag) return false;   
        //删除当前目录   
        if (dirFile.delete()) {   
            return true;   
        } else {   
            return false;   
        }   
    } 
    
    /**
     * 删除文件夹
     * @param sPath
     * @return
     */
    public static boolean DeleteFolder(String filePath) {   
        boolean flag = false;   
        File file = new File(filePath);   
        // 判断目录或文件是否存在   
        if (!file.exists()) {  // 不存在返回 false   
            return flag;   
        } else {   
            // 判断是否为文件   
            if (file.isFile()) {  // 为文件时调用删除文件方法   
                return deleteFile(filePath);   
            } else {  // 为目录时调用删除目录方法   
                return deleteDirectory(filePath);   
            }   
        }   
    } 

    /**
     * 读取文件内容，但并不在服务端上保存
     * 
     * @param filePath
     *            文件路径
     */
    public static List<String> readFileContents(File file) {
        List<String> s = new ArrayList<String>();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            // 建立BufferedReader对象，并实例化为br
            BufferedReader br = new BufferedReader(fr);
            // 从文件读取一行字符串
            String Line = br.readLine();
            // 判断读取到的字符串是否不为空
            while (Line != null) {
                s.add(Line);
                // 从文件中继续读取一行数据
                Line = br.readLine();
            }
            // 关闭BufferedReader对象
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.getLog(FileUtil.class).error("",e);
        } finally {
            try {
                if (fr != null) {
                    // 关闭文件
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogFactory.getLog(FileUtil.class).error("",e);
            }
        }
        return s;
    }

    /**
     * 复制文件方法
     * 
     * @param src
     *            文件源
     * @param dst
     *            目标文件
     */
    public static void copy(File src, File dst) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src),
                        BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.getLog(FileUtil.class).error("",e);
        }
    }
    
    /**
     * 复制文件夹方法
     * 
     * @param src
     *            文件源
     * @param dst
     *            目标文件
     * @throws Exception 
     */
	public static void copyFolder(String src, String des) throws Exception {  
        File file1=new File(src);
        if(file1.exists()){
	        File[] fs=file1.listFiles();  
	        File file2=new File(des);  
	        if(!file2.exists()){  
	            file2.mkdirs();  
	        }  
	        for (File f : fs) {  
	            if(f.isFile()){  
	                fileCopy(f.getPath(),des+"\\"+f.getName()); //调用文件拷贝的方法  
	            }else if(f.isDirectory()){  
	            	copyFolder(f.getPath(),des+"\\"+f.getName());  
	            }  
	        }  
        }   
    }  
  
    /** 
     * 文件拷贝的方法 
     * @throws Exception 
     */  
    public static void fileCopy(String src, String des) throws Exception {
    	try {  
            FileInputStream in = new java.io.FileInputStream(src);  
            FileOutputStream out = new FileOutputStream(des);  
            byte[] bt = new byte[1024];  
            int count;  
            while ((count = in.read(bt)) > 0) {  
                out.write(bt, 0, count);  
            }  
            in.close();  
            out.close();  
        } catch (IOException ex) {  
        	ex.printStackTrace();
        	LogFactory.getLog(FileUtil.class).error("",ex);
        }
    }
    
    /** 
     * 文件拷贝的方法 
     * @throws Exception 
     */  
    public static void fileCopy(InputStream in, String des){
    	try {  
            FileOutputStream out = new FileOutputStream(des);  
            byte[] bt = new byte[1024];  
            int count;  
            while ((count = in.read(bt)) > 0) {  
                out.write(bt, 0, count);  
            }  
            in.close();  
            out.close();  
        } catch (IOException ex) {  
        	ex.printStackTrace();
        	LogFactory.getLog(FileUtil.class).error("",ex);
        }
    }
    
    public static byte[] toByteArray(String filename) throws IOException {  
    	  
        File f = new File(filename);  
        if (!f.exists()) {  
            throw new FileNotFoundException(filename);  
        }  
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
        BufferedInputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while (-1 != (len = in.read(buffer, 0, buf_size))) {  
                bos.write(buffer, 0, len);  
            }  
            return bos.toByteArray();  
        } catch (IOException e) {  
            e.printStackTrace();
            LogFactory.getLog(FileUtil.class).error("",e);
            throw e;  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();
                LogFactory.getLog(FileUtil.class).error("",e);
            }  
            bos.close();  
        }  
    }
}
