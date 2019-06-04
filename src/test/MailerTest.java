package test;

import utils.Mailer;

public class MailerTest {
	public static void main(String[] args) {
		String address = "hitwh_fuhao@126.com";
		String subject = "Tshare注册验证码";
		String body = "我是tshare的客服人员";
		
		Mailer.sendEmail(address, subject, body);
	}
}
