package api.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import json.JSONArray;
import utils.Cookie;
import utils.JSONParser;
import utils.form.FormOperator;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import dao.vo.User;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public Login() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
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
		
		User user = new User();
		new FormOperator<User>(user, request);
		
		String auto = request.getParameter("auto_login");
		if(user.getId() == null) {
			// 此时是自动登陆，获取cookie
			Cookie cookie = new Cookie(request);
			user.setId(cookie.getCookie("id"));
			user.setPassword(cookie.getCookie("pwd"));
			auto = "1";
		}
		
		JSONArray array = new JSONArray();
		
		if(user.getId() == null) {
			// 无法登陆
			array.set("code", 0);
			array.set("msg", "不能自动登录");
		} else {
			// 验证账号密码
			String password = user.getPassword();
			
			UserDAO uDao = new UserDAOImpl();
			user = uDao.selectById(user.getId());
			
			if(user == null || !password.equals(user.getPassword())) {
				// 登录失败
				array.set("code", 0);
				array.set("msg", "登录失败");
			} else {
				// 登录成功
				// 暂时没有给cookie加密
				// 暂时没有设置一个session_id
				
				if(auto.equals("1")) {
					Cookie cookie = new Cookie();
					cookie.setCookie(response, "id", user.getId(), 7*24*3600, "/");
					cookie.setCookie(response, "pwd", user.getPassword(), 7*24*3600, "/");
				}
				
				// 使用session保存用户
				HttpSession session = request.getSession(true);
				session.setAttribute("user", user);
				
				array.set("code", 1);
				array.set("msg", "登录成功");
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
