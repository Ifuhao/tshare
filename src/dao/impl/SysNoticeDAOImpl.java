package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Database;
import dao.SysNoticeDAO;
import dao.vo.SysNotice;

public class SysNoticeDAOImpl implements SysNoticeDAO {

	@Override
	public boolean insert(SysNotice sn) {
		PreparedStatement stmt = Database.getStmt("insert", sn);
		try{
			return stmt.execute();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean delete(int sys_id) {
		String sql = "update sysNotice set is_delete=1 where sys_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try{
			stmt.setInt(1, sys_id);
			return stmt.execute();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public SysNotice selectById(int sys_id) {
		String sql = "select * from sysNotice where sys_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		SysNotice sn = null;
		
		try{
			stmt.setInt(1, sys_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				sn = new SysNotice();
				Database.getObject(rs, sn);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sn;
	}

	@Override
	public int count() {
		String sql = "select count(sys_id) num from sysNotice";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try{
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("num");
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
