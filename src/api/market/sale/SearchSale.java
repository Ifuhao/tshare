package api.market.sale;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.WordAnalysis;
import dao.SaleDAO;
import dao.UserDAO;
import dao.impl.SaleDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.Sale;

@SuppressWarnings("serial")
public class SearchSale extends HttpServlet {
	private LinkedList<String> keyList = new LinkedList<>();
	private LinkedList<Sale> saleList = new LinkedList<>();
	private long allow_time = 0;
	private long now = 0;
	private SaleDAO dao = new SaleDAOImpl();
	private UserDAO udao = new UserDAOImpl();
	
	/**
	 * Constructor of the object.
	 */
	public SearchSale() {
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
		
		// 关键字
		String key = request.getParameter("key");
		
		// 日期要求(0全部/1一天内/2一周内/3一月内/4一年内)
		int date = Integer.parseInt(request.getParameter("date"));
		
		// 类型要求
		String type = request.getParameter("type");
		type = type.equals("全部")?"":type;
		
		// 显示第几页
		int page = Integer.parseInt(request.getParameter("page"));
		
		// 是否全新(0不是/1是)
		int sale_new = Integer.parseInt(request.getParameter("sale_new"));
		
		// 是否配送(0不是/1是)
		int delivery = Integer.parseInt(request.getParameter("delivery"));
		
		// 是否可议价
		int bargain = Integer.parseInt(request.getParameter("bargain"));
		
		this.keyList.clear();
		this.saleList.clear();
		
		// 根据日期进行筛选
		this.now = new Date().getTime();		// 当前时间(单位ms)
		this.allow_time = Long.MAX_VALUE;
		switch(date) {
		case 1:		// 一天内
			this.allow_time = 24*60*60*1000;
			break;
		case 2:		// 一周内
			this.allow_time = 7*24*60*60*1000;
			break;
		case 3:		// 一月内
			this.allow_time = 30*24*60*60*1000;
			break;
		case 4:		// 一年内
			this.allow_time = 365*24*60*60*1000;
			break;
		}
		
		// 完全匹配搜索
		this.search(key, type, sale_new!=0, delivery, bargain);
		
		// 分词匹配搜索
		String keys[] = WordAnalysis.split(key);
		for(int i=0;i<keys.length;i++) {
			this.search(keys[i], type, sale_new!=0, delivery, bargain);
		}
		
		JSONArray json = new JSONArray();
		if(saleList.size() == 0) {
			// 没有搜到任何结果
			json.set("code", 0);
			json.set("msg", "搜索不到");
		} else {
			// 进行分页(12项一页)
			LinkedList<Sale[]> list = new LinkedList<>();
			Sale json_data[] = new Sale[12];
			for(int i=0;i<saleList.size();i++) {
				if(i%12 == 0 && i != 0) {
					list.add(json_data);
					json_data = new Sale[10];
				}
				
				json_data[i%12] = saleList.get(i);
				if(i==saleList.size()-1) {
					list.add(json_data);
				}
			}
			
			if(page > list.size()) {
				// 没有这一页
				json.set("code", 0);
				json.set("msg", "不存在这一页");
			} else {
				LinkedList<JSONArray> jsonSales = new LinkedList<>();
				for(int i=0;i<list.get(page-1).length;i++) {
					if(list.get(page-1)[i] == null) {
						break;
					} else {
						jsonSales.add(this.getJsonSale(list.get(page-1)[i]));
					}
				}
				
				JSONArray dataArray = JSONArray.arrayToJSONArray(jsonSales);
				
				json.set("code", 1);
				json.set("amount", saleList.size());
				json.set("data", dataArray);
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
	
	private void search(String keyword, String type, boolean sale_new, int delivery, int bargain) {
		String field[] = new String[]{"title", "category", "description"};
		for(int i=0;i<field.length;i++) {
			Sale[] sales = this.dao.select(field[i], keyword, type, sale_new, delivery, bargain);
			this.store(sales, keyword);
		}
	}
	
	private void store(Sale sales[], String keyword) {
		if(sales == null) return;
		for(int i=0;i<sales.length;i++) {
			if(this.now - sales[i].getTime().getTime() > this.allow_time) {
				// 时间要求不符合，跳过这条记录
				continue;
			}
			
			int index = this.saleList.indexOf(sales[i]);
			if(index == -1) {
				this.keyList.add(keyword);
				this.saleList.add(sales[i]);
			} else {
				String oldKeyword = this.keyList.get(index);
				if(keyword.indexOf(oldKeyword) != -1) {
					// 新的关键字包含旧的关键字，进行替换
					this.keyList.set(index, keyword);
				} else if(oldKeyword.indexOf(keyword) == -1) {
					// 两个关键字没有互相包含的关系，则进行合并
					String newKeyword = oldKeyword + keyword;
					this.keyList.set(index, newKeyword);
				}
			}
		}
	}
	
	/**
	 * 编号：sale_id
	 * 标题：title
	 * 图片：picture
	 * 价格：price
	 * 浏览量：view
	 */
	private JSONArray getJsonSale(Sale sale) {
		JSONArray array = new JSONArray();
		array.set("sale_id", sale.getSale_id());
		array.set("title", sale.getTitle());
		array.set("uid", sale.getId());
		array.set("uname", this.udao.selectById(sale.getId()).getUsername());
		
		String split[] = sale.getPicture().split(";");
		array.set("main_pic", split[0]);
		array.set("buy_price", sale.getBuy_price());
		array.set("price", sale.getPrice());
		array.set("view", sale.getView());
		
		return array;
	}
}
