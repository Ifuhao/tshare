package utils.file;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * pdf工具类
 * @author fuhao
 */
public class PDFUtils {
	public static ResourceBundle rb = ResourceBundle.getBundle("dym");
	public static String dymPath = rb.getString("dym");
	/**
	 * 将word转为pdf
	 * @param src word文件路径
	 * @param dest pdf保存路径
	 * @throws Exception 
	 */
	public static void Word2Pdf(String src, String dest) throws Exception {
		// PDF.jar存放的路径
		String jarPath = dymPath + "PDF.jar";
		File file = new File(jarPath);
		URL url = file.toURI().toURL();
		URLClassLoader loader = new URLClassLoader(new URL[]{url});
		Class<?> PDFConverter = loader.loadClass("fuhao.PDFConverter");
		Method word2pdf = PDFConverter.getDeclaredMethod("Word2Pdf", String.class, String.class);
		word2pdf.invoke(null, src, dest);
		loader.close();
	}
	
	/**
	 * 拆分pdf(取pdf文件的前page页为一个新的pdf)
	 * @param pdf 原pdf文件
	 * @param page 拆分页数
	 */
	public static void splitPdf(String srcPdf, String destPdf, int page) {
		File src = new File(srcPdf);
		if(!src.exists()) {
			System.out.println("原文件不存在");
			return;
		}
		
		// 先创建目标文件夹
		File dest = new File(destPdf);
		if(!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		
		try {
			PDDocument doc = PDDocument.load(src, MemoryUsageSetting.setupTempFileOnly());
			int allPages = doc.getPages().getCount();
			if(page >= allPages) {
				// 如果需要拆分的页数比pdf总页数还多，则按pdf总页数拆分，也就是不拆分，直接复制即可
				FileUtils.copy(srcPdf, destPdf);
			} else {
				PDDocument newPdf = new PDDocument();
				PDPageTree pages = doc.getPages();
				for(int i=0;i<page;i++) {
					newPdf.addPage(pages.get(i));
				}
				newPdf.save(dest);
				newPdf.close();
			}
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将pdf转为png图片
	 * @param src 原pdf路径
	 * @param dest 图片的父目录
	 */
	public static void Pdf2Png(String src, String destPath) {
		File srcFile = new File(src);
		if(!srcFile.exists()) {
			System.out.println("源文件不存在");
			return;
		}
		
		File destFile = new File(destPath);
		if(!destFile.exists()) {
			destFile.mkdirs();
		}
		
		try {
			PDDocument doc = PDDocument.load(srcFile);
			PDFRenderer renderer = new PDFRenderer(doc);
			int pageCount = doc.getNumberOfPages();
			for(int i=0;i<pageCount;i++){
				BufferedImage image = renderer.renderImageWithDPI(i, 300);
				File file = new File(destPath + File.separator + i + ".png");
				ImageIO.write(image, "PNG", file);
			}
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将多张png图片按顺寻连接，由上往下进行合并
	 * @param pngs 所有的png图片的路径
	 * @param dest_png 目标png图片的路径
	 * @return 返回图片的宽度
	 */
	public static int merge_png(String[] pngs, String dest_png) {
		// 将全部图片读到缓冲区，并计算合并后图片的宽高
		int width = 0;
		int height = 0;
		
		BufferedImage[] images = new BufferedImage[pngs.length];
		for(int i=0;i<pngs.length;i++) {
			try {
				images[i] = ImageIO.read(new File(pngs[i]));
				if(images[i].getWidth() > width) {
					width = images[i].getWidth();
				}
				
				height += images[i].getHeight();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 生成一张新的缓冲图片
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = newImg.getGraphics();
		
		// 将所有png图片写入新的缓冲图片并保存
		int y=0;
		for(int i=0;i<pngs.length;i++) {
			g.drawImage(images[i], 0, y, width, y+images[i].getHeight(), 0, 0, images[i].getWidth(), images[i].getHeight(), null);
			y += images[i].getHeight();
		}
		
		try {
			ImageIO.write(newImg, "PNG", new File(dest_png));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return width;
	}
}
