package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import utils.Database;
import dao.PerNoticeDAO;
import dao.vo.PerNotice;

public class PerNoticeDAOImpl implements PerNoticeDAO {

	@Override
	public boolean insert(PerNotice pn) {
		PreparedStatement stmt = Database.getStmt("insert", pn);
		try {
			return stmt.execute();
		} catch (Exception e) {
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
	public boolean read(int msg_id) {
		String sql = "update perNotice set is_read=1 where msg_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, msg_id);
			return stmt.execute();
		} catch (Exception e) {
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
	public PerNotice[] selectByGroupId(int group_id) {
		String sql = "select * from perNotice where group_id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<PerNotice> list = new LinkedList<>();
		
		try {
			stmt.setInt(1, group_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				PerNotice pn = new PerNotice();
				Database.getObject(rs, pn);
				list.add(pn);
			}
		} catch (Exception e) {
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
			PerNotice pns[] = new PerNotice[list.size()];
			for(int i=0;i<pns.length;i++) {
				pns[i] = list.get(i);
			}
			return pns;
		}
	}

	@Override
	public PerNotice getLastestMsg(int group_id) {
		String sql = "select * from perNotice where group_id=? and time="
				+ "(select max(time) from perNotice where group_id=?)";
		PreparedStatement stmt = Database.getPstmt(sql);
		PerNotice pn = null;
		
		try{
			stmt.setInt(1, group_id);
			stmt.setInt(2, group_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				pn = new PerNotice();
				Database.getObject(rs, pn);
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
		return pn;
	}
	
	@Override
	public PerNotice[] getNotices(int group_id, int start, int count) {
		String sql = "select * from perNotice where group_id=? order by time desc limit ?, ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<PerNotice> list = new LinkedList<>();
		
		try {
			stmt.setInt(1, group_id);
			stmt.setInt(2, start);
			stmt.setInt(3, count);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				PerNotice pn = new PerNotice();
				Database.getObject(rs, pn);
				list.add(pn);
			}
		} catch (Exception e) {
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
			PerNotice pns[] = new PerNotice[list.size()];
			for(int i=0;i<pns.length;i++) {
				pns[i] = list.get(i);
			}
			return pns;
		}
	}
	
	@Override
	public int count() {
		String sql = "select count(msg_id) num from perNotice";
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
