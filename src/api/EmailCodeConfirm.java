package api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import utils.form.FormOperator;
import json.JSONArray;
import dao.VeriDAO;
import dao.impl.VeriDAOImpl;
import dao.vo.Veri;

public class EmailCodeConfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public EmailCodeConfirm() {
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
		response.setCharacterEncoding("UTF-8");
		
		Veri veri = new Veri();
		new FormOperator<Veri>(veri, request);
		
		// 查询数据库确认验证码的正确性和有效性
		VeriDAO veriDAO = new VeriDAOImpl();
		String email_code = veri.getCode();
		veri = veriDAO.selectById(veri.getId());
		
		JSONArray array = new JSONArray();
		
		if(veri == null) {
			// 验证码不存在
			array.set("code", 0);
			array.set("msg", "邮箱验证码不存在");
		} else {
			 if(!email_code.equals(veri.getCode())){
				 // 验证码错误
				 array.set("code", 0);
				 array.set("msg", "邮箱验证码错误");
				 
			 } else {
				 long time = new Date().getTime()/1000;
				 if(time > veri.getTime()) {
					 // 验证码超时
					 array.set("code", 0);
					 array.set("msg", "邮箱验证码超时");
				 } else {
					 // 验证成功
					 array.set("code", 1);
					 array.set("msg", "邮箱验证码正确");
				 }
				 
				 // 删除验证码
				 veriDAO.deleteById(veri.getId());
			 }
		}
		
		PrintWriter out = response.getWriter();
		out.print(JSONParser.json_encode(array));
		out.close();
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
