package api.market.sale;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.servlet.ServletException;
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

@SuppressWarnings("serial")
public class EditSaleServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EditSaleServlet() {
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
		
		if(request.getParameter("is_delete") != null) {
			// 这是一个删除性质的修改
			int sale_id = Integer.parseInt(request.getParameter("sale_id"));
			SaleDAO sale_dao = new SaleDAOImpl();
			sale_dao.deleteById(sale_id);
			
			JSONArray json = new JSONArray();
			json.set("code", 1);
			json.set("msg", "删除成功");
			PrintWriter out = response.getWriter();
			out.write(JSONParser.json_encode(json));
			out.close();
		} else {
			Sale sale = new Sale();
			// 参考图片的父目录：D:/tomcat/upload/picture/xxx.image
			String base = "D:/tomcat/upload/picture/";
			
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
			
			Iterator<String> iter = map.keySet().iterator();
			String newpic = "";		// 图片路径：xxx.image
			LinkedList<String> delpic = new LinkedList<>();
			
			while(iter.hasNext()) {
				String key = iter.next();
				String value = map.get(key);
				if(key.startsWith("delpic")) {
					// 删除图片
					delpic.add(value);
					// 从物理路径删除
					System.out.println(base+value);
					File file = new File(base + value);
					file.delete();
				} else if(key.startsWith("newpic")) {
					// 新增图片
					String uuid = UUID.randomUUID().toString();
					String[] split = value.split(",");
					String image_type = split[0].substring(split[0].indexOf("/")+1, split[0].indexOf(";"));
					String filename = uuid + "." + image_type;
					FileUtils.base64ToFile(split[1], base + filename);
					newpic += filename + ";";
				} else {
					// 填写Sale对象属性
					String field = "Sale." + key;
					new BeanOperator(sale, field, value);
				}
			}
			
			SaleDAO sale_dao = new SaleDAOImpl();
			
			// 获取数据库中该编号的商品，比较参考图片的变化，计算出变化后的图片路径
			Sale oldSale = sale_dao.selectById(sale.getSale_id());
			String oldpic = oldSale.getPicture();
			
			for(int i=0;i<delpic.size();i++) {
				// 只要是delpic链表保存的图片都应该删除
				int delIndex = oldpic.indexOf(delpic.get(i));
				oldpic = oldpic.substring(0, delIndex) + oldpic.substring(delIndex+delpic.get(i).length()+1);
			}
			
			if(oldpic.length()==1) {
				// 所有的图片都被删除了，只剩下一个分号
				oldpic = newpic;
			} else {
				oldpic += newpic;
			}
			
			sale.setPicture(oldpic.replace("/", "\\"));
			sale.setId(oldSale.getId());
			sale.setTime(oldSale.getTime());
			sale.setIs_sell(oldSale.getIs_sell());
			sale.setIs_delete(oldSale.getIs_delete());
			
			// 数据库操作
			sale_dao.update(sale);
			
			JSONArray json = new JSONArray();
			json.set("code", 1);
			json.set("msg", "编辑成功");
			PrintWriter out = response.getWriter();
			out.write(JSONParser.json_encode(json));
			out.close();
		}
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
