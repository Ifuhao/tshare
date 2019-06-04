package dao;

import dao.vo.Veri;

public interface VeriDAO {
	/**
	 * 增加一条验证码记录
	 * @param veri
	 */
	public boolean insert(Veri veri);
	
	/**
	 * 删除一条验证码记录
	 * @param veri
	 * @return
	 */
	public boolean deleteById(String id);
	
	/**
	 * 查询一条记录
	 * @param id
	 * @return
	 */
	public Veri selectById(String id);
}
