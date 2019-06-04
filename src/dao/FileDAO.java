package dao;

import java.util.HashMap;

import dao.vo.File;

public interface FileDAO {
	/**
	 * 向数据库插入一条记录
	 * @param file
	 * @return
	 */
	public boolean insert(File file);
	
	/**
	 * 更新一条记录
	 * @param file
	 * @return
	 */
	public boolean update(File file);
	
	/**
	 * 根据文件名搜索
	 * @param filename
	 * @return
	 */
	public File selectByFilename(String filename);
	
	/**
	 * 搜索所有的文件
	 * @return
	 */
	public File[] selectAll();
	
	/**
	 * 根据指定条件搜索
	 * @param map
	 * @return
	 */
	public File[] selectByCond(HashMap<String, String> map);
	
	/**
	 * 根据field字段进行模糊搜索
	 * @param field 字段名
	 * @param value 搜索关键字
	 * @param is_dir 是否是搜索文件夹
	 * @return
	 */
	public File[] select(String field, String value, boolean is_dir);
}
