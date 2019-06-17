package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.WishDAO;
import dao.vo.Wish;

public class WishDAOImpl implements WishDAO {

	@Override
	public boolean insert(Wish wish) {
		PreparedStatement stmt = Database.getStmt("insert", wish);
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
	public boolean update(Wish wish) {
		PreparedStatement stmt = Database.getStmt("update", wish);
		try {
			return stmt.execute();
		} catch(Exception e) {
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
	public boolean getById(int wish_id) {
		String sql = "update wish set is_get = 1 where wish_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, wish_id);
			return stmt.execute();
		} catch (SQLException e) {
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
	public boolean deleteById(int wish_id) {
		String sql = "update wish set is_delete = 1 where wish_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, wish_id);
			return stmt.execute();
		} catch (SQLException e) {
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
	public Wish selectById(int wish_id) {
		String sql = "select * from wish where wish_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		Wish wish = null;
		
		try {
			stmt.setInt(1, wish_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				wish = new Wish();
				Database.getObject(rs, wish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wish;
	}

	@Override
	public Wish[] selectByCond(HashMap<String, String> map) {
		Iterator<String> keys = map.keySet().iterator();
		String cond = "";
		
		while(keys.hasNext()) {
			String key = keys.next();
			String value = map.get(key);
			
			cond += (key + " = " + "'" + value + "' and ");
		}
		cond = cond.substring(0, cond.length()-4);
		String sql = "select * from wish where " + cond;
		
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<Wish> list = new LinkedList<>();
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Wish wish = new Wish();
				Database.getObject(rs, wish);
				list.add(wish);
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
			Wish wishes[] = new Wish[list.size()];
			for(int i=0;i<wishes.length;i++) {
				wishes[i] = list.get(i);
			}
			return wishes;
		}
	}
	
	@Override
	public int effectCount() {
		String sql = "select count(wish_id) num from wish where is_get = 0 and is_delete = 0";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int count() {
		String sql = "select count(wish_id) num from wish";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
