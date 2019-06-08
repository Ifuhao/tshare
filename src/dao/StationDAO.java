package dao;

import dao.vo.Station;

public interface StationDAO {
	
	/**
	 * 增加一次上传记录
	 * @return
	 */
	public boolean addUpload_time();
	
	/**
	 * 增加指定次数的上传记录
	 * @param times
	 * @return
	 */
	public boolean addUpload_time(int times);
	
	/**
	 * 增加一次下载记录
	 * @return
	 */
	public boolean addDownload_time();
	
	/**
	 * 增加指定次数的下载记录
	 * @param times
	 * @return
	 */
	public boolean addDownload_time(int times);
	
	/**
	 * 增加一份文件记录
	 * @return
	 */
	public boolean addFile_number();
	
	/**
	 * 增加指定份数的文件记录
	 * @param numbers
	 * @return
	 */
	public boolean addFile_number(int numbers);
	
	/**
	 * 查询station表
	 * @return
	 */
	public Station select();
}
