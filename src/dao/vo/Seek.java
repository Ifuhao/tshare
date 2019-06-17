package dao.vo;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Seek implements Serializable,Cloneable{
	private int seek_id;			// 收购商品编号
	private String id;				// 发起者邮箱
	private String title;			// 商品标题
	private String description;		// 商品描述
	private Date time;				// 发布时间
	private int is_buy;				// 是否交易成功
	private int is_delete;			// 是否删除商品
	
	public Seek(){}
	
	public int getSeek_id() {
		return seek_id;
	}
	public void setSeek_id(int seek_id) {
		this.seek_id = seek_id;
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
	public int getIs_buy() {
		return is_buy;
	}
	public void setIs_buy(int is_buy) {
		this.is_buy = is_buy;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	
	public String getKey() {
		return "seek_id";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + seek_id;
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
		Seek other = (Seek) obj;
		if (seek_id != other.seek_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seek [seek_id=" + seek_id + ", id=" + id + ", title=" + title
				+ ", description=" + description + ", accept_price="
				+ ", time=" + time + ", is_buy=" + is_buy
				+ ", is_delete=" + is_delete + "]";
	}

	public Seek clone(){
		Seek seek = new Seek();
		seek.setSeek_id(this.seek_id);
		seek.setId(this.id);
		seek.setTitle(this.title);
		seek.setDescription(this.description);
		seek.setTime(this.time);
		seek.setIs_buy(this.is_buy);
		seek.setIs_delete(this.is_delete);
		return seek;
	}
}
