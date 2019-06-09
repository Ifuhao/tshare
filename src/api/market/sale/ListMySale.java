package api.market.sale;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.SaleDAO;
import dao.impl.SaleDAOImpl;
import dao.vo.Sale;
import dao.vo.User;

@SuppressWarnings("serial")
public class ListMySale extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ListMySale() {
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
		if(user == null) {
			// 用户没有登录
		} else {
			// 根据用户邮箱查询用户的上架信息
			int page = Integer.parseInt(request.getParameter("page"));
			
			SaleDAO sale_dao = new SaleDAOImpl();
			HashMap<String, String> map = new HashMap<>();
			map.put("id", user.getId());
			map.put("is_sell", "0");
			map.put("is_delete", "0");
			Sale[] mysale = sale_dao.selectByCond(map);
			
			JSONArray json = new JSONArray();
			if(mysale == null) {
				// 没有上架信息
				json.set("code", 0);
				json.set("msg", "暂无上架商品");
			} else {
				// 返回所有的上架信息，按页显示，一页10项
				if(12*(page-1) > mysale.length) {
					// 没有这一页
					json.set("code", 0);
					json.set("msg", "没有这一页");
				} else {
					int start = 12*(page-1);
					int end = 12*page;
					if(end > mysale.length) {
						end = mysale.length;
					}
					
					JSONArray sale_json = new JSONArray(false);
					for(int i=start;i<end;i++) {
						sale_json.set(this.getSaleJson(mysale[i]));
					}
					json.set("sale", sale_json);
					json.set("amount", mysale.length);
				}
			}
			
			PrintWriter out = response.getWriter();
			out.write(JSONParser.json_encode(json));
			out.close();
		}
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
	
	
	private JSONArray getSaleJson(Sale sale) {
		JSONArray json = new JSONArray();
		json.set("sale_id", sale.getSale_id());
		json.set("title", sale.getTitle());
		json.set("price", sale.getPrice());
		json.set("view", sale.getView());
		json.set("buy_price", sale.getBuy_price());
		json.set("time", sale.getTime().toString());
		String picture = sale.getPicture();
		String[] split = picture.split(";");
		json.set("main_pic", split[0]);
		return json;
	}
}
