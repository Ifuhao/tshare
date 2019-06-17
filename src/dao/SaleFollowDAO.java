package dao;

import dao.vo.SaleFollow;

public interface SaleFollowDAO {
	/**
	 * 向表中插入一条记录
	 * @param obj
	 * @return
	 */
	public boolean insert(SaleFollow obj);
	
	/**
	 * 根据主键删除表中的一条记录
	 * @param follow_id
	 * @return
	 */
	public boolean deleteById(int follow_id);
	
	/**
	 * 根据主键查询一条记录
	 * @param follow_id
	 * @return
	 */
	public SaleFollow selectByFollowId(int follow_id);
	
	/**
	 * 根据商品标号查询多条记录
	 * @param sale_id
	 * @return
	 */
	public SaleFollow[] selectBySaleId(int sale_id);
	
	/**
	 * 根据用户账号查询多条记录
	 * @param id
	 * @return
	 */
	public SaleFollow[] selectById(String id);
	
	public int count();
}
