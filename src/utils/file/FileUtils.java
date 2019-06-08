package utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;

import sun.misc.BASE64Decoder;

public class FileUtils {
	
	/**
	 * 删除指定文件或文件夹
	 * @param filename
	 */
	public static boolean delete(String filename) {
		filename = getPath(filename);
		
		File file = new File(filename);
		if(!file.exists()) {
			System.out.println("文件不存在");
		}
		
		if(file.isDirectory()) {
			// 递归删除文件夹
			File[] subFiles = file.listFiles();
			for(int i=0;i<subFiles.length;i++) {
				if(!delete(filename + subFiles[i].getName()))
					return false;
			}
		}
		if(!file.delete()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 将源文件复制到目标文件夹，并设置是否删除源文件，复制前后文件名保持不变
	 * @param srcFile 源文件路径
	 * @param destPath 目标文件夹
	 * @param isDelete 为true时删除源文件，false则不删除
	 */
	public static void copy(String srcFile, String destFile, boolean isDelete) {
		srcFile = getPath(srcFile);
		destFile = getPath(destFile);
		
		// 如果目标路径不存在则创建
		File dest = new File(destFile);
		if(!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		
		File src = new File(srcFile);
		
		if(src.isFile()) {
			// 单个文件复制
			try {
				Files.copy(src.toPath(), new File(destFile).toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 文件夹的复制
			new File(destFile).mkdir();
			File[] srcFiles = src.listFiles();
			for(int i=0;i<srcFiles.length;i++) {
				String nextSrcFile = srcFile + srcFiles[i].getName();
				String nextDestPath = destFile;
				copy(nextSrcFile, nextDestPath, isDelete);
			}
		}
		
		if(isDelete) {
			// 删除源文件
			delete(srcFile);
		}
	}
	
	/**
	 * 将源文件复制到目标路径，并且不删除源文件
	 * @param srcFile 源文件路径
	 * @param destPath 目标文件路径
	 */
	public static void copy(String srcFile, String destPath) {
		copy(srcFile, destPath, false);
	}
	
	/**
	 * 文件重命名
	 * @param src 源文件（完整的文件路径）
	 * @param dest 目标文件名（一个名字即可）
	 */
	public static void rename(String src, String dest) {
		System.out.println(src);
		System.out.println(dest);
		File srcFile = new File(src);
		if(!srcFile.exists()) {
			return;
		}
		
		HashMap<String, String> map = FileUtils.getPathAndFilename(src);
		File destFile = new File(map.get("path") + dest);
		srcFile.renameTo(destFile);
	}
	
	/**
	 * 将文件路径后面加上File.separator
	 * @param path
	 * @return
	 */
	public static String getPath(String path) {
		if(!path.endsWith(File.separator)) {
			path += File.separator;
		}
		
		return path;
	}
	
	/**
	 * 获取文件或文件夹的大小
	 * @param filename
	 * @return
	 */
	public static long getFileSize(String filename) {
		long total = 0;
		File file = new File(filename);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++) {
				String newFilename = filename + File.separator + files[i].getName();
				total += getFileSize(newFilename);
			}
		} else {
			total += file.length();
		}
		
		return total;
	}
	
	/**
	 * 文件单位转换
	 * @param size 传入以B为单位的文件大小，返回一个HashMap（单位合适，其中"size" -> 文件大小, "unit" -> 单位）
	 * @return
	 */
	public static HashMap<String, String> filesize(long filesize) {
		HashMap<String, String> map = new HashMap<>();
		String unit = "B";
		int time = 0;
		
		double size = filesize;
		while(size > 1000) {
			size /= 1024.0;
			time++;
		}
		
		if(time==1) {
			unit = "KB";
		} else if(time==2) {
			unit = "MB";
		} else if(time == 3) {
			unit = "GB";
		}
		
		map.put("size", String.format("%."+time+"f", size));
		map.put("unit", unit);
		return map;
	}
	
	/**
	 * 传入一个路径，分割其父目录和文件名
	 * 返回值由键值对表示"path" -> 父目录, "filename" -> 文件名
	 * @return
	 */
	public static HashMap<String, String> getPathAndFilename(String url) {
		HashMap<String, String> map = new HashMap<>();
		
		String[] explode = url.replace("\\", "/").split("/");
		if(explode.length == 1) {
			map.put("path", null);
			map.put("filename", explode[0]);
		} else {
			map.put("filename", explode[explode.length-1]);
			map.put("path", url.substring(0, url.length()-map.get("filename").length()));
		}
		return map;
	}
	
	/**
	 * 获取文件的后缀名
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		int index = filename.lastIndexOf(".");
		if(index == -1) {
			// 没有点，则没有后缀名
			return null;
		}
		
		String ext = filename.substring(index+1, filename.length());
		return ext;
	}
	
	/**
	 * 将base64数据解码成文件
	 * @param base64
	 * @param destFile
	 */
	public static void base64ToFile(String base64, String destFile) {
		BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
 
            OutputStream out = new FileOutputStream(destFile);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}
