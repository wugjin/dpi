package com.hfjh.dams.core.exception;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyExceptionHandler implements HandlerExceptionResolver {    

  private static final Logger logger = Logger.getLogger(MyExceptionHandler.class);   

 /** 
  * 通过实现自定义接口HandlerExceptionResolver的resolveException方法，
  * 来完成日志的记录，而且还可以指定程序发生错误后跳转的web页面
  */
 @Override    
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {          
    logger.error("",ex);        
    return null;
  }
}

