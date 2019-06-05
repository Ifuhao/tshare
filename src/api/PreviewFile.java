package api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import utils.JSONParser;
import utils.file.FileUtils;
import utils.file.PDFUtils;
import utils.form.StringUtils;

@SuppressWarnings("serial")
public class PreviewFile extends HttpServlet {
	private String real = "";
	
	/**
	 * Constructor of the object.
	 */
	public PreviewFile() {
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
		
		/**
		 * url = year/subject/ext/filename
		 * 预览的目的是将待预览的文件以长图形式显示(长图的保存路径为：repository/temp/url/filename.png)
		 * 1. 判断文件是否可以预览(根据文件类型)
		 * 2. 将word文件先转为pdf(使用openoffice的jobconverter)
		 * 3. 取pdf的前10页预览(pdfbox实现pdf拆分)
		 * 4. 将得到的pdf变成一张一张图片(pdfbox实现pdf转png)
		 * 5. 将图片合成为一张长图(使用Java的awt/swing中的图像处理技术)
		 */
		
		String valid[] = {"doc", "docx", "ppt", "pptx", "pdf", "xls"};
		
		this.real = request.getSession().getServletContext().getRealPath("");
		String url = this.real + "upload_file" + File.separator + request.getParameter("url");		// 预览文件的url(year/subject/ext/文件名)
		String ext = FileUtils.getExtension(url);
		
		// 预览文件保存的目标文件夹
		String dest_dir =this.real + "repository" + File.separator + "temp" + File.separator + request.getParameter("url");
		
		JSONArray json = new JSONArray();
		
		File destFile = new File(dest_dir + ".png");
		if(destFile.exists()) {
			// 预览文件已经存在了，不需要再次生成
			System.out.println("预览图片已经生成");
			BufferedImage img = ImageIO.read(destFile);
			json.set("code", 1);
			json.set("height", img.getHeight());
		} else {
			if(!StringUtils.in_array_ignorecase(ext, valid)) {
				// 无法预览的文件类型
				json.set("code", 0);
				json.set("msg", "无法预览的文件类型");
			} else {
				// 先创建预览文件夹
				File dest_dir_file = new File(dest_dir);
				if(!dest_dir_file.exists()) {
					dest_dir_file.mkdirs();
				}
				
				// 先转为pdf
				try {
					PDFUtils.Word2Pdf(url, FileUtils.getPath(dest_dir) + "预览.pdf");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// pdf拆分，预览10页
				PDFUtils.splitPdf(FileUtils.getPath(dest_dir) + "预览.pdf", FileUtils.getPath(dest_dir) + "预览10.pdf", 10);
				FileUtils.delete(FileUtils.getPath(dest_dir) + "预览.pdf");
				
				// pdf转为png图片
				PDFUtils.Pdf2Png(FileUtils.getPath(dest_dir) + "预览10.pdf", FileUtils.getPath(dest_dir));
				FileUtils.delete(FileUtils.getPath(dest_dir) + "预览10.pdf");
				
				// png图片合成一张长图
				File path = new File(dest_dir);
				String pngs[] = path.list();
				for(int i=0;i<pngs.length;i++) {
					pngs[i] = FileUtils.getPath(dest_dir) + pngs[i];
				}
				String dest_png = dest_dir + ".png";
				int width = PDFUtils.merge_png(pngs, dest_png);
				FileUtils.delete(dest_dir);
				
				json.set("code", 1);
				json.set("width", width);
			}
		}
		
		PrintWriter out = response.getWriter();
		out.print(JSONParser.json_encode(json));
		System.out.println(JSONParser.json_encode(json));
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
