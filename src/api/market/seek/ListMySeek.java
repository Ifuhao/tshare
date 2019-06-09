package api.market.seek;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.SeekDAO;
import dao.impl.SeekDAOImpl;
import dao.vo.Seek;
import dao.vo.User;

@SuppressWarnings("serial")
public class ListMySeek extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ListMySeek() {
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
		JSONArray json = new JSONArray(false);
		if(user == null) {
			// 用户没有登录
		} else {
			// 查询我的收购(10条记录一页)
			int page = Integer.parseInt(request.getParameter("page"));
			
			SeekDAO seek_dao = new SeekDAOImpl();
			HashMap<String, String> map = new HashMap<>();
			map.put("id", user.getId());
			map.put("is_buy", "0");
			map.put("is_delete", "0");
			Seek[] seeks = seek_dao.selectByCond(map);
			if(seeks == null) {
				// 没有查询到记录
				json.set("code", 0);
				json.set("msg", "您暂时没有收购记录");
			} else if(10*(page-1) > seeks.length) {
				// 没有这一页
				json.set("code", 0);
				json.set("msg", "没有这一页");
			} else {
				json.set("code", 1);
				json.set("amount", seeks.length);
				
				int start = 10*(page-1);
				int end = 10*page;
				end = end>seeks.length?seeks.length:end;
				JSONArray seek_json = new JSONArray(false);
				for(int i=start;i<end;i++) {
					JSONArray js = this.getSeekJson(seeks[i]);
					seek_json.set(js);
				}
				json.set("data", seek_json);
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
	
	private JSONArray getSeekJson(Seek seek) {
		JSONArray json = new JSONArray(false);
		json.set("name", seek.getTitle());
		json.set("description", seek.getDescription());
		json.set("time", seek.getTime().toString());
		return json;
	}
}
