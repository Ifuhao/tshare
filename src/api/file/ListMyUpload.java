package api.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.file.FileUtils;
import dao.FileDAO;
import dao.UserDAO;
import dao.impl.FileDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.File;
import dao.vo.User;

@SuppressWarnings("serial")
public class ListMyUpload extends HttpServlet {
	private static final int PAGESIZE = 10;
	private String real = "D:/tomcat/upload/file/";
	private UserDAO user_dao = new UserDAOImpl();

	/**
	 * Constructor of the object.
	 */
	public ListMyUpload() {
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
		// 列出我上传的文件，仅仅列出顶层文件（如果是文件夹则以压缩文件显示）
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		User user = (User) request.getSession(true).getAttribute("user");
		JSONArray json = new JSONArray();
		if(user == null) {
			// 用户没有登录
			json.set("code", 0);
			json.set("msg", "用户没有登录");
		} else {
			// 查询
			FileDAO file_dao = new FileDAOImpl();
			HashMap<String, String> map = new HashMap<>();
			map.put("id", user.getId());
			File[] files = file_dao.selectByCond(map);
			
			if(files == null) {
				json.set("code", 0);
				json.set("msg", "暂未上传文件");
			} else {
				// 过滤非顶层文件
				LinkedList<File> dest = new LinkedList<>();
				for(int i=0;i<files.length;i++) {
					if(files[i].getFilename().split("/").length == 4) {
						// 顶层文件
						dest.add(files[i]);
					}
				}
				int page = Integer.parseInt(request.getParameter("page"));
				int start = (page-1)*PAGESIZE;
				
				if(start >= dest.size()) {
					json.set("code", 0);
					json.set("msg", "该页不存在");
				} else {
					int end = page*PAGESIZE;
					end = end>dest.size()?dest.size():end;
					
					JSONArray file_json = new JSONArray(false);
					for(int i=start;i<end;i++) {
						file_json.set(this.getFileJson(dest.get(i)));
					}
					
					json.set("code", 1);
					json.set("data", file_json);
				}
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
	
	private JSONArray getFileJson(File file) {
		JSONArray json = new JSONArray();
		
		String filename = file.getFilename();
		json.set("url", filename);
		
		json.set("category", file.getCategory());
		json.set("subject", file.getSubject());
		json.set("type", file.getType());
		json.set("time", file.getTime());
		json.set("description", file.getDescription());
		json.set("upload_uname", user_dao.selectById(file.getId()).getUsername());
		json.set("score", file.getScore());
		json.set("download", file.getDownload());
		json.set("upload_time", file.getUpload_time());
		
		HashMap<String, String> filesize = FileUtils.filesize(file.getSize());
		json.set("size", filesize.get("size")+filesize.get("unit"));
		if(file.getIs_dir() == 1) {
			Directory dir = new Directory(file.getFilename());
			json.set("contents", dir.getContents());
			json.set("name", file.getName()+".zip");
		} else {
			json.set("contents", "");
			json.set("name", file.getName());
		}
		return json;
	}
	
	class Directory {
		private LinkedList<Directory> dirs;
		private LinkedList<String> files;
		private String path;
		
		public Directory(String path) {
			this.dirs = new LinkedList<>();
			this.files = new LinkedList<>();
			
			path = path.replace("\\", "/");
			String[] list = path.split("/");
			this.path = list[list.length-1];
			// 递归构建目录结构
			java.io.File file = new java.io.File(ListMyUpload.this.real + path);
			java.io.File[] subFiles = file.listFiles();
			for(int i=0;i<subFiles.length;i++) {
				if(subFiles[i].isDirectory()) {
					this.dirs.add(new Directory(path + java.io.File.separator + subFiles[i].getName()));
				} else {
					this.files.add(subFiles[i].getName());
				}
			}
		}
		
		public JSONArray getContents() {
			JSONArray arr = new JSONArray(false);
			
			for(int i=0;i<this.dirs.size();i++) {
				JSONArray dirJson = this.dirs.get(i).getContents();
				for(int j=0;j<dirJson.size();j++) {
					arr.set(dirJson.getKeyByIndex(j), dirJson.getDataByIndex(j));
				}
			}
			
			for(int i=0;i<this.files.size();i++) {
				arr.set(this.files.get(i), 0);
			}
			
			JSONArray array = new JSONArray();
			array.set(this.path, arr);
			return array;
		}
	}
}
