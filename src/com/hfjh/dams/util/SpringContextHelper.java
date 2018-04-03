package com.hfjh.dams.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取spring ApplicationContenxt
 * @author wugj
 * @version 1.0 2016-12-1
 */
public class SpringContextHelper implements ApplicationContextAware {
	 private static ApplicationContext appCtx;  
 
	  @Override  
	  public void setApplicationContext( ApplicationContext applicationContext ) {  
	      appCtx = applicationContext;  
	  }  
	  /** 
	   * 获得bean对象
	   * @author wugj
	   * @since 1.0 2016-12-1
	   */  
	  public static Object getBean(String beanName) {  
	      return appCtx.getBean(beanName);  
	  }  

}
