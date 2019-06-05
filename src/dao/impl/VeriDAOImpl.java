package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Database;
import dao.VeriDAO;
import dao.vo.Veri;


public class VeriDAOImpl implements VeriDAO {

	@Override
	public boolean insert(Veri veri) {
		PreparedStatement stmt = Database.getStmt("insert", veri);
		try {			
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
	public boolean deleteById(String id) {
		String sql = "delete from veri where id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			stmt.setString(1, id);
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
	public Veri selectById(String id) {
		String sql = "select * from veri where id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		Veri veri = null;
		try {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				veri = new Veri();
				Database.getObject(rs, veri);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return veri;
	}
}
