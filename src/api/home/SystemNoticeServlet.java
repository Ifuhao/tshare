package api.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import dao.SysNoticeAcceptDAO;
import dao.SysNoticeDAO;
import dao.impl.SysNoticeAcceptDAOImpl;
import dao.impl.SysNoticeDAOImpl;
import dao.vo.SysNotice;
import dao.vo.SysNoticeAccept;
import dao.vo.User;

@SuppressWarnings("serial")
public class SystemNoticeServlet extends HttpServlet {
	private static final int PAGESIZE = 10;
	private SysNoticeDAO sn_dao = new SysNoticeDAOImpl();

	/**
	 * Constructor of the object.
	 */
	public SystemNoticeServlet() {
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
			
			SysNoticeAcceptDAO sna_dao = new SysNoticeAcceptDAOImpl();
			SysNoticeAccept[] sna = sna_dao.selectByAddress(user.getId());
			
			if(sna == null) {
				// 没有系统通知
				json.set("code", 0);
				json.set("msg", "暂无系统通知");
			} else {
				JSONArray sna_json = new JSONArray(false);
				
				int start = sna.length-(page-1)*PAGESIZE;
				int end = sna.length-page*PAGESIZE;
				end = end<0?0:end;
				
				for(int i=start-1;i>=end;i--) {
					sna_json.set(this.getSnaJson(sna[i]));
				}
				
				json.set("code", 1);
				json.set("data", sna_json);
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

	private JSONArray getSnaJson(SysNoticeAccept sna) {
		JSONArray json = new JSONArray();
		SysNotice sn = sn_dao.selectById(sna.getSys_id());
		json.set("sys_id", sna.getSys_id());
		json.set("sender_name", "系统管理员");
		json.set("title", sn.getTitle());
		json.set("content", sn.getContent());
		json.set("time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(sn.getTime())));
		json.set("is_read", sna.getIs_read());
		return json;
	}
}
