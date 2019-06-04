package api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.WordAnalysis;
import utils.file.FileUtils;
import dao.FileDAO;
import dao.impl.FileDAOImpl;
import dao.impl.UserDAOImpl;
import dao.vo.File;

@SuppressWarnings("serial")
public class SearchFile extends HttpServlet {
	// 搜索到的文件保存在一个键值对数据结构中(键=filename+key | 值=file)
	private LinkedList<String> keyList = new LinkedList<>();
	private LinkedList<File> fileList = new LinkedList<>();
	private FileDAO dao = new FileDAOImpl();
	private String real = "";
	/**
	 * Constructor of the object.
	 */
	public SearchFile() {
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
		
		this.keyList.clear();
		this.fileList.clear();
		/**
		 * 根据资料名、文件名、科目名称、文件描述进行搜索
		 * 搜索的结果包括单个文件，也包括文件夹。此后我将文件夹也视作一个文件对象，所以后面我所提到的所有文件都包含文件夹的含义
		 *
		 * 搜索过程分为两个步骤：
		 * 	|- 完全匹配搜索: 直接在上述四类名称中根据关键字进行完全匹配
		 *	|- 分词匹配搜索: 对关键字进行分词，对分词的每一部分进行匹配
		 *
		 * 匹配过程中将文件与关键字建立起键值对关系
		 *  |- key=关键字
		 *  |- value=文件对象
		 * 由于一个关键字可以搜索出多个文件，所以用HashMap类型无法描述
		 * 可以在存储时进行如下设计
		 *	|- 将关键字和文件分开用LinkedList保存，只需要保证其索引相同即可，这样即可解决关键字重复问题
		 *
		 * 如果一个文件对应于多个关键字，则进行相应的合并删除操作
		 *	|- 如果关键字a包含关键字b，则删除关键字b对应的项
		 *	|- 如果关键字a和关键字b没有包含关系，则将两者合并成新的一项，并且删除这两项
		 *
		 * 对搜索到的文件进行排序，排序根据需求而定（相关度、下载量、评分等）
		 *	|- 对于链表的数据结构，可以采用双重循环的冒泡排序
		 * 
		 * 进行分页处理，每10项一页，最后不足10项算一页
		 *	|- 使用二维数组进行存储
		 */
		
		// 搜索关键字
		String key = request.getParameter("key");
		// 1为文件夹搜索方式、0为单个文件搜索方式
		int mode = Integer.parseInt(request.getParameter("mode"));
		// 排序方式：0（相关度）、1（下载量）、2（上传时间）、3（评分）
		int sort = Integer.parseInt(request.getParameter("sort"));
		// 显示第几页
		int page = Integer.parseInt(request.getParameter("page"));
		
		this.real = request.getSession().getServletContext().getRealPath("") + "upload_file" + java.io.File.separator;
		
		// 1. 完全匹配搜索
		boolean flag = mode==0 ? false : true;
		this.search(key, flag);
		
		// 2. 分词匹配搜索
		String keys[] = WordAnalysis.split(key);
		for(int i=0;i<keys.length;i++) {
			this.search(keys[i], flag);
		}
		
		// 3. 进行排序
		if(sort == 0) {
			// 根据相关度（关键字长度）进行排序
			for(int i=0;i<this.keyList.size();i++) {
				String keyword1 = this.keyList.get(i);
				
				for(int j=i;j<this.keyList.size();j++) {
					String keyword2 = this.keyList.get(j);
					if(keyword1.length() < keyword2.length()) {
						this.change(i, j);
						// 交换关键字
						keyword1 = keyword2;
					}
				}
			}
		} else if(sort == 1) {
			// 根据下载量进行排序（download字段）
			for(int i=0;i<this.keyList.size();i++) {
				int download1 = this.fileList.get(i).getDownload();
				
				for(int j=i;j<this.keyList.size();j++) {
					int download2 = this.fileList.get(j).getDownload();
					if(download1 < download2) {
						this.change(i, j);
						download1 = download2;
					}
				}
			}
		} else if(sort == 2) {
			// 根据上传时间进行排序（upload_time字段），最近上传的放在前面
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				for(int i=0;i<this.keyList.size();i++) {
					String time1 = this.fileList.get(i).getUpload_time();
					Date date1 = sdf.parse(time1);
					for(int j=i;j<this.keyList.size();j++) {
						String time2 = this.fileList.get(j).getUpload_time();
						Date date2 = sdf.parse(time2);
						
						if(date1.before(date2)) {
							this.change(i, j);
							date1 = date2;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if(sort == 3) {
			// 根据评分进行排序（score字段），评分高的放在前面
			for(int i=0;i<this.keyList.size();i++) {
				float score1 = this.fileList.get(i).getScore();
				
				for(int j=i;j<this.keyList.size();j++) {
					float score2 = this.fileList.get(j).getScore();
					if(score1 < score2) {
						this.change(i, j);
						score1 = score2;
					}
				}
			}
		}
		
		// 4. 进行分页
		LinkedList<File[]> list = new LinkedList<>();
		File[] files = new File[10];
		for(int i=0;i<this.keyList.size();i++) {
			if(i != 0 && i%10 == 0) {
				list.add(files);
				files = new File[10];
			}
			files[i%10] = this.fileList.get(i);
			if(i==this.keyList.size()-1) {
				list.add(files);
			}
		}
		
		JSONArray array = new JSONArray();
		if(this.keyList.size()==0) {
			// 没有搜索到任何结果
			array.set("code", 0);
			array.set("msg", "没有找到您需要的文件");
		} else {
			/**
			 * 显示数据：
			 * 1.  name：文件名
			 * 2.  category：类别（课内/课外）
			 * 3.  subject：科目
			 * 4.  type：文件分类（课件/试卷）
			 * 5.  time：争对年份
			 * 6.  description：描述
			 * 7.  upload_time：上传时间
			 * 8.  upload_uid：上传人学号
			 * 9.  upload_uname：上传人姓名
			 * 10. score：评分
			 * 11. download：下载量
			 * 12. url：下载链接地址
			 * 13. size：文件大小
			 * 14. contents：内部结构
			 */ 
			if(page <= list.size()) {
				// 显示第page-1页
				files = list.get(page-1);
				LinkedList<JSONArray> jsonFiles = new LinkedList<>();
				
				for(int i=0;i<files.length;i++) {
					if(files[i] == null) {
						break;
					}
					JSONArray jsonFile = this.getJsonFile(files[i], flag);
					jsonFiles.add(jsonFile);
				}
				
				JSONArray dataArray = JSONArray.arrayToJSONArray(jsonFiles);
				
				array.set("code", 1);
				array.set("data", dataArray);
				array.set("amount", this.keyList.size());
			}
		}
		
		PrintWriter out = response.getWriter();
		out.print(JSONParser.json_encode(array));
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
	
	private void search(String keyword, boolean is_dir) {
		// 1. 搜索filname
		String field[] = new String[]{"filename", "name", "description", "subject"};
		for(int i=0;i<field.length;i++) {
			File files[] = this.dao.select(field[i], keyword, is_dir);
			this.store(files, keyword, is_dir);
		}
	}
	
	private void store(File files[], String keyword, boolean is_dir) {
		if(files == null) return;
		for(int i=0;i<files.length;i++) {
			if(is_dir) {
				// 只保存顶层文件夹(year/subject/zip/xxx)
				String[] explode = files[i].getFilename().split("/");
				if(explode.length != 4) {
					continue;
				}
			}
			
			int index = fileList.indexOf(files[i]);
			if(index == -1) {
				this.keyList.add(keyword);
				this.fileList.add(files[i]);
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
	
	private void change(int i, int j) {
		// 交换关键字索引
		String strTemp = this.keyList.get(i);
		this.keyList.set(i, this.keyList.get(j));
		this.keyList.set(j, strTemp);
		
		// 交换文件对象
		File fileTemp = this.fileList.get(i);
		this.fileList.set(i, this.fileList.get(j));
		this.fileList.set(j, fileTemp);
	}
	
	private JSONArray getJsonFile(File file, boolean mode) {
		JSONArray jsonFile = new JSONArray();
		HashMap<String, String> filesize = FileUtils.filesize(file.getSize());
		
		String filename = file.getFilename();
		jsonFile.set("name", file.getName());
		jsonFile.set("category", file.getCategory()+"");
		jsonFile.set("subject", file.getSubject());
		jsonFile.set("type", file.getType());
		jsonFile.set("time", file.getTime());
		jsonFile.set("description", file.getDescription());
		jsonFile.set("upload_time", file.getUpload_time());
		jsonFile.set("upload_uid", file.getId());
		jsonFile.set("upload_uname", new UserDAOImpl().selectById(file.getId()).getUsername());
		jsonFile.set("score", file.getScore()+"");
		jsonFile.set("download", file.getDownload()+"");
		jsonFile.set("url", filename);
		jsonFile.set("size", filesize.get("size") + filesize.get("unit"));
		if(mode) {
			Directory dir = new Directory(file.getFilename());
			jsonFile.set("contents", dir.getContents());
		} else {
			jsonFile.set("contents", "");
		}
		return jsonFile;
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
			java.io.File file = new java.io.File(SearchFile.this.real + path);
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
				arr.set(this.files.get(i));
			}
			
			JSONArray array = new JSONArray();
			array.set(this.path, arr);
			return array;
		}
	}
}
