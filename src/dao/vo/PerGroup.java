package dao.vo;

import java.io.Serializable;

/**
 * 用户组表实体类
 * @author fuhao
 */
@SuppressWarnings("serial")
public class PerGroup implements Serializable, Cloneable {
	public static final int TYPE_TRANSACTION = 1;
	public static final int TYPE_PERSONAL = 2;
	
	private int group_id;		// 组编号
	private int type;			// 类型
	private String user1;		// 用户1
	private String user2;		// 用户2
	
	private int new_msg1;		// 用户1是否有未读消息
	private int new_msg2;		// 用户2是否有未读消息
	private int delete1;		// 用户1是否退出组
	private int delete2;		// 用户2是否退出组
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public int getNew_msg1() {
		return new_msg1;
	}
	public void setNew_msg1(int new_msg1) {
		this.new_msg1 = new_msg1;
	}
	public int getNew_msg2() {
		return new_msg2;
	}
	public void setNew_msg2(int new_msg2) {
		this.new_msg2 = new_msg2;
	}
	public int getDelete1() {
		return delete1;
	}
	public void setDelete1(int delete1) {
		this.delete1 = delete1;
	}
	public int getDelete2() {
		return delete2;
	}
	public void setDelete2(int delete2) {
		this.delete2 = delete2;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + group_id;
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
		PerGroup other = (PerGroup) obj;
		if (group_id != other.group_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PerGroup [group_id=" + group_id + ", type=" + type + ", user1="
				+ user1 + ", user2=" + user2 + ", new_msg1=" + new_msg1
				+ ", new_msg2=" + new_msg2 + ", delete1=" + delete1
				+ ", delete2=" + delete2 + "]";
	}
	
	public PerGroup clone() {
		PerGroup pg = new PerGroup();
		pg.setGroup_id(this.group_id);
		pg.setType(this.type);
		pg.setUser1(this.user1);
		pg.setUser2(this.user2);
		pg.setNew_msg1(this.new_msg1);
		pg.setNew_msg2(this.new_msg2);
		pg.setDelete1(this.delete1);
		pg.setDelete2(this.delete2);
		return pg;
	}
}
