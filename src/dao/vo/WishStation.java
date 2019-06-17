package dao.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WishStation implements Serializable, Cloneable {
	private int user;
	private int achieve;
	private int wishes;
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getAchieve() {
		return achieve;
	}
	public void setAchieve(int achieve) {
		this.achieve = achieve;
	}
	public int getWishes() {
		return wishes;
	}
	public void setWishes(int wishes) {
		this.wishes = wishes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + achieve;
		result = prime * result + user;
		result = prime * result + wishes;
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
		WishStation other = (WishStation) obj;
		if (achieve != other.achieve)
			return false;
		if (user != other.user)
			return false;
		if (wishes != other.wishes)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WishStation [user=" + user + ", achieve=" + achieve
				+ ", wishes=" + wishes + "]";
	}
	
	public WishStation clone() {
		WishStation obj = new WishStation();
		obj.setUser(this.user);
		obj.setAchieve(this.achieve);
		obj.setWishes(this.wishes);
		return obj;
	}
}
