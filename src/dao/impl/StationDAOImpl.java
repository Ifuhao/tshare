package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Database;
import dao.StationDAO;
import dao.vo.Station;

public class StationDAOImpl implements StationDAO {
	
	public boolean update(Station station) {
		String sql = "update station set upload_time=?, download_time=?, file_number=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			stmt.setInt(1, station.getUpload_time());
			stmt.setInt(2, station.getDownload_time());
			stmt.setInt(3, station.getFile_number());
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
	public boolean addUpload_time() {
		return this.addUpload_time(1);
	}

	@Override
	public boolean addUpload_time(int times) {
		Station station = this.select();
		station.setUpload_time(station.getUpload_time()+times);
		return this.update(station);
	}

	@Override
	public boolean addDownload_time() {
		return this.addDownload_time(1);
	}

	@Override
	public boolean addDownload_time(int times) {
		Station station = this.select();
		station.setDownload_time(station.getDownload_time()+times);
		return this.update(station);
	}

	@Override
	public boolean addFile_number() {
		return this.addFile_number(1);
	}

	@Override
	public boolean addFile_number(int numbers) {
		Station station = this.select();
		station.setFile_number(station.getFile_number()+numbers);
		return this.update(station);
	}

	@Override
	public Station select() {
		String sql = "select * from station";
		PreparedStatement stmt = Database.getPstmt(sql);
		ResultSet rs = null;
		
		Station station = null;
		
		try {
			rs = stmt.executeQuery();
			while(rs.next()) {
				station = new Station();
				Database.getObject(rs, station);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return station;
	}

}
