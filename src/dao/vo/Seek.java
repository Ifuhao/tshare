package dao.vo;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Seek implements Serializable,Cloneable{
	private int seek_id;	//收购商品编号
	private String id;		//发起者邮箱
	private String title;	//商品标题
	private String description;	//商品描述
	private Date time;		//发布时间
	private int is_buy;		//是否交易成功
	private int is_delete;		//是否删除商品
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + is_delete;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + is_buy;
		result = prime * result + seek_id;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		if (is_delete != other.is_delete)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (is_buy != other.is_buy)
			return false;
		if (seek_id != other.seek_id)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seek [seek_id=" + seek_id + ", id=" + id + ", title=" + title
				+ ", description=" + description + ", time=" + time
				+ ", is_buy=" + is_buy + ", delete=" + is_delete + "]";
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
