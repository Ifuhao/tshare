package dao;

import java.util.HashMap;

import dao.vo.Wish;

public interface WishDAO {
	/**
	 * 向心愿表中插入一条记录
	 * @param wish
	 * @return
	 */
	public boolean insert(Wish wish);
	
	/**
	 * 修改wish表中的一条数据
	 * @param wish
	 * @return
	 */
	public boolean update(Wish wish);
	
	/**
	 * 修改某项心愿为已得到
	 * @param wish_id
	 * @return
	 */
	public boolean getById(int wish_id);
	
	/**
	 * 修改某项心愿为已删除
	 * @param wish_id
	 * @return
	 */
	public boolean deleteById(int wish_id);
	
	/**
	 * 根据编号查询心愿
	 * @param wish_id
	 * @return
	 */
	public Wish selectById(int wish_id);
	
	/**
	 * 根据条件进行精准查询
	 * @param map
	 * @return
	 */
	public Wish[] selectByCond(HashMap<String, String> map);
	
	/**
	 * 有效心愿单的数量
	 * @return
	 */
	public int effectCount();
	
	/**
	 * 心愿单总数量
	 * @return
	 */
	public int count();
}
