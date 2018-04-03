package com.hfjh.dams.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;

import com.hfjh.dams.shiro.bo.UserModel;
import com.hfjh.dams.shiro.constant.UserConstant;

public class UserRealm extends AuthorizingRealm {
	
	@Value("#{configProperties['password']}")
	private String password;

	/**
	 * Shiro权限认证，即权限授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Subject currentUser = SecurityUtils.getSubject();
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		// 存放(菜单)权限url字符串
		Set<String> urlSet = getUrlSet();
		// 存放角色名称
		Set<String> roles = getRoleSet();
		// 角色集合
		
		info.addRoles(roles);
		// 基于字符串模式的权限认证
		info.addStringPermissions(urlSet);
		// 把数据缓存到session里
		Session session = currentUser.getSession();
		session.setAttribute("info", info);
		return info;
	}

	/**
	 * 用户认证,登录认证(原理：用户提交 用户名和密码 --- shiro 封装令牌 ---- realm 通过用户名将密码查询返回 ----
	 * shiro 自动去比较查询出密码和用户输入密码是否一致---- 进行登录控制 )
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		// 根据用户名密码查找用户
		String userName = upToken.getUsername();
		UserModel user = new UserModel();
		user.setUserName(userName);
		user.setRealName(UserConstant.USER_REAL_NAME);
			
		return new SimpleAuthenticationInfo(user,this.password.toCharArray(),UserConstant.USER_REAL_NAME);
	}
	
	/**
	 * 用户退出时删除缓存数据
	 */
	@Override
	protected void doClearCache(PrincipalCollection principals){
		
	}
	
	/**
	 * Description：从缓存中获取用户角色权限
	 * @author wugj
	 * @since 2017-10-11  上午11:38:03
	 */
	private Set<String> getRoleSet(){
		Set<String> roleSet = new HashSet<String>();
		return roleSet;
	}
	
	/**
	 * Description：从缓存中获取用户模块、操作权限
	 * @author wugj
	 * @since 2017-10-11 上午11:38:21
	 */
	private Set<String> getUrlSet(){
		Set<String> urlSet = new HashSet<String>();
		return urlSet;
	}
	
	
}
