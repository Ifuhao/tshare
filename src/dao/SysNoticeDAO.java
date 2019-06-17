package dao;

import dao.vo.SysNotice;

public interface SysNoticeDAO {
	
	/**
	 * 向表中插入一条记录
	 * @param sn
	 * @return
	 */
	public boolean insert(SysNotice sn);
	
	/**
	 * 根据消息编号将该条系统通知设置为删除
	 * @param sys_id
	 * @return
	 */
	public boolean delete(int sys_id);
	
	/**
	 * 根据消息编号获取该条系统通知的所有信息
	 * @param sys_id
	 * @return
	 */
	public SysNotice selectById(int sys_id);
	
	/**
	 * 计算表中记录的数量
	 * @return
	 */
	public int count();
}
