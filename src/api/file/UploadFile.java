package api.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import json.JSONArray;
import utils.JSONParser;
import utils.Zip;
import utils.file.FileUtils;
import utils.form.FormOperator;
import dao.FileDAO;
import dao.impl.FileDAOImpl;
import dao.vo.File;
import dao.vo.User;

/**
 * 上传文件Servlet
 * @author fuhao
 */
@MultipartConfig
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String real = "";
	/**
	 * Constructor of the object.
	 */
	public UploadFile() {
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
		
		/**
		 * 上传文件如果是压缩文件需要解压缩
		 * 需要计算filename：用户+文件的hashcode
		 * 文件另存为upload_file/year/subject/filename
		 * 参数：
		 * 	1. 文件(Part对象)
		 *  2. name：文件名
		 *  3. type：文件类型（课件、试卷）
		 *  4. time：文件针对年份（2019春）
		 *  5. category：文件分类（课内/课外）
		 *  6. teacher：老师
		 *  7. description：文件描述
		 */
		
		this.real = "D:/tomcat/upload/file/";		// 上传的文件不能保存在
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");		// 从session中获取上传者
		
		JSONArray array = new JSONArray();
		if(user == null) {
			// 没有登录
			array.set("code", 0);
			array.set("msg", "登录才能上传文件");
		} else {
			File file = new File();
			new FormOperator<File>(file, request);		// 接收除了File标签以外的其他表单元素并写入file对象
			Date date = new Date();
			file.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			
			// 计算临时文件的路径
			String path = "";
			// path是/upload_file/year/subject/ 只差最后一级目录ext
			path += (new SimpleDateFormat("yyyy").format(date) + java.io.File.separator + file.getSubject() + java.io.File.separator);
			
			// 先将文件保存，然后再重命名
			Part part = request.getPart("file");		// 接收File标签
			String fName = part.getSubmittedFileName();
			InputStream in = part.getInputStream();
			
			// 设置文件大小
			file.setSize(part.getSize());
			
			java.io.File newFile = new java.io.File(real + path + fName);
			if(!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			FileOutputStream out = new FileOutputStream(newFile);
			
			byte bytes[] = new byte[1024];
			int count = 0;
			while((count = in.read(bytes)) != -1) {
				out.write(bytes, 0, count);
			}
			out.flush();
			out.close();
			in.close();
			
			file.setId(user.getId());
			
			// 重命名文件newFile
			// 计算filename(使用用户+文件的hashcode 或   使用UUID获取128位全国唯一标识)
	//		String uuid = UUID.randomUUID().toString();
			String uuid = user.hashCode()*31 + file.hashCode()+"";
			uuid = uuid.replace("-", "");
			String filename = real + path + uuid;
	
			// 获取文件的后缀名并生成最后一级目录
			String ext = FileUtils.getExtension(real + path + fName);
			
			if(ext == null) {
				path += "other";
			} else {
				path += ext;
				filename += ("." + ext);
				if(!ext.equals("zip")) {
					file.setName(file.getName() + "." + ext);
				}
			}
			java.io.File objFile = new java.io.File(filename);
			newFile.renameTo(objFile);
			
			// 将文件复制到这一路径下并删除源文件
			if("zip".equals(ext)) {
				// 上传了一个压缩包，进行解压缩
				Zip.unzip(filename, real + path + java.io.File.separator + uuid);
				
				// 解压缩的文件是文件夹
				file.setIs_dir(1);
			} else {
				file.setIs_dir(0);
				FileUtils.copy(filename, real + path + java.io.File.separator + FileUtils.getPathAndFilename(filename).get("filename"));
			}
			FileUtils.delete(filename);
			// 文件重命名
			String newFilename = path + java.io.File.separator + uuid;
			if(ext != null && !ext.equals("zip")) {
				newFilename += "."+ext;
			}
			file.setFilename(newFilename.replace("\\", "/"));
			
			// 写入数据库
			FileDAO dao = new FileDAOImpl();
			dao.insert(file);
			if(file.getIs_dir() == 1) {
				// 文件夹需要将其内部的所有文件的数据都加入数据库
				this.store(file, dao);
			}
			
			// 返回数据给前端
			array.set("code", 1);
			array.set("msg", "上传成功");
		}
		
		PrintWriter writer = response.getWriter();
		writer.print(JSONParser.json_encode(array));
		writer.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	private void store(File file, FileDAO dao) {
		String filename = file.getFilename();
		java.io.File parent = new java.io.File(this.real + filename);
		java.io.File[] subFiles = parent.listFiles();
		for(int i=0;i<subFiles.length;i++) {
			File newFile = file.clone();
			String newFilename = filename + java.io.File.separator + subFiles[i].getName();
			newFile.setFilename(newFilename);
			newFile.setName(subFiles[i].getName());
			newFile.setSize(FileUtils.getFileSize(this.real + newFilename));
			
			java.io.File newSub = new java.io.File(this.real + newFilename);
			if(newSub.isDirectory()) {
				newFile.setIs_dir(1);
				store(newFile, dao);
			} else {
				newFile.setIs_dir(0);
			}
			newFile.setFilename(newFilename.replace("\\", "/"));
			dao.insert(newFile);
		}
	}
}
