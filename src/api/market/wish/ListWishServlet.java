package api.market.wish;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.WishDAO;
import dao.impl.WishDAOImpl;
import dao.vo.Wish;

@SuppressWarnings("serial")
public class ListWishServlet extends HttpServlet {
	private static final int PAGESIZE = 30;

	/**
	 * Constructor of the object.
	 */
	public ListWishServlet() {
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
		
		int page = Integer.parseInt(request.getParameter("page"));
		
		WishDAO wish_dao = new WishDAOImpl();
		int count = wish_dao.effectCount();
		int start = (page-1)*PAGESIZE;
		
		JSONArray json = new JSONArray(false);
		if(start >= count) {
			// 该页不存在
			json.set("code", 0);
			json.set("msg", "该页不存在");
		} else {
			int index = 0;
			json.set("amount", count);
			count = wish_dao.count();
			JSONArray wish_json = new JSONArray(false);
			while(index < 10) {
				if(count-start == 0) break;
				
				Wish wish = wish_dao.selectById(count-start++);
				if(wish.getIs_get() == 1 || wish.getIs_delete() == 1) continue;
				
				JSONArray ja = this.getWishJson(wish);
				wish_json.set(ja);
				index++;
			}
			json.set("code", 1);
			json.set("data", wish_json);
		}
		
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
	
	private JSONArray getWishJson(Wish wish) {
		JSONArray json = new JSONArray(false);
		json.set("title", wish.getTitle());
		json.set("description", wish.getDescription());
		return json;
	}
}
