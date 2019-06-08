package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import utils.form.ClassUtils;
import utils.form.StringUtils;

public class Database {
	private static Connection conn = null;
	
	public static Connection getConn() throws Exception {
		if(conn == null || conn.isClosed()) {
			ResourceBundle rb = ResourceBundle.getBundle("db");
			String driver = rb.getString("driver");
			String url = rb.getString("url") + "/" + rb.getString("dbname") + "?" + "useSSL=" + rb.getString("ssl");
			String user = rb.getString("user");
			String password = rb.getString("password");
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		}
		return conn;
	}
	
	/**
	 * 根据数据库操作返回不同的stmt
	 * @param operator 包括插入、更新
	 * @param obj
	 * @return
	 */
	public static <T> PreparedStatement getStmt(String operator, T obj) {
		String sql = "";
		Class<?> cls = obj.getClass();
		String table = StringUtils.lowerCap(cls.getSimpleName());
		Field fields[] = cls.getDeclaredFields();
		
		PreparedStatement stmt = null;
		if(operator.equals("insert")) {
			// 插入
			sql = "insert into " + table + " values(";
			for(int i=0;i<fields.length;i++) {
				sql += "?, ";
			}
			sql = sql.substring(0, sql.length()-2)+")";
			stmt = Database.getPstmt(sql);
		} else if(operator.equals("update")) {
			// 更新
			sql = "update " + table  + " set ";
			for(int i=0;i<fields.length;i++) {
				sql += fields[i].getName() + "=?, ";
			}
			
			try {
				Method getMet = cls.getDeclaredMethod("getKey");
				// 获取主键名
				String key = (String) getMet.invoke(obj);
				sql = sql.substring(0, sql.length()-2)+" where " + key + " = ?";
				stmt = Database.getPstmt(sql);
				
				// 获取主键类型，用于设置stmt的where参数
				Class<?> keyType = ClassUtils.getClass(cls.getDeclaredField(key).getType());
				// 获取主键值
				getMet = cls.getDeclaredMethod("get" + StringUtils.upperCap(key));
				
				// 获取设置主键的stmt方法
				Class<?> stmtCls = PreparedStatement.class;
				Method setMet = stmtCls.getDeclaredMethod("set" + StringUtils.upperCap(keyType.getSimpleName()), int.class, keyType);
				// 设置条件值
				setMet.invoke(stmt, fields.length+1, getMet.invoke(obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Database.setParam(stmt, obj);
		return stmt;
	}
	
	/**
	 * @param sql
	 * @return
	 */
	public static PreparedStatement getPstmt(String sql) {
		try {
			return Database.getConn().prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据结果集设置对象的成员值
	 * @param rs
	 * @param obj
	 */
	public static <T> void getObject(ResultSet rs, T obj) {
		Class<?> objCls = obj.getClass();
		Class<?> rsObj = ResultSet.class;
		
		Field[] fields = objCls.getDeclaredFields(); // Sale
		for(int i=0;i<fields.length;i++) {
			String fieldName = fields[i].getName();		// sale_id
			Class<?> fieldType = ClassUtils.getClass(fields[i].getType());
			
			try {
				// rs的getter方法，用于获取值
				Method getMet = rsObj.getDeclaredMethod("get" + StringUtils.upperCap(fieldType.getSimpleName()), String.class);

				// obj的setter方法，用于给对象的成员设置值
				Method setMet = objCls.getDeclaredMethod("set" + StringUtils.upperCap(fieldName), fieldType);
				
				setMet.invoke(obj, getMet.invoke(rs, fieldName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 给stmt设置参数
	 * @param stmt
	 * @param obj
	 */
	private static <T> void setParam(PreparedStatement stmt, T obj) {
		Class<?> cls = obj.getClass();
		Field fields[] = cls.getDeclaredFields();
		
		Class<?> stmtCls = PreparedStatement.class;
		
		// 设置PreparedStatement的参数
		try {
			for(int i=0;i<fields.length;i++) {
				Field field = fields[i];
				Class<?> fieldCls = ClassUtils.getClass(field.getType());
				String fieldType = fieldCls.getSimpleName();		// 获取属性类型用于拼接stmt的setter方法
				
				// stmt的setter方法
				Method setMet = stmtCls.getDeclaredMethod("set" + StringUtils.upperCap(fieldType), int.class, fieldCls);
				// 属性的getter方法
				Method getMet = cls.getDeclaredMethod("get" + StringUtils.upperCap(field.getName()));
				// 获取值
				Object value = getMet.invoke(obj);
				
				// mysql数据库中的基本类型：varchar->String | int -> int | float -> float
				if(value == null) {
					// 给空数据设置默认值
					if(ClassUtils.isInt(fieldCls)){
						value = 0;
					} else if(ClassUtils.isFloat(fieldCls)) {
						value = 0.0;
					} else if(ClassUtils.isString(fieldCls)) {
						value = "";
					}
				}
				
				// 设置stmt的参数
				setMet.invoke(stmt, i+1, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
