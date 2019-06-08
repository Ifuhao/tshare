package dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import utils.Database;
import dao.SeekDAO;
import dao.vo.Seek;

public class SeekDAOImpl implements SeekDAO {

	@Override
	public boolean insert(Seek seek) {
		PreparedStatement stmt = Database.getStmt("insert", seek);
		System.out.println(stmt);
		try{
			return stmt.execute();
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
	public boolean deleteById(int seek_id) {
		String sql = "update sale set delete = 1 where seek_id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try{
			stmt.setInt(1, seek_id);
			return stmt.execute();
		}catch (SQLException e) {
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
	public boolean buyBy_Id(int seek_id) {
		String sql = "update seek set is_buy = 1 where seek_id = ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		try {
			stmt.setInt(1,seek_id);
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return false;
	}

	@Override
	public Seek selectById(int seek_id) {
		String sql = "select * from seek where seek_id=?";
		PreparedStatement stmt = Database.getPstmt(sql);
		Seek seek = null;
		
		try {
			stmt.setInt(1, seek_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				seek = new Seek();
				Database.getObject(rs, seek);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Seek[] select(String field, String value) {
		String sql = "select * from seek where "+field+" like ?";
		PreparedStatement stmt = Database.getPstmt(sql);
		
		LinkedList<Seek> list = new LinkedList<>();
		
		try {
			stmt.setString(1,"%" + value +"%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Seek seek = new Seek();
				Database.getObject(rs, seek);
				list.add(seek);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(list.size() == 0){
			return null;
		}else{
			Seek seeks[] = new Seek[list.size()];
			for(int i=0;i<list.size();i++){
				seeks[i] = list.get(i);
			}
			return seeks;
		}
		
	}

	@Override
	public Seek[] selectByCond(HashMap<String, String> map) {
		Iterator<String>keys = map.keySet().iterator();
		String cond = "";
		
		while(keys.hasNext()){
			String key = keys.next();
			String value = map.get(key);
			cond+= (key + "=" + "'" + value + " ' and");
		} 
		cond = cond.substring(0,cond.length()-4);
		String sql = "select * from seek where " + cond;
		PreparedStatement stmt = Database.getPstmt(sql);
		
		LinkedList<Seek> list = new LinkedList<>();
		
		try {
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Seek seek = new Seek();
				Database.getObject(rs, seek);
				list.add(seek);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(list.size() == 0){
			return null;
		}else{
			Seek seeks[] = new Seek[list.size()];
			for(int i=0;i<seeks.length;i++){
				seeks[i] = list.get(i);
			}
			return seeks;
		}
	}
	
}