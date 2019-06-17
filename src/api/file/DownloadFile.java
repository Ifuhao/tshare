package api.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.Zip;
import utils.file.FileUtils;
import dao.DlFileDAO;
import dao.FileDAO;
import dao.impl.DlFileDAOImpl;
import dao.impl.FileDAOImpl;
import dao.vo.DlFile;
import dao.vo.User;

@SuppressWarnings("serial")
public class DownloadFile extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DownloadFile() {
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
		
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		if(user == null) {
			// 没有登录，无法下载
			System.out.println(user);
			response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
		} else {
			String real = "D:/tomcat/upload/file/";
			
			/**
			 * 下载文件，如果是单个文件则直接下载，如果是文件夹需要先打包成zip再下载
			 */
			String url = request.getParameter("url");
			String filename = request.getParameter("filename");
			
			FileDAO dao = new FileDAOImpl();
			dao.vo.File file = dao.selectByFilename(url);
			if(file == null) {
				// 因为数据库中有的filename后面带有"/"
				file = dao.selectByFilename(FileUtils.getPath(url));
			}
			
			if(file != null) {
				if(file.getIs_dir() == 1) {
					// 下载文件夹
					try {
						filename += ".zip";
						String dest = FileUtils.getPathAndFilename(url).get("path") + filename;
						Zip.zip(real + url, real + dest);
						url = dest;
						response.setContentType("application/zip");// 定义输出类型
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					response.setContentType("application/octet-stream");// 定义输出类型
				}
				
				response.setContentLength((int)FileUtils.getFileSize(real + url));
		        response.setHeader("Content-Disposition", "attachment;filename="+filename);
		        
		        FileInputStream fis = new FileInputStream(real + url);
		        OutputStream os = response.getOutputStream();
		        byte[] buf = new byte[1024];
		        int l = -1;
		        while((l = fis.read(buf))!=-1){
		            os.write(buf,0,l);
		            os.flush();
		        }
		        
		        os.close();
		        fis.close();
		        
		        if(file.getIs_dir() == 1) {
		        	FileUtils.delete(real + url);
		        }
		        
		        // 增加或修改下载记录
		        DlFileDAO dlDAO = new DlFileDAOImpl();
		        HashMap<String, String> map = new HashMap<>();
		        map.put("id", user.getId());
		        map.put("filename", request.getParameter("url"));
		        DlFile dlFile[] = dlDAO.selectByCond(map);
		        if(dlFile == null) {
		        	// 新的下载记录
		        	file.setDownload(file.getDownload()+1);
		        	dao.update(file);
		        	DlFile newDlFile = new DlFile();
		        	newDlFile.setDid(dlDAO.count()+1);
		        	newDlFile.setId(user.getId());
		        	newDlFile.setFilename(request.getParameter("url"));
		        	newDlFile.setIsmark(0);
		        	newDlFile.setScore(5);
		        	newDlFile.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		        	dlDAO.insert(newDlFile);
		        } else {
		        	DlFile newDlFile = dlFile[0];
		        	newDlFile.setIsmark(0);
		        	newDlFile.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		        	newDlFile.setScore(5);
		        	dlDAO.update(newDlFile);
		        }
			}
		}
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
