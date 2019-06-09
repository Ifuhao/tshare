package api.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.vo.User;

@SuppressWarnings("serial")
public class PersonServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PersonServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		User user = (User)request.getSession(true).getAttribute("user");
		
		if(user == null) {
			response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
			// 用户没有登录
		} else {
			JSONArray json = new JSONArray();
			JSONArray user_json = this.getUserJson(user);
			
			json.set("code", 1);
			json.set("user", user_json);
			
			PrintWriter out = response.getWriter();
			out.write(JSONParser.json_encode(json));
			out.close();
		}
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
	
	private JSONArray getUserJson(User user) {
		JSONArray json = new JSONArray();
		json.set("username", user.getUsername());
		json.set("head_image", user.getHead_image());
		json.set("upload_time", user.getUpload_time());
		json.set("transaction", user.getTransaction());
		json.set("money", user.getMoney());
		json.set("follow_num", user.getFollow());
		json.set("fans_num", user.getFans());
		return json;
	}
}
