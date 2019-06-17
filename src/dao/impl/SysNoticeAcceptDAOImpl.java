package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import utils.Database;
import dao.SysNoticeAcceptDAO;
import dao.vo.SysNoticeAccept;

public class SysNoticeAcceptDAOImpl implements SysNoticeAcceptDAO {

	@Override
	public boolean insert(SysNoticeAccept sna) {
		PreparedStatement stmt = Database.getStmt("insert", sna);
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
	public boolean delete(int accept_id) {
		String sql = "update sysNoticeAccept set is_delete=1 where accept_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try{
			stmt.setInt(1, accept_id);
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
	public SysNoticeAccept[] selectByAddress(String address) {
		String sql = "select * from sysNoticeAccept where address = ? and is_delete=0";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<SysNoticeAccept> list = new LinkedList<>();
		
		try{
			stmt.setString(1, address);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				SysNoticeAccept sna = new SysNoticeAccept();
				Database.getObject(rs, sna);
				list.add(sna);
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
		
		if(list.size() == 0) {
			return null;
		} else {
			SysNoticeAccept snas[] = new SysNoticeAccept[list.size()];
			for(int i=0;i<snas.length;i++) {
				snas[i] = list.get(i);
			}
			return snas;
		}
	}

	@Override
	public int count() {
		String sql = "select count(accept_id) num from sysNoticeAccept";
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

	@Override
	public boolean hasNew(String address) {
		String sql = "select * from sysNoticeAccept where address=? and is_read=0 and is_delete=0";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try{
			stmt.setString(1, address);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
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
	public boolean read(int sys_id, String address) {
		String sql = "update sysNoticeAccept set is_read = 1 where sys_id = ? and address = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try{
			stmt.setInt(1, sys_id);
			stmt.setString(2, address);
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
}
