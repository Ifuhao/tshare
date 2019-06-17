package dao;

import dao.vo.PerNotice;

public interface PerNoticeDAO {
	
	/**
	 * 向表中插入一条记录
	 * @param pn
	 * @return
	 */
	public boolean insert(PerNotice pn);
	
	/**
	 * 根据消息编号将该条私信设置为接收方已读
	 * @param msg_id
	 * @return
	 */
	public boolean read(int msg_id);
	
	/**
	 * 根据组号，查找该组所有的消息，并按照时间由早到晚的顺序返回
	 * @param group_id
	 * @return
	 */
	public PerNotice[] selectByGroupId(int group_id);
	
	/**
	 * 获取某个用户组中最新的消息
	 * @param group_id
	 * @param address
	 * @return
	 */
	public PerNotice getLastestMsg(int group_id);
	
	/**
	 * 获取指定用户组中最新的n条消息
	 * @param group_id
	 * @param start
	 * @param count
	 * @return
	 */
	public PerNotice[] getNotices(int group_id, int start, int count);
	
	/**
	 * 获取记录的数量
	 * @return
	 */
	public int count();
}
