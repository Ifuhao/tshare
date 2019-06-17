package dao.vo;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Wish implements Serializable, Cloneable {
	private int wish_id;		// 编号(主键)
	private String id;			// 发布者账号
	private String title;		// 标题
	private String description;	// 描述
	private Date time;			// 发布时间
	private int is_get;			// 是否得到
	private int is_delete;		// 是否删除
	public int getWish_id() {
		return wish_id;
	}
	public void setWish_id(int wish_id) {
		this.wish_id = wish_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getIs_get() {
		return is_get;
	}
	public void setIs_get(int is_get) {
		this.is_get = is_get;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + wish_id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wish other = (Wish) obj;
		if (wish_id != other.wish_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Wish [wish_id=" + wish_id + ", id=" + id + ", title=" + title
				+ ", description=" + description + ", time=" + time
				+ ", is_get=" + is_get + ", is_delete=" + is_delete + "]";
	}
	
	public String getKey() {
		return "wish_id";
	}
	
	public Wish clone() {
		Wish wish = new Wish();
		wish.setWish_id(this.wish_id);
		wish.setId(this.id);
		wish.setTitle(this.title);
		wish.setDescription(this.description);
		wish.setTime(this.time);
		wish.setIs_get(this.is_get);
		wish.setIs_delete(this.is_delete);
		return wish;
	}
}
