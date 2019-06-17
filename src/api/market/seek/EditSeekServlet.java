package api.market.seek;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.form.FormOperator;
import dao.SeekDAO;
import dao.impl.SeekDAOImpl;
import dao.vo.Seek;

@SuppressWarnings("serial")
public class EditSeekServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EditSeekServlet() {
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
		SeekDAO seek_dao = new SeekDAOImpl();
		if(request.getParameter("is_delete") != null) {
			// 这是一个删除性质的修改
			int seek_id = Integer.parseInt(request.getParameter("seek_id"));
			seek_dao.deleteById(seek_id);
			
			json.set("code", 1);
			json.set("msg", "删除成功");
		} else {
			Seek seek = new Seek();
			new FormOperator<Seek>(seek, request);
			
			
			Seek oldSeek = seek_dao.selectById(seek.getSeek_id());
			
			seek.setIs_buy(oldSeek.getIs_buy());
			seek.setIs_delete(oldSeek.getIs_delete());
			seek.setTime(oldSeek.getTime());
			seek.setId(oldSeek.getId());
			
			seek_dao.update(seek);
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
