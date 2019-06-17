package dao.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DlFile implements Serializable, Cloneable {
	private int did;
	private String id;
	private String filename;
	private int ismark;
	private int score;
	private String time;
	
	public DlFile() {}
	
	public void setDid(int did) {
		this.did = did;
	}
	public int getDid() {
		return did;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getIsmark() {
		return ismark;
	}
	public void setIsmark(int ismark) {
		this.ismark = ismark;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ismark;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		DlFile other = (DlFile) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ismark != other.ismark)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	
	/**
	 * 获取主键名称
	 * @return
	 */
	public String getKey() {
		return "did";
	}

	@Override
	public String toString() {
		return "Download_file [did=" + did + ", id=" + id + ", filename="
				+ filename + ", ismark=" + ismark + ", time=" + time + "]";
	}
	
	public DlFile clone() {
		DlFile newFile = new DlFile();
		newFile.setDid(this.did);
		newFile.setId(this.id);
		newFile.setFilename(this.filename);
		newFile.setIsmark(this.ismark);
		newFile.setScore(this.score);
		newFile.setTime(this.time);
		return newFile;
	}
}
