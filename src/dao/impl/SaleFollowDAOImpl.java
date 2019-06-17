package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import utils.Database;
import dao.SaleFollowDAO;
import dao.vo.SaleFollow;

public class SaleFollowDAOImpl implements SaleFollowDAO {

	@Override
	public boolean insert(SaleFollow obj) {
		PreparedStatement stmt = Database.getStmt("insert", obj);
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
	public boolean deleteById(int follow_id) {
		String de_sql = "delete from saleFollow where follow_id=?";
		String up_sql = "update saleFollow set follow_id = follow_id-1 where follow_id > ?";
		PreparedStatement de_stmt = Database.getPstmt(de_sql);
		PreparedStatement up_stmt = Database.getPstmt(up_sql);
		
		try {
			de_stmt.setInt(1, follow_id);
			up_stmt.setInt(1, follow_id);
			de_stmt.execute();
			up_stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				de_stmt.close();
				up_stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public SaleFollow selectByFollowId(int follow_id) {
		String sql = "select * from saleFollow where follow_id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		SaleFollow obj = null;
		
		try {
			stmt.setInt(1, follow_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				obj = new SaleFollow();
				Database.getObject(rs, obj);
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
		return obj;
	}

	@Override
	public SaleFollow[] selectBySaleId(int sale_id) {
		String sql = "select * from saleFollow where sale_id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<SaleFollow> list = new LinkedList<>();
		
		try {
			stmt.setInt(1, sale_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				SaleFollow obj = new SaleFollow();
				Database.getObject(rs, obj);
				list.add(obj);
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
			SaleFollow[] arr = new SaleFollow[list.size()];
			for(int i=0;i<arr.length;i++) {
				arr[i] = list.get(i);
			}
			return arr;
		}
	}

	@Override
	public SaleFollow[] selectById(String id) {
		String sql = "select * from saleFollow where id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<SaleFollow> list = new LinkedList<>();
		
		try {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				SaleFollow obj = new SaleFollow();
				Database.getObject(rs, obj);
				list.add(obj);
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
			SaleFollow[] arr = new SaleFollow[list.size()];
			for(int i=0;i<arr.length;i++) {
				arr[i] = list.get(i);
			}
			return arr;
		}
	}

	public int count() {
		String sql = "select count(follow_id) num from saleFollow";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("num");
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
