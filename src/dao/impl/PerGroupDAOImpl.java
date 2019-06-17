package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import utils.Database;
import dao.PerGroupDAO;
import dao.vo.PerGroup;

public class PerGroupDAOImpl implements PerGroupDAO {

	@Override
	public boolean insert(PerGroup pg) {
		PreparedStatement stmt = Database.getStmt("insert", pg);
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
	public boolean delete(int group_id, String user, int delete) {
		String sql = "update perGroup set delete1=if(group_id=? and user1=?, ?, delete1), "
				+ "delete2=if(group_id=? and user2=?, ?, delete2)";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, group_id);
			stmt.setString(2, user);
			stmt.setInt(3, delete);
			stmt.setInt(4, group_id);
			stmt.setString(5, user);
			stmt.setInt(6, delete);
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
	public boolean setNew(int group_id, String user, int new_msg) {
		String sql = "update perGroup set new_msg1=if(group_id=? and user1=?, ?, new_msg1), "
				+ "new_msg2=if(group_id=? and user2=?, ?, new_msg2)";

		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, group_id);
			stmt.setString(2, user);
			stmt.setInt(3, new_msg);
			stmt.setInt(4, group_id);
			stmt.setString(5, user);
			stmt.setInt(6, new_msg);
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
	public PerGroup[] getNew(String address, int type) {
		String sql = "select * from perGroup where type=? and "
				+ "((user1=? and new_msg1=1 and delete1=0) or "
				+ "(user2=? and new_msg2=1 and delete2=0))";
		PreparedStatement stmt = Database.getPstmt(sql);
LinkedList<PerGroup> list = new LinkedList<>();
		
		try {
			stmt.setInt(1, type);
			stmt.setString(2, address);
			stmt.setString(3, address);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				PerGroup pg = new PerGroup();
				Database.getObject(rs, pg);
				list.add(pg);
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
			PerGroup[] groups = new PerGroup[list.size()];
			for(int i=0;i<groups.length;i++) {
				groups[i] = list.get(i);
			}
			return groups;
		}
	}
	
	@Override
	public PerGroup[] selectByUser(String user, int type) {
		String sql = "select * from perGroup where ((user1=? and delete1=0) "
				+ "or (user2=? and delete2=0)) and type=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<PerGroup> list = new LinkedList<>();
		
		try {
			stmt.setString(1, user);
			stmt.setString(2, user);
			stmt.setInt(3, type);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				PerGroup pg = new PerGroup();
				Database.getObject(rs, pg);
				list.add(pg);
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
			PerGroup[] groups = new PerGroup[list.size()];
			for(int i=0;i<groups.length;i++) {
				groups[i] = list.get(i);
			}
			return groups;
		}
	}
	
	public PerGroup selectByUser(String user1, String user2, int type) {
		String sql = "select * from perGroup where ((user1=? and user2=?) or (user1=? and user2=?)) and type=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		PerGroup pg = null;
		
		try {
			stmt.setString(1, user1);
			stmt.setString(2, user2);
			stmt.setString(3, user2);
			stmt.setString(4, user1);
			stmt.setInt(5, type);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				pg = new PerGroup();
				Database.getObject(rs, pg);
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
		return pg;
	}

	@Override
	public boolean exist(String user1, String user2, int type) {
		String sql = "select group_id from perGroup where ((user1=? and user2=?) or (user1=? and user2=?)) and type=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			stmt.setString(1, user1);
			stmt.setString(2, user2);
			stmt.setString(3, user2);
			stmt.setString(4, user1);
			stmt.setInt(5, type);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
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
	public int count() {
		String sql = "select count(group_id) num from perGroup";
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
	public boolean hasNew(String user, int type) {
		String sql = "select * from perGroup where type=? and exists("
				+ "select * from perGroup where (user1=? and new_msg1=1) or (user2=? and new_msg2=1))";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try{
			stmt.setInt(1, type);
			stmt.setString(2, user);
			stmt.setString(3, user);
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
	public boolean hasNew(int group_id, String user, int type) {
		String sql = "select * from perGroup where type=? and group_id=? and"
				+ " ((user1=? and new_msg1=1) or (user2=? and new_msg2=1))";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try{
			stmt.setInt(1, type);
			stmt.setInt(2, group_id);
			stmt.setString(3, user);
			stmt.setString(4, user);
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
}
