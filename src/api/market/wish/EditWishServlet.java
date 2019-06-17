package api.market.wish;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.form.FormOperator;
import dao.WishDAO;
import dao.impl.WishDAOImpl;
import dao.vo.Wish;

@SuppressWarnings("serial")
public class EditWishServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EditWishServlet() {
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
		
		JSONArray json = new JSONArray();
		WishDAO wish_dao = new WishDAOImpl();
		if(request.getParameter("is_delete") != null) {
			// 这是一个删除性质的修改
			int wish_id = Integer.parseInt(request.getParameter("wish_id"));
			wish_dao.deleteById(wish_id);
			
			json.set("code", 1);
			json.set("msg", "删除成功");
		} else {
			Wish wish = new Wish();
			new FormOperator<>(wish, request);
			
			Wish oldWish = wish_dao.selectById(wish.getWish_id());
			wish.setId(oldWish.getId());
			wish.setIs_get(oldWish.getIs_get());
			wish.setIs_delete(oldWish.getIs_delete());
			wish.setTime(oldWish.getTime());
			
			wish_dao.update(wish);
			
			json.set("code", 1);
			json.set("msg", "编辑成功");
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
