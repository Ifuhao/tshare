package api.market.seek;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import dao.SeekDAO;
import dao.impl.SeekDAOImpl;
import dao.vo.Seek;
import dao.vo.User;

@SuppressWarnings("serial")
public class PublishSeek extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PublishSeek() {
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
		
		User user = ((User)request.getSession(true).getAttribute("user"));
		if(user == null) {
			// 用户不在登录状态
		}
		
		SeekDAO seek_dao = new SeekDAOImpl();
		
		Seek seek = new Seek();
		seek.setId(user.getId());
		seek.setTime(new Date(new java.util.Date().getTime()));
		seek.setSeek_id(seek_dao.count()+1);
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		seek.setTitle(title);
		seek.setDescription(description);
		
		seek_dao.insert(seek);
		
		JSONArray json = new JSONArray();
		json.set("code", 1);
		json.set("msg", "发布成功");
		PrintWriter out = response.getWriter();
		out.write(JSONParser.json_encode(json));
		out.close();
		
//		int wish = Integer.parseInt(request.getParameter("wish"));
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