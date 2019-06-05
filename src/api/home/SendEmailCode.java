package api.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import utils.Mailer;
import utils.form.FormOperator;
import json.JSONArray;
import dao.VeriDAO;
import dao.impl.VeriDAOImpl;
import dao.vo.Veri;

public class SendEmailCode extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public SendEmailCode() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		
		Veri veri = new Veri();
		new FormOperator<Veri>(veri, request);
		
		VeriDAO veriDAO = new VeriDAOImpl();
		veriDAO.deleteById(veri.getId());		// 删除之前发送过的验证码
		
		int code = new Random().nextInt();		// 使用随机数生成验证码
		if(code<0) code = -code;
		code = code % 899999 + 100000;
		
		String body = "您好！此电子邮件地址正用于注册Tshare平台帐号，验证码：<h2>" + code + "</h2>请勿泄露给他人，10分钟内有效。<br>"
				+ "如果不是您本人操作，请忽略此邮件，不会有任何情况发生。<br><br>此致<br>Tshare客服团队";
		String subject = "Tshare注册验证码";
		
		JSONArray array = new JSONArray();
		
		if(Mailer.sendEmail(veri.getId(), subject, body)) {
			veri.setCode(code+"");	// 设置验证码
			
			long time = new Date().getTime() + 10*60*1000;
			time = time / 1000;
			veri.setTime(time);		// 设置验证码过期时间
			
			veriDAO.insert(veri);
			
			array.set("code", 1);
			array.set("msg", "验证码发送成功");
		} else {
			array.set("code", 0);
			array.set("msg", "验证码发送失败");
		}
		
		PrintWriter out = response.getWriter();
		out.print(JSONParser.json_encode(array));
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
