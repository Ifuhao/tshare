package dao;

import java.util.HashMap;

import dao.vo.DlFile;

public interface DlFileDAO {
	
	/**
	 * 向dlFile表中插入一条数据
	 * @param dlFile
	 * @return
	 */
	public boolean insert(DlFile dlFile);
	
	/**
	 * 更新表中的一条数据
	 * @param dlFile
	 * @return
	 */
	public boolean update(DlFile dlFile);
	
	/**
	 * 根据did查询记录
	 * @param did
	 * @return
	 */
	public DlFile selectByDid(String did);
	
	/**
	 * 根据给定的条件查询多条记录
	 * @param map
	 * @return
	 */
	public DlFile[] selectByCond(HashMap<String, String> map);
	
	/**
	 * 获取数据库记录的数量
	 * @return
	 */
	public int count();
}
