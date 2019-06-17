package dao.vo;

import java.io.Serializable;

/**
 * 私信表实体类
 * @author fuhao
 */
@SuppressWarnings("serial")
public class PerNotice implements Serializable, Cloneable {
	
	private int msg_id;			// 消息编号，唯一主键
	private int group_id;		// 用户组号
	private String sender;		// 发送方邮箱
	private String address;		// 接收方邮箱
	private String content;		// 消息内容
	private long time;			// 发送时间
	private int is_read;		// 接收方是否已读
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + msg_id;
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
		PerNotice other = (PerNotice) obj;
		if (msg_id != other.msg_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PerNotice [msg_id=" + msg_id + ", group_id=" + group_id
				+ ", sender=" + sender + ", address=" + address + ", content="
				+ content + ", time=" + time + ", is_read=" + is_read + "]";
	}
	
	public PerNotice clone() {
		PerNotice pn = new PerNotice();
		pn.setMsg_id(this.msg_id);
		pn.setGroup_id(this.group_id);
		pn.setSender(this.sender);
		pn.setAddress(this.address);
		pn.setContent(this.content);
		pn.setTime(this.time);
		pn.setIs_read(this.is_read);
		return pn;
	}
}
