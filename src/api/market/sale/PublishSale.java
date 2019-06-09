package api.market.sale;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.file.FileUtils;
import utils.form.BeanOperator;
import dao.SaleDAO;
import dao.impl.SaleDAOImpl;
import dao.vo.Sale;
import dao.vo.User;

@MultipartConfig
@SuppressWarnings("serial")
public class PublishSale extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PublishSale() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		User user = ((User)request.getSession(true).getAttribute("user"));
		if(user == null) {
			// 用户不在登录状态
			
		}
		
		// 参考图片的父目录：/tshare/market/picture/sale_id/xxx.image
		String base = request.getSession().getServletContext().getRealPath("")
				+ "market" + File.separator + "picture" + File.separator;
		
		BufferedReader reader = request.getReader();
		String msg=null;
		String line;
		while((line=reader.readLine())!=null)
			msg+=line;
		
		// 通过分割msg来获取参数
		String findstr = "form-data; name=\"";
		int length = findstr.length();
		
		HashMap<String, String> map = new HashMap<>();		// 保存提交的数据
		
		// 开始分割
		int start = msg.indexOf(findstr);
		while(start != -1) {
			int end = msg.indexOf("\"", start + length + 1);
			String key = msg.substring(start+length, end);
			String value = msg.substring(end+1, msg.indexOf("-----", end+1));
			map.put(key, value);
			
			start = msg.indexOf(findstr, end+1);
		}
		
		// 获取提交数据并生成Sale对象
		SaleDAO sale_dao = new SaleDAOImpl();
		Sale sale = new Sale();
		sale.setSale_id(sale_dao.count()+1);
		sale.setId(user.getId());
		
		base += sale.getSale_id() + File.separator;
		File baseDir = new File(base);
		if(!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		Iterator<String> iter = map.keySet().iterator();
		String picture = "";		// 图片路径：xxx.image
		
		while(iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);
			if(key.startsWith("pic")) {
				// 图片，单独处理
				String uuid = UUID.randomUUID().toString();
				String[] split = value.split(",");
				String image_type = split[0].substring(split[0].indexOf("/")+1, split[0].indexOf(";"));
				String filename = uuid + "." + image_type;
				FileUtils.base64ToFile(split[1], base + filename);
				
				picture += filename + ";";
			} else {
				// 填写Sale对象属性
				String field = "Sale." + key;
				new BeanOperator(sale, field, value);
			}
		}
		
		sale.setPicture(picture);
		sale.setTime(new Date(new java.util.Date().getTime()));

		sale_dao.insert(sale);
		
		JSONArray json = new JSONArray();
		json.set("code", 1);
		json.set("msg", "上架成功");
		PrintWriter out = response.getWriter();
		out.write(JSONParser.json_encode(json));
		out.close();
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
