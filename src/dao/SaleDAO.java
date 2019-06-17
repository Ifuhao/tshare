package dao;

import java.util.HashMap;

import dao.vo.Sale;

public interface SaleDAO {
	/**
	 * 向sale表中插入一条数据
	 * @param sale
	 * @return
	 */
	public boolean insert(Sale sale);
	
	/**
	 * 修改sale表中的一条数据
	 * @param sale
	 * @return
	 */
	public boolean update(Sale sale);
	
	/**
	 * 删除sale表的一条记录
	 * @param sale_id
	 * @return
	 */
	public boolean deleteById(int sale_id);
	
	/**
	 * 修改sale表中一条记录
	 * @param sale_id
	 * @return
	 */
	public boolean sellById(int sale_id);
	
	/**
	 * 根据主键查询sale表
	 * @param sale_id
	 * @return
	 */
	public Sale selectById(int sale_id);
	
	/**
	 * 根据指定字段进行模糊查询
	 * @param field
	 * @param value
	 * @return
	 */
	public Sale[] select(String field, String value);
	
	/**
	 * 根据条件进行查询
	 * @param map
	 * @return
	 */
	public Sale[] selectByCond(HashMap<String, String> map);
	
	/**
	 * 根据条件和某个字段进行模糊查询
	 * @param field 模糊查询字段
	 * @param value 模糊查询关键字
	 * @return
	 */
	public Sale[] select(String field, String value, String type, boolean sale_new, boolean delivery, boolean bargain);
	
	/**
	 * 获取数据数量
	 * @return
	 */
	public int count();
	
	public int effectCount();
}
