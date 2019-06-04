package dao.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Veri implements Serializable, Cloneable {
	private String id;	// 主键
	private String code;
	private long time;
	
	public Veri() {}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getKey() {
		return "id";
	}
	
	public Veri clone() {
		Veri veri = new Veri();
		veri.setId(this.id);
		veri.setCode(this.code);
		veri.setTime(this.time);
		return veri;
	}
}
