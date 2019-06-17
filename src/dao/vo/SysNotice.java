package dao.vo;

import java.io.Serializable;

/**
 * 系统消息内容表实体类
 * @author fuhao
 */
@SuppressWarnings("serial")
public class SysNotice implements Serializable, Cloneable {
	
	private int sys_id;		// 系统消息编号
	private String title;	// 消息标题
	private String content;	// 消息内容
	private long time;		// 平台发送消息时间
	private int is_delete;	// 平台是否删除
	public int getSys_id() {
		return sys_id;
	}
	public void setSys_id(int sys_id) {
		this.sys_id = sys_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		result = prime * result + sys_id;
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
		SysNotice other = (SysNotice) obj;
		if (sys_id != other.sys_id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SysNotice [sys_id=" + sys_id + ", title=" + title
				+ ", content=" + content + ", time=" + time + ", is_delete="
				+ is_delete + "]";
	}
	
	public SysNotice clone() {
		SysNotice sn = new SysNotice();
		sn.setSys_id(this.sys_id);
		sn.setTitle(this.title);
		sn.setContent(this.content);
		sn.setTime(this.time);
		sn.setIs_delete(this.is_delete);
		return sn;
	}
}
