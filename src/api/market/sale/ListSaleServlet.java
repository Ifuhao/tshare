package api.market.sale;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.SaleDAO;
import dao.UserDAO;
import dao.impl.SaleDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.Sale;

@SuppressWarnings("serial")
public class ListSaleServlet extends HttpServlet {
	private UserDAO user_dao = new UserDAOImpl();
	private static final Integer PAGESIZE = 20;

	/**
	 * Constructor of the object.
	 */
	public ListSaleServlet() {
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
		
		SaleDAO sale_dao = new SaleDAOImpl();
		int count = sale_dao.effectCount();
		int start = (page-1)*PAGESIZE;
		
		JSONArray json = new JSONArray();
		if(start >= count) {
			// 不存在这一页
			json.set("code", 0);
			json.set("msg", "该页不存在");
		} else {
			int index = 0;
			json.set("amount", count);
			count = sale_dao.count();
			JSONArray sale_json = new JSONArray(false);
			
			while(index < PAGESIZE) {
				if(count-start == 0) break;
				Sale sale = sale_dao.selectById(count-start++);
				if(sale.getIs_sell() == 1 || sale.getIs_delete() == 1) continue;
				
				JSONArray ja = this.getSaleJson(sale);
				sale_json.set(ja);
				index++;
			}
			
			json.set("code", 1);
			json.set("data", sale_json);
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
	
	private JSONArray getSaleJson(Sale sale) {
		JSONArray json = new JSONArray();
		json.set("sale_id", sale.getSale_id());
		json.set("title", sale.getTitle());
		json.set("description", sale.getDescription());
		json.set("price", sale.getPrice());
		json.set("buy_price", sale.getBuy_price());
		json.set("view", sale.getView());
		json.set("time", sale.getTime().toString());
		json.set("uid", sale.getId());
		json.set("uname", user_dao.selectById(sale.getId()).getUsername());
		String picture = sale.getPicture();
		String[] split = picture.split(";");
		json.set("main_pic", split[0]);
		return json;
	}
}
