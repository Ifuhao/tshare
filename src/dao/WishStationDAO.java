package dao;

import dao.vo.WishStation;

public interface WishStationDAO {
	
	/**
	 * 给表的user字段增加num
	 * @param num
	 * @return
	 */
	public boolean setUser(int num);
	
	/**
	 * 给表的user字段增加1
	 * @return
	 */
	public boolean addUser();
	
	/**
	 * 给表的achieve字段增加num
	 * @param num
	 * @return
	 */
	public boolean setAchieve(int num);
	
	/**
	 * 给表的achieve字段增加1
	 * @return
	 */
	public boolean addAchieve();
	
	/**
	 * 给表的wishes字段增加num
	 * @param num
	 * @return
	 */
	public boolean setWishes(int num);
	
	/**
	 * 给表的wishes字段增加1
	 * @return
	 */
	public boolean addWishes();
	
	/**
	 * 获取表中的记录
	 * @return
	 */
	public WishStation select();
}
