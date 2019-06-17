package api.market.wish;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import dao.WishDAO;
import dao.impl.WishDAOImpl;
import dao.vo.User;
import dao.vo.Wish;

@SuppressWarnings("serial")
public class ListMyWish extends HttpServlet {
	private static final int PAGESIZE = 30;

	/**
	 * Constructor of the object.
	 */
	public ListMyWish() {
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
		
		User user = (User) request.getSession(true).getAttribute("user");
		JSONArray json = new JSONArray();
		
		if(user == null) {
			// 用户没有登录
			json.set("code", 0);
			json.set("msg", "用户没有登录");
		} else {
			int page = Integer.parseInt(request.getParameter("page"));
			WishDAO wish_dao = new WishDAOImpl();
			
			HashMap<String, String> map = new HashMap<>();
			map.put("id", user.getId());
			map.put("is_get", "0");
			map.put("is_delete", "0");
			Wish[] wishes = wish_dao.selectByCond(map);
			if(wishes == null) {
				// 暂无心愿单
				json.set("code", 0);
				json.set("msg", "暂无心愿单");
			} else {
				// 进行分页
				int start = (page-1)*PAGESIZE;
				if(start >= wishes.length) {
					// 该页不存在
					json.set("code", 0);
					json.set("msg", "该页不存在");
				} else {
					JSONArray wish_json = new JSONArray(false);
					int end = page*PAGESIZE;
					end = end>wishes.length?wishes.length:end;
					
					for(int i=start;i<end;i++) {
						wish_json.set(this.getWishJson(wishes[i]));
					}
					json.set("code", 1);
					json.set("data", wish_json);
				}
			}
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
		json.set("wish_id", wish.getWish_id());
		json.set("title", wish.getTitle());
		json.set("description", wish.getDescription());
		return json;
	}
}
