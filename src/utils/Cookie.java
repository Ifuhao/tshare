package utils;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookie {
	
	private HashMap<String, String> map;
	
	public Cookie() {}
	
	public Cookie(HttpServletRequest request) {
		this.map = new HashMap<>();
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(int i=0;i<cookies.length;i++) {
				this.map.put(cookies[i].getName(), cookies[i].getValue());
			}
		}
	}
	
	public String getCookie(String key) {
		return this.map.get(key);
	}
	
	/**
	 * 获取一个指定生存周期，制定作用域的cookie
	 * @param response
	 * @param key
	 * @param value
	 * @param time 生存周期
	 * @param domain 作用域
	 * @return
	 */
	public void setCookie(HttpServletResponse response, String key, String value, int time, String domain) {
		// 此处添加对cookie的加密措施
		// 暂未完成
		
		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key, value);
		// 设置cookie存活时间
		cookie.setMaxAge(time);
		// 设置cookie的作用域
		cookie.setPath(domain);
		response.addCookie(cookie);
	}
	
	/**
	 * 获取一个指定生存周期，默认作用域为全局的cookie
	 * @param key
	 * @param value
	 * @param time
	 * @return
	 */
	public void setCookie(HttpServletResponse response, String key, String value, int time) {
		this.setCookie(response, key, value, time, "/");
	}
	
	/**
	 * 获取一个生存周期为1小时，作用于为全局的cookie
	 * @param key
	 * @param value
	 * @return
	 */
	public void getCookie(HttpServletResponse response, String key, String value) {
		this.setCookie(response, key, value, 3600);
	}
}
