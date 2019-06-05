package api.market;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.SaleDAO;
import dao.impl.SaleDAOImpl;
import dao.vo.Sale;

@MultipartConfig
@SuppressWarnings("serial")
public class PublishSale extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PublishSale() {
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
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		
//		Part part = request.getPart("picture");
//		
//		JSONArray json = new JSONArray();
//		json.set("code", 1);
//		json.set("msg", "上架成功");
//		PrintWriter out = response.getWriter();
//		out.write(JSONParser.json_encode(json));
//		out.close();
		
		
		// 图片的保存路径/tshare/market/sale_id/xxx.image
		SaleDAO sale_dao = new SaleDAOImpl();
        Sale sale = new Sale();
        sale.setSale_id(sale_dao.count()+1);
        
		request.setCharacterEncoding("utf-8");//防止中文名乱码 
        int sizeThreshold=1024*6; //缓存区大小 
        String basePath = this.getServletContext().getRealPath("")
        		+ File.separator + "market" + File.separator + sale.getSale_id()
        		+ File.separator;
        
        File repository = new File(basePath); //缓存区目录
        long sizeMax = 1024 * 1024 * 10;//设置文件的大小为10M
        
        final String allowExtNames = "jpg,gif,bmp,png";
        
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(repository);
        diskFileItemFactory.setSizeThreshold(sizeThreshold);
        ServletFileUpload servletFileUpload=new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setSizeMax(sizeMax);
        
        List<FileItem> fileItems = null;
        try {
            fileItems = servletFileUpload.parseRequest(request);
            
            for(FileItem fileItem:fileItems) {
                long size=0;
                String filePath = fileItem.getName();
                System.out.println(filePath);
                if(filePath==null || filePath.trim().length()==0)  
                    continue;  
                String fileName=filePath.substring(filePath.lastIndexOf(File.separator)+1);   
                String extName=filePath.substring(filePath.lastIndexOf(".")+1);   
                if(allowExtNames.indexOf(extName)!=-1) {  
                    try {  
                        fileItem.write(new File(basePath+fileName));  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                } else {  
                    throw new FileUploadException("file type is not allowed");  
                }
            }
	    } catch (FileSizeLimitExceededException e){  
	        System.out.println("file size is not allowed");  
	    } catch (FileUploadException e1){  
	        e1.printStackTrace();  
	    }  
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println("this is Post method");
		out.flush();
		out.close();
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
