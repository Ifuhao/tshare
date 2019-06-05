package api.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import utils.form.FormOperator;
import json.JSONArray;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import dao.vo.User;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public Register() {
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
		
		User user = new  User();
		new FormOperator<User>(user, request);
		
		// 查询一下邮箱是否已经注册
		UserDAO userDAO = new UserDAOImpl();
		User newUser = userDAO.selectById(user.getId());
		
		JSONArray array = new JSONArray();
		
		if(newUser != null) {
			// 邮箱已经注册
			array.set("code", 0);
			array.set("msg", "邮箱已经注册");
		} else {
			System.out.println(user);
			userDAO.insert(user);
			
			array.set("code", 1);
			array.set("msg", "注册成功");
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
