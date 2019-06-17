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
import dao.UserDAO;
import dao.impl.SaleDAOImpl;
import dao.impl.SaleFollowDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.Sale;
import dao.vo.SaleFollow;
import dao.vo.User;

@SuppressWarnings("serial")
public class SaleDetailServlet extends HttpServlet {
	private SaleFollowDAO sf_dao = new SaleFollowDAOImpl();

	/**
	 * Constructor of the object.
	 */
	public SaleDetailServlet() {
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
		
		User me = (User) request.getSession(true).getAttribute("user");
		JSONArray json = new JSONArray(false);
		if(me == null) {
			// 用户没有
			json.set("code", 0);
			json.set("msg", "用户未登录");
		} else {
			// 获取商品的详细信息和商品出售者的信息
			SaleDAO sale_dao = new SaleDAOImpl();
			int sale_id = Integer.parseInt(request.getParameter("sale_id"));
			Sale sale = sale_dao.selectById(sale_id);
			
			// 获取出售者信息
			UserDAO user_dao = new UserDAOImpl();
			User user = user_dao.selectById(sale.getId());
			
			
			JSONArray sale_json = this.getSaleJson(sale, me);
			JSONArray user_json = this.getUserJson(user);
			
			json.set("code", 1);
			json.set("sale", sale_json);
			json.set("user", user_json);
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
	
	
	private JSONArray getSaleJson(Sale sale, User user) {
		JSONArray json = new JSONArray();
		json.set("title", sale.getTitle());
		json.set("description", sale.getDescription());
		json.set("category", sale.getCategory());
		json.set("num", sale.getNum());
		json.set("buy_price", sale.getBuy_price());
		json.set("buy_way", sale.getBuy_way());
		json.set("sale_new", sale.getSale_new());
		json.set("price", sale.getPrice());
		json.set("bargain", sale.getBargain());
		json.set("delivery", sale.getDelivery());
		json.set("view", sale.getView());
		
		SaleFollow sf[] = sf_dao.selectById(user.getId());
		json.set("is_follow", 0);
		if(sf != null) {
			for(int i=0;i<sf.length;i++) {
				if(sf[i].getSale_id() == sale.getSale_id()) {
					json.set("is_follow", 1);
					break;
				}
			}
		}
		
		String picture = sale.getPicture();
		String[] split = picture.split(";");
		JSONArray pic = new JSONArray(false);
		for(int i=0;i<split.length;i++) {
			if(!split[i].equals("")) {
				pic.set(split[i]);
			}
		}
		json.set("picture", pic);
		return json;
	}
	
	private JSONArray getUserJson(User user) {
		JSONArray json = new JSONArray();
		json.set("uid", user.getId());
		json.set("head_image", user.getHead_image());
		json.set("uname", user.getUsername());
		return json;
	}
}
