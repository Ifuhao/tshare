package api.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.PerNoticeDAO;
import dao.impl.PerNoticeDAOImpl;
import dao.vo.PerNotice;
import dao.vo.User;

@SuppressWarnings("serial")
public class MessageDetailServlet extends HttpServlet {
	private static final int PAGESIZE = 5;

	/**
	 * Constructor of the object.
	 */
	public MessageDetailServlet() {
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
			json.set("code", 0);
			json.set("msg", "用户未登录");
		} else {
			int page = Integer.parseInt(request.getParameter("page"));
			int group_id = Integer.parseInt(request.getParameter("group_id"));
			
			PerNoticeDAO pn_dao = new PerNoticeDAOImpl();
			
			int start = (page-1)*PAGESIZE;
			PerNotice pn[] = pn_dao.getNotices(group_id, start, PAGESIZE);
			if(pn == null) {
				// 没有更多的消息了
				json.set("code", 0);
				json.set("msg", "没有更多的消息了");
			} else {
				JSONArray msg_json = new JSONArray(false);
				for(int i=0;i<pn.length;i++) {
					msg_json.set(this.getMsgJson(pn[i], user.getId()));
				}
				json.set("code", 1);
				json.set("data", msg_json);
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
	
	private JSONArray getMsgJson(PerNotice pn, String user) {
		JSONArray json = new JSONArray();
		json.set("content", pn.getContent());
		json.set("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(pn.getTime())));
		json.set("is_me", pn.getSender().equals(user)?1:0);
		return json;
	}

}
