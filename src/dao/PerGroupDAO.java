package dao;

import dao.vo.PerGroup;

public interface PerGroupDAO {
	
	/**
	 * 向表中插入一条数据
	 * @param pg
	 * @return
	 */
	public boolean insert(PerGroup pg);
	
	/**
	 * 设置用户在组中的状态
	 * @param group_id
	 * @param user
	 * @param delete 为0时标记为退出组，为1时标记为加入组
	 * @return
	 */
	public boolean delete(int group_id, String user, int delete);
	
	/**
	 * 设置用户的消息接收状态
	 * @param group_id
	 * @param user
	 * @param new_msg 为0时标记为已读，为1时标记为未读 
	 * @return
	 */
	public boolean setNew(int group_id, String user, int new_msg);
	
	/**
	 * 根据用户账号获取所有未读新消息的用户组
	 * @return
	 */
	public PerGroup[] getNew(String address, int type);
	
	/**
	 * 查询用户是否有未读私信或交易消息
	 * @param user
	 * @param type
	 * @return
	 */
	public boolean hasNew(String user, int type);
	
	/**
	 * 查询用户在某个组中是否有未读私信或交易消息
	 * @param group_id
	 * @param user
	 * @param type
	 * @return
	 */
	public boolean hasNew(int group_id, String user, int type);
	
	/**
	 * 根据用户账号查询他们所在的组
	 * @param user1
	 * @param user2
	 * @return
	 */
	public PerGroup selectByUser(String user1, String user2, int type);
	
	/**
	 * 根据用户账号查询他所在的所有组，要求该用户没有退出该组
	 * @param user
	 * @return
	 */
	public PerGroup[] selectByUser(String user, int type);
	
	/**
	 * 判断两个用户是否已经建立了用户组
	 * @param user1 用户邮箱
	 * @param user2 用户邮箱
	 * @return
	 */
	public boolean exist(String user1, String user2, int type);
	
	public int count();
}
