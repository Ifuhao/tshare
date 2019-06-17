package api.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JSONParser;
import json.JSONArray;
import dao.DlFileDAO;
import dao.FileDAO;
import dao.impl.DlFileDAOImpl;
import dao.impl.FileDAOImpl;
import dao.vo.DlFile;
import dao.vo.File;
import dao.vo.User;

@SuppressWarnings("serial")
public class FileMarkServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FileMarkServlet() {
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
			json.set("code", 0);
			json.set("msg", "用户没有登录");
		} else {
			int did = Integer.parseInt(request.getParameter("did"));
			int score = Integer.parseInt(request.getParameter("score"));
			
			// 更新文件下载表
			DlFileDAO dl_dao = new DlFileDAOImpl();
			DlFile df = dl_dao.selectByDid(did);
			df.setScore(score);
			df.setIsmark(1);
			dl_dao.update(df);
			
			// 更新文件表
			HashMap<String, String> map = new HashMap<>();
			map.put("filename", df.getFilename());
			map.put("ismark", "1");
			
			DlFile[] dfs = dl_dao.selectByCond(map);
			int sumScore = 0;
			for(int i=0;i<dfs.length;i++) {
				sumScore += dfs[i].getScore();
			}
			
			FileDAO file_dao = new FileDAOImpl();
			File file = file_dao.selectByFilename(df.getFilename());
			file.setScore(sumScore/dfs.length);
			
			file_dao.update(file);
			
			json.set("code", 1);
			json.set("msg", "评分成功");
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
