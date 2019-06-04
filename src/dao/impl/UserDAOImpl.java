package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.UserDAO;
import dao.vo.User;

public class UserDAOImpl implements UserDAO {

	@Override
	public boolean insert(User user) {
		PreparedStatement stmt = Database.getStmt("insert", user);
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
	public boolean update(User user) {
		PreparedStatement stmt = Database.getStmt("update", user);
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
	public User selectById(String id) {
		String sql = "select * from user where id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		User user = null;
		try {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				user = new User();
				Database.getObject(rs, user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public User[] selectAll() {
		LinkedList<User> users = new LinkedList<>();
		String sql = "select * from user";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				User newUser = new User();
				Database.getObject(rs, newUser);
				users.add(newUser);
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
		
		if(users.size() == 0) {
			return null;
		} else {
			User user[] = new User[users.size()];
			for(int i=0;i<user.length;i++) {
				user[i] = users.get(i);
			}
			return user;
		}
	}

	@Override
	public User[] selectByCondtion(HashMap<String, String> map) {
		LinkedList<User> users = new LinkedList<>();
		
		Iterator<String> keys = map.keySet().iterator();
		String cond = "";
		LinkedList<String> k = new LinkedList<>();
		LinkedList<String> v = new LinkedList<>();
		
		while(keys.hasNext()) {
			String key = keys.next();
			String value = map.get(key);
			k.add(key);
			v.add(value);
			
			cond += (key + " = " + "'" + value + "' and ");
		}
		cond = cond.substring(0, cond.length()-4);
		String sql = "select * from user where " + cond;
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				User newUser = new User();
				Database.getObject(rs, newUser);
				users.add(newUser);
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
		
		if(users.size() == 0) {
			return null;
		} else {
			User user[] = new User[users.size()];
			for(int i=0;i<user.length;i++) {
				user[i] = users.get(i);
			}
			return user;
		}
	}

	@Override
	public Integer count() {
		String sql = "select count(id) num from user";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int count = rs.getInt("num");
				return count;
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
		return 0;
	}
	
}
