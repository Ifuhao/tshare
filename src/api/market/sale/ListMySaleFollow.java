package api.market.sale;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import dao.SaleDAO;
import dao.SaleFollowDAO;
import dao.impl.SaleDAOImpl;
import dao.impl.SaleFollowDAOImpl;
import dao.vo.Sale;
import dao.vo.SaleFollow;
import dao.vo.User;

@SuppressWarnings("serial")
public class ListMySaleFollow extends HttpServlet {
	private static final int PAGESIZE = 10;
	private SaleDAO sale_dao = new SaleDAOImpl();

	/**
	 * Constructor of the object.
	 */
	public ListMySaleFollow() {
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
			json.set("msg", "用户未登录");
		} else {
			int page = Integer.parseInt(request.getParameter("page"));
			SaleFollowDAO dao = new SaleFollowDAOImpl();
			SaleFollow[] follows = dao.selectById(user.getId());

			if(follows == null) {
				// 暂无关注
				json.set("code", 0);
				json.set("msg", "暂无关注");
			} else {
				JSONArray follow_json = new JSONArray(false);
				int start = (page-1)*PAGESIZE;
				int end = page*PAGESIZE;
				end = end>follows.length?follows.length:end;
				
				for(int i=start;i<end;i++) {
					JSONArray ja = this.getFollowJson(follows[i]);
					follow_json.set(ja);
				}
				
				json.set("code", 1);
				json.set("data", follow_json);
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

	private JSONArray getFollowJson(SaleFollow follow) {
		JSONArray json = new JSONArray();
		Sale sale = sale_dao.selectById(follow.getSale_id());
		json.set("title", sale.getTitle());
		json.set("sale_id", sale.getSale_id());
		String picture = sale.getPicture();
		String[] split = picture.split(";");
		json.set("main_pic", split[0]);
		json.set("price", sale.getPrice());
		return json;
	}
}
