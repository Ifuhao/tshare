package utils;

import java.util.ResourceBundle;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Mailer {
	// 我的邮箱地址
	private static ResourceBundle rb = ResourceBundle.getBundle("mailer");
	private static String address = Mailer.rb.getString("address");
	private static String password = Mailer.rb.getString("password");
	private static String host = Mailer.rb.getString("host");
	
	/**
	 * 发送邮箱验证码
	 * @param address 目标邮箱地址
	 * @param subject 邮件标题
	 * @param body 邮件内容
	 */
	public static boolean sendEmail(String address, String subject, String body) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(Mailer.host);
			email.setCharset("UTF-8");
			email.addTo(address);
			
			email.setFrom(Mailer.address, "Tshare客服团队");
			email.setAuthentication(Mailer.address, Mailer.password);
			email.setSubject(subject);
			email.setMsg(body);
			email.send();
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
		}
		return false;
	}
}
