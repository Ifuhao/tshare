package api.market.wish;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.WishDAO;
import dao.impl.WishDAOImpl;
import dao.vo.User;
import dao.vo.Wish;

@SuppressWarnings("serial")
public class PublishWish extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PublishWish() {
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
			// 用户没有登录
		}
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		Date time = new Date(new java.util.Date().getTime());
		
		WishDAO wish_dao = new WishDAOImpl();
		Wish new_wish = new Wish();
		new_wish.setWish_id(wish_dao.count()+1);
		new_wish.setId(user.getId());
		new_wish.setTitle(title);
		new_wish.setDescription(description);
		new_wish.setTime(time);
		wish_dao.insert(new_wish);
		
		JSONArray json = new JSONArray();
		json.set("code", 1);
		json.set("msg", "发布成功");
		PrintWriter out = response.getWriter();
		out.write(JSONParser.json_encode(json));
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
