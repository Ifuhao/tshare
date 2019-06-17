package dao;

import dao.vo.SysNoticeAccept;

public interface SysNoticeAcceptDAO {
	
	/**
	 * 向表中插入一行记录
	 * @param sna
	 * @return
	 */
	public boolean insert(SysNoticeAccept sna);
	
	/**
	 * 根据主键将该条系统通知针对接收者设置为删除状态
	 * @param accept_id
	 * @return
	 */
	public boolean delete(int accept_id);
	
	/**
	 * 查询接收者没有删除的所有系统通知
	 * @param address
	 * @return
	 */
	public SysNoticeAccept[] selectByAddress(String address);
	
	/**
	 * 是否有新的未读的系统消息
	 * @return
	 */
	public boolean hasNew(String address);
	
	/**
	 * 将某个用户指定的系统通知设置为已读
	 * @param sys_id
	 * @param user
	 * @return
	 */
	public boolean read(int sys_id, String user);
	
	/**
	 * 获取该表中记录的数量
	 * @return
	 */
	public int count();
}
