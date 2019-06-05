package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.SaleDAO;
import dao.vo.Sale;

public class SaleDAOImpl implements SaleDAO {

	@Override
	public boolean insert(Sale sale) {
		PreparedStatement stmt = Database.getStmt("insert", sale);
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
	public boolean deleteById(int sale_id) {
		String sql = "update sale set delete = 1 where sale_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, sale_id);
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
	public boolean sellById(int sale_id) {
		String sql = "update sale set is_sell = 1 where sale_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, sale_id);
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
	public Sale selectById(int sale_id) {
		String sql = "select * from sale where sale_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		Sale sale = null;
		
		try {
			stmt.setInt(1, sale_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				sale = new Sale();
				Database.getObject(rs, sale);
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
		
		return sale;
	}

	@Override
	public Sale[] select(String field, String value) {
		String sql = "select * from sale where " + field + " like ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		LinkedList<Sale> list = new LinkedList<>();
		
		try {
			stmt.setString(1, "%" + value + "%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Sale sale = new Sale();
				Database.getObject(rs, sale);
				list.add(sale);
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
		
		if(list.size() == 0) {
			return null;
		} else {
			Sale sales[] = new Sale[list.size()];
			for(int i=0;i<list.size();i++) {
				sales[i] = list.get(i);
			}
			return sales;
		}
	}

	@Override
	public Sale[] selectByCond(HashMap<String, String> map) {
		Iterator<String> keys = map.keySet().iterator();
		String cond = "";
		
		while(keys.hasNext()) {
			String key = keys.next();
			String value = map.get(key);
			
			cond += (key + " = " + "'" + value + "' and ");
		}
		cond = cond.substring(0, cond.length()-4);
		String sql = "select * from sale where " + cond;
		PreparedStatement stmt = Database.getPstmt(sql);
		
		LinkedList<Sale> list = new LinkedList<>();
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Sale sale = new Sale();
				Database.getObject(rs, sale);
				list.add(sale);
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
			Sale sales[] = new Sale[list.size()];
			for(int i=0;i<sales.length;i++) {
				sales[i] = list.get(i);
			}
			return sales;
		}
	}

	@Override
	public int count() {
		String sql = "select count(sale_id) num from sale";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("num");
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
		return 0;
	}
}
