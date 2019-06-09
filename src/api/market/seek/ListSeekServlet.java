package api.market.seek;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.SeekDAO;
import dao.UserDAO;
import dao.impl.SeekDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.Seek;

@SuppressWarnings("serial")
public class ListSeekServlet extends HttpServlet {
	private UserDAO user_dao = new UserDAOImpl();
	
	/**
	 * Constructor of the object.
	 */
	public ListSeekServlet() {
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
		JSONArray json = new JSONArray(false);
		
		SeekDAO seek_dao = new SeekDAOImpl();
		int count = seek_dao.effectCount();
		int start = (page-1)*10;
		if(start >= count) {
			// 该页不存在
			json.set("code", 0);
			json.set("msg", "该页不存在");
		} else {
			int index = 0;
			json.set("amount", count);
			count = seek_dao.count();
			JSONArray seek_json = new JSONArray(false);
			
			while(index < 10) {
				if(count-start == 0) {
					break;
				}
				Seek seek = seek_dao.selectById(count-start++);
				if(seek.getIs_buy() == 1 || seek.getIs_delete() == 1) {
					continue;
				}
				JSONArray ja = this.getSeekJson(seek);
				seek_json.set(ja);
				index++;
			}
			json.set("code", 1);
			json.set("data", seek_json);
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
		json.set("uid", seek.getId());
		json.set("uname", user_dao.selectById(seek.getId()).getUsername());
		json.set("title", seek.getTitle());
		json.set("description", seek.getDescription());
		json.set("time", seek.getTime().toString());
		return json;
	}
}
