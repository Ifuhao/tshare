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
import dao.UserDAO;
import dao.impl.DlFileDAOImpl;
import dao.impl.FileDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.DlFile;
import dao.vo.User;

@SuppressWarnings("serial")
public class ListMyDownload extends HttpServlet {
	private static final int PAGESIZE = 10;
	
	private FileDAO file_dao = new FileDAOImpl();
	
	private UserDAO user_dao = new UserDAOImpl();
	
	/**
	 * Constructor of the object.
	 */
	public ListMyDownload() {
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
			DlFileDAO dl_dao = new DlFileDAOImpl();
			HashMap<String, String> map = new HashMap<>();
			map.put("id", user.getId());
			DlFile dls[] = dl_dao.selectByCond(map);
			
			if(dls == null) {
				// 没有下载的文件
				json.set("code", 0);
				json.set("msg", "没有下载的文件");
			} else {
				int page = Integer.parseInt(request.getParameter("page"));
				int start = (page-1)*PAGESIZE;
				int end = page*PAGESIZE;
				end = end>dls.length?dls.length:end;
				
				JSONArray dl_json = new JSONArray(false);
				for(int i=start;i<end;i++) {
					dl_json.set(this.getDlJson(dls[i]));
				}
				json.set("code", 1);
				json.set("data", dl_json);
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
	
	private JSONArray getDlJson(DlFile dl) {
		JSONArray json = new JSONArray();
		json.set("did", dl.getDid());
		json.set("name", file_dao.selectByFilename(dl.getFilename()).getName());
		json.set("upload_name", user_dao.selectById(dl.getId()).getUsername());
		json.set("download_time", dl.getTime());
		json.set("is_mark", dl.getIsmark());
		json.set("score", dl.getScore());
		return json;
	}
}
