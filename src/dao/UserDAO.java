package dao;

import java.util.HashMap;

import dao.vo.User;

public interface UserDAO {
	
	/**
	 * 向user表增加一条数据
	 * @param user
	 * @return
	 */
	public boolean insert(User user);
	
	/**
	 * 根据用户编号修改用户数据
	 * @param user
	 * @return
	 */
	public boolean update(User user);
	
	/**
	 * 根据编号查询用户完整信息
	 * @param id
	 * @return
	 */
	public User selectById(String id);
	
	/**
	 * 查询所有用户信息
	 * @return
	 */
	public User[] selectAll();
	
	/**
	 * 根据条件查询用户
	 * @param map
	 * @return
	 */
	public User[] selectByCondtion(HashMap<String, String> map);
	
	/**
	 * 获取用户数量
	 * @return
	 */
	public Integer count();
}
