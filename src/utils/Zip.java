package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import utils.file.FileUtils;


/**
 * 文件的压缩与解压缩（zip压缩包）
 * @author fuhao
 */
public class Zip {
	
	/**
	 * 解压zip文件到指定目录，必须保证目标文件夹是存在的
	 * @param zipName zip文件路径
	 * @param filePath 解压的目标文件夹
	 */
	public static void unzip(String zipName, String savePath) {
		savePath = FileUtils.getPath(savePath);
		ZipFile zip = null;
		InputStream in = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		try {
			zip = new ZipFile(zipName, "gbk");
			Enumeration<?> entries = zip.getEntries();
			
			while(entries.hasMoreElements()) {
				// 获取压缩包中一个文件项
				ZipEntry entry = (ZipEntry) entries.nextElement();
				
				// 该文件项在压缩包中的文件路径
				String filename = entry.getName();
				File newFile = new File(savePath + filename);
				if(filename.endsWith("/")) {
					// 这个文件项在压缩包中是文件夹
					if(!newFile.exists())
						newFile.mkdirs();
				} else {
					// 这个文件项是单个文件，则先创建父目录，然后复制文件
					if(!newFile.getParentFile().exists()) {
						newFile.getParentFile().mkdirs();
					}
					
					in = zip.getInputStream(entry);
					fos = new FileOutputStream(newFile);
					bos = new BufferedOutputStream(fos);
					
					int count = 0;
					byte buf[] = new byte[1024];
					while((count = in.read(buf)) != -1) {
						bos.write(buf, 0, count);
					}
					
					bos.flush();
					bos.close();
					fos.close();
					in.close();
				}
			}
			zip.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解压zip文件到与zip同名的当前目录
	 * @param zipName
	 */
	public static void unzip(String zipName) {
		String savePath = zipName.substring(0, zipName.lastIndexOf("."));
		unzip(zipName, savePath);
	}
	
	/**
	 * 将一个文件夹或单个文件打包成指定名字的zip压缩包
	 * @param filename 待打包的文件或文件夹
	 * @param zipName 目标zip压缩包的路径（包括zip文件名）
	 * @throws Exception 
	 */
	public static void zip(String filename, String zipName) throws Exception {
		OutputStream out = new FileOutputStream(zipName);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        ZipOutputStream zos = new ZipOutputStream(bos);
        // 解决中文文件名乱码
        zos.setEncoding("gbk");
        File file = new File(filename);
        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }
        zipFile(file, basePath, zos);
        zos.closeEntry();
        zos.close();
        bos.close();
        out.close();
	}
	
	/**
	 * 将一个文件夹或单个文件打包到与文件名相同的zip压缩包中
	 * @param filename
	 * @throws Exception 
	 */
	public static void zip(String filename) throws Exception {
		File file = new File(filename);
		String zipName = "";
		
		if(file.isDirectory()) {
			zipName = filename + ".zip";
		} else {
			String ext = FileUtils.getExtension(filename);
			if(ext == null) {
				// 没有后缀名的文件
				zipName = filename + ".zip";
			} else {
				zipName = filename.substring(0, filename.lastIndexOf(".")) + ".zip";
			}
		}
		
		zip(filename, zipName);
	}
    
    /**
     * 递归压缩文件
     * @param parentFile
     * @param basePath
     * @param zos
     * @throws Exception
     */
    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[1024];
        for (File file : files) {
            if (file.isDirectory()) {
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, cache.length)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
    }
}
