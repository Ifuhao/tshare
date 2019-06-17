package dao.vo;

import java.io.Serializable;

/**
 * 系统消息接收表实体类
 * @author fuhao
 */
@SuppressWarnings("serial")
public class SysNoticeAccept implements Serializable, Cloneable {
	
	private int accept_id;		// 接受编号
	private int sys_id;
	private String address;			// 接收方邮箱
	private int is_read;			// 接收方是否已读
	private int is_delete;			// 接收方是否删除
	public int getAccept_id() {
		return accept_id;
	}
	public void setAccept_id(int accept_id) {
		this.accept_id = accept_id;
	}
	public int getSys_id() {
		return sys_id;
	}
	public void setSys_id(int sys_id) {
		this.sys_id = sys_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int is_read) {
		this.is_read = is_read;
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
		result = prime * result + accept_id;
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
		SysNoticeAccept other = (SysNoticeAccept) obj;
		if (accept_id != other.accept_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SysNoticeAccept [sys_accept_id=" + accept_id + ", sys_id="
				+ sys_id + ", address=" + address + ", is_read=" + is_read
				+ ", is_delete=" + is_delete + "]";
	}
	
	public SysNoticeAccept clone() {
		SysNoticeAccept sna = new SysNoticeAccept();
		sna.setAccept_id(this.accept_id);
		sna.setSys_id(this.sys_id);
		sna.setAddress(this.address);
		sna.setIs_read(this.is_read);
		sna.setIs_delete(this.is_delete);
		return sna;
	}
}
