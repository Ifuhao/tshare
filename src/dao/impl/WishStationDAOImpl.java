package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Database;
import dao.WishStationDAO;
import dao.vo.WishStation;

public class WishStationDAOImpl implements WishStationDAO {

	@Override
	public boolean setUser(int num) {
		String sql = "update wishStation set user = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, this.select().getUser()+num);
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
	public boolean addUser() {
		return this.setUser(1);
	}

	@Override
	public boolean setAchieve(int num) {
		String sql = "update wishStation set achieve = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, this.select().getAchieve()+num);
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
	public boolean addAchieve() {
		return this.setAchieve(1);
	}

	@Override
	public boolean setWishes(int num) {
		String sql = "update wishStation set wishes = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1, this.select().getWishes()+num);
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
	public boolean addWishes() {
		return this.setWishes(1);
	}

	@Override
	public WishStation select() {
		String sql = "select * from wishStation";
		PreparedStatement stmt = Database.getPstmt(sql);
		WishStation obj = null;
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				obj = new WishStation();
				Database.getObject(rs, obj);
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
		return obj;
	}

}
