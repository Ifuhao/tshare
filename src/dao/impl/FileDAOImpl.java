package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.FileDAO;
import dao.vo.File;

public class FileDAOImpl implements FileDAO {
	@Override
	public boolean insert(File file) {
		file.setScore(5);
		PreparedStatement stmt = Database.getStmt("insert" ,file);
		try {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public boolean update(File file) {
		PreparedStatement stmt = Database.getStmt("update", file);
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
	public File selectByFilename(String filename) {
		String sql = "select * from file where filename = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		File file = null;
		try {
			stmt.setString(1, filename);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				file = new File();
				Database.getObject(rs, file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return file;
	}

	@Override
	public File[] selectAll() {
		String sql = "select * from file";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<File> files = new LinkedList<>();
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				File newFile = new File();
				Database.getObject(rs, newFile);
				files.add(newFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(files.size() == 0) {
			return null;
		} else {
			File[] file =  new File[files.size()];
			for(int i=0;i<file.length;i++) {
				file[i] = files.get(i);
			}
			
			return file;
		}
	}

	@Override
	public File[] selectByCond(HashMap<String, String> map) {
		LinkedList<File> files = new LinkedList<>();
		
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
		String sql = "select * from file where " + cond;
		PreparedStatement stmt = Database.getPstmt(sql);
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				File newFile = new File();
				Database.getObject(rs, newFile);
				files.add(newFile);
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
		
		if(files.size() == 0) {
			return null;
		} else {
			File file[] = new File[files.size()];
			for(int i=0;i<file.length;i++) {
				file[i] = files.get(i);
			}
			return file;
		}
	}

	@Override
	public File[] select(String field, String value, boolean is_dir) {
		String sql = "select * from file where " + field + " like ? and is_dir = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		LinkedList<File> files = new LinkedList<>();
		
		try {
			stmt.setString(1, "%"+value+"%");
			stmt.setInt(2, is_dir?1:0);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				File newFile = new File();
				Database.getObject(rs, newFile);
				files.add(newFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(files.size() == 0) {
			return null;
		} else {
			File file[] = new File[files.size()];
			for(int i=0;i<file.length;i++) {
				file[i] = files.get(i);
			}
			return file;
		}
	}
}
