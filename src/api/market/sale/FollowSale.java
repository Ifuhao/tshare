package api.market.sale;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import dao.SaleFollowDAO;
import dao.impl.SaleFollowDAOImpl;
import dao.vo.SaleFollow;
import dao.vo.User;

@SuppressWarnings("serial")
public class FollowSale extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FollowSale() {
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
			int sale_id = Integer.parseInt(request.getParameter("sale_id"));
			int is_follow = Integer.parseInt(request.getParameter("is_follow"));
			
			SaleFollowDAO dao = new SaleFollowDAOImpl();
			
			if(is_follow == 1) {
				SaleFollow obj = new SaleFollow();
				obj.setId(user.getId());
				obj.setSale_id(sale_id);
				obj.setFollow_id(dao.count()+1);
				
				dao.insert(obj);
				json.set("code", 1);
				json.set("msg", "关注成功");
			} else {
				// 取消关注
				SaleFollow sf[] = dao.selectById(user.getId());
				if(sf == null) {
					// 原本就没有关注过这个商品
					json.set("code", 0);
					json.set("msg", "用户没有关注过");
				} else {
					int follow_id = 0;
					for(int i=0;i<sf.length;i++) {
						if(sf[i].getSale_id() == sale_id) {
							follow_id = sf[i].getFollow_id();
							break;
						}
					}
					dao.deleteById(follow_id);
					json.set("json", 1);
					json.set("msg", "取消关注");
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

}
