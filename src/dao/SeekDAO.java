package dao;

import java.util.HashMap;

import dao.vo.Seek;

public interface SeekDAO {
	/**
	 * 向seek表插入一条数据
	 */
	public boolean insert(Seek seek);
	
	/**
	 * 修改seek表中的一条数据
	 * @param seek
	 * @return
	 */
	public boolean update(Seek seek);
	
	/**
	 * 删除seek表的一条记录
	 */
	public boolean deleteById(int seek_id);
	
	/**
	 * 修改seek表中的一条记录
	 */
	public boolean buyById(int seek_id);
	
	/**
	 * 根据主键查询seek表
	 */
	public Seek selectById(int seek_id);
	
	/**
	 * 根据指定字段进行模糊查询
	 */
	public Seek[] select(String field, String value);
	
	/**
	 * 根据条件进行查询
	 */
	public Seek[] selectByCond(HashMap<String, String> map);

	public int count();
	
	public int effectCount();
}
