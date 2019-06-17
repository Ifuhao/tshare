package api.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.PerGroupDAO;
import dao.PerNoticeDAO;
import dao.UserDAO;
import dao.impl.PerGroupDAOImpl;
import dao.impl.PerNoticeDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.PerGroup;
import dao.vo.User;

@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet {
	private static final int PAGESIZE = 10;
	private UserDAO user_dao = new UserDAOImpl();
	private PerNoticeDAO pn_dao = new PerNoticeDAOImpl();
	private PerGroupDAO pg_dao = new PerGroupDAOImpl();
	/**
	 * Constructor of the object.
	 */
	public MessageServlet() {
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
			// 用户未登录
			json.set("code", 0);
			json.set("msg", "用户未登录");
		} else {
			int page = Integer.parseInt(request.getParameter("page"));
			String type = request.getParameter("type");
			int group_type = PerGroup.TYPE_TRANSACTION;
			if(type.equals("person")) {
				group_type = PerGroup.TYPE_PERSONAL;
			}
			
			PerGroup pg[] = pg_dao.selectByUser(user.getId(), group_type);
			if(pg == null) {
				// 没有新消息
				json.set("code", 0);
				json.set("msg", "没有新消息");
			} else {
				JSONArray pg_json = new JSONArray(false);
				
				int start = (page-1)*PAGESIZE;
				int end = page*PAGESIZE;
				end = end>pg.length?pg.length:end;
				for(int i=start;i<end;i++) {
					pg_json.set(this.getPgJson(pg[i], user.getId(), group_type));
				}
				
				json.set("code", 1);
				json.set("data", pg_json);
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
	
	private JSONArray getPgJson(PerGroup pg, String user, int type) {
		JSONArray json = new JSONArray();
		json.set("group", this.getGroupJson(pg, user, type));
		json.set("sender", this.getSenderJson(pg, user));
		json.set("address", this.getAddressJson(user));
		return json;
	}
	
	private JSONArray getGroupJson(PerGroup pg, String user, int type) {
		JSONArray json = new JSONArray();
		json.set("group_id", pg.getGroup_id());
		json.set("last_msg", pn_dao.getLastestMsg(pg.getGroup_id()).getContent());
		json.set("is_read", pg_dao.hasNew(pg.getGroup_id(), user, type)==true?1:0);
		return json;
	}
	
	private JSONArray getSenderJson(PerGroup pg, String user) {
		JSONArray json = new JSONArray();
		User u = user_dao.selectById(pg.getUser1().equals(user)?pg.getUser2():pg.getUser1());
		json.set("head_image", u.getHead_image());
		json.set("name", u.getUsername());
		json.set("id", u.getId());
		return json;
	}
	
	
	private JSONArray getAddressJson(String user) {
		JSONArray json = new JSONArray();
		json.set("head_image", user_dao.selectById(user).getHead_image());
		return json;
	}
}
