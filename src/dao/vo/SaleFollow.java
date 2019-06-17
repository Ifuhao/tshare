package dao.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SaleFollow implements Serializable, Cloneable {
	
	private int follow_id;		// 主键
	private int sale_id;		// 关注的商品编号
	private String id;			// 关注该商品的用户账号
	public int getFollow_id() {
		return follow_id;
	}
	public void setFollow_id(int follow_id) {
		this.follow_id = follow_id;
	}
	public int getSale_id() {
		return sale_id;
	}
	public void setSale_id(int sale_id) {
		this.sale_id = sale_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + follow_id;
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
		SaleFollow other = (SaleFollow) obj;
		if (follow_id != other.follow_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Sale_follow [follow_id=" + follow_id + ", sale_id=" + sale_id
				+ ", id=" + id + "]";
	}
	
	public String getKey() {
		return "follow_id";
	}
	
	public SaleFollow clone() {
		SaleFollow obj = new SaleFollow();
		obj.setFollow_id(this.follow_id);
		obj.setSale_id(this.sale_id);
		obj.setId(this.id);
		return obj;
	}
}
