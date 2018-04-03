package com.hfjh.dams.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.LogFactory;

/**
* @ClassName: ClassUtils 
* @Description: 类工具
* @author zhaox
* @date Mar 27, 2012 10:53:16 AM
 */
public class ClassUtils {
	/**
	* @Title: getAllClassBySuperClass 
	* @Description: 给一个父类，返回这个父类的所有实现类
	* @param c 一个类 
	* @return List<Class> 返回类型 
	* @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Class> getAllClassBySuperClass(Class c){
		List<Class> returnClassList=new ArrayList<Class>();
		
		String packageName = c.getPackage().getName();
		try {
			List<Class> allClass = getClasses(packageName); 
		
			//判断是否是同一个接口
			for(int i=0;i<allClass.size();i++){
				if(!Modifier.isInterface(allClass.get(i).getModifiers()) 
						&& !Modifier.isAbstract(allClass.get(i).getModifiers())
						&& !c.equals(allClass.get(i)) && c.isAssignableFrom(allClass.get(i))){
					returnClassList.add(allClass.get(i));
					
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LogFactory.getLog(ClassUtils.class).error("",e);
		} catch (IOException e) {
			e.printStackTrace();
			LogFactory.getLog(ClassUtils.class).error("",e);
		}
		return returnClassList;
	}
	/**
	* @Title: getClasses 
	* @Description: //从一个包中查找出所有的类，在jar包中不能查找
	* @param packageName
	* @param @throws ClassNotFoundException
	* @param @throws IOException    设定文件 
	* @return List<Class> 返回类型 
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	private static List<Class> getClasses(String packageName)
		throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			try {
                dirs.add(new File(resource.toURI().getPath()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                LogFactory.getLog(ClassUtils.class).error("",e);
            }
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName)
		throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." +
						file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' +
						file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
	
	/**
	* @Title: getAllClassBySuperClass 
	* @Description: 给一个父类，返回这个父类的所有实现类
	* @param c 一个类 
	* @return List<Class> 返回类型 
	* @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Class> getAllClassBySuperClass(Class c,String packageName){
		List<Class> returnClassList=new ArrayList<Class>();
		
		try {
			List<Class> allClass = getClasses(packageName); 
		
			//判断是否是同一个接口
			for(int i=0;i<allClass.size();i++){
				if(!Modifier.isInterface(allClass.get(i).getModifiers()) 
						&& !Modifier.isAbstract(allClass.get(i).getModifiers())
						&& !c.equals(allClass.get(i)) && c.isAssignableFrom(allClass.get(i))){
					returnClassList.add(allClass.get(i));
					
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LogFactory.getLog(ClassUtils.class).error("",e);
		} catch (IOException e) {
			e.printStackTrace();
			LogFactory.getLog(ClassUtils.class).error("",e);
		}
		return returnClassList;
	}
	
}
