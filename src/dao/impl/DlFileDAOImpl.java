package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.DlFileDAO;
import dao.vo.DlFile;

public class DlFileDAOImpl implements DlFileDAO {

	@Override
	public boolean insert(DlFile dlFile) {
		PreparedStatement stmt = Database.getStmt("insert", dlFile);
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
	public boolean update(DlFile dlFile) {
		PreparedStatement stmt = Database.getStmt("update", dlFile);
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
	public DlFile selectByDid(int did) {
		String sql = "select * from dlFile where did = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		DlFile dlFile = null;
		try {
			stmt.setInt(1, did);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				dlFile = new DlFile();
				Database.getObject(rs, dlFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dlFile;
	}

	@Override
	public DlFile[] selectByCond(HashMap<String, String> map) {
		LinkedList<DlFile> dlFiles = new LinkedList<>();
		
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
		String sql = "select * from dlFile where " + cond;
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				DlFile newFile = new DlFile();
				Database.getObject(rs, newFile);
				dlFiles.add(newFile);
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
		
		if(dlFiles.size() == 0) {
			return null;
		} else {
			DlFile files[] = new DlFile[dlFiles.size()];
			for(int i=0;i<files.length;i++) {
				files[i] = dlFiles.get(i);
			}
			return files;
		}
	}

	@Override
	public int count() {
		String sql = "select count(did) num from dlFile";
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
