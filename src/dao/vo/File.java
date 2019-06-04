package dao.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class File implements Serializable, Cloneable {
	private String id;			// 上传者邮箱
	private String filename;	// 文件路径/储存器中的文件名（文件+上传者的hashcode），这是主键
	private String name;		// 文件真正的名字
	
	private String type;		// 文件类型：课件/试卷等
	private String time;		// 文件适用时间（2019春）
	private String subject;		// 科目
	private	int category;	// 文件分类：课内/课外
	private String description;	// 文件描述
	private String upload_time;	// 文件上传时间：格式yyyy-mm-dd
	private float score;		// 文件评分
	private int is_dir;		// 文件还是文件夹
	private int download;	// 下载量
	private String teacher;		// 该科目对应的老师
	private long size;		// 文件大小：单位B
	
	public File() {}
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public int getIs_dir() {
		return is_dir;
	}
	public void setIs_dir(int is_dir) {
		this.is_dir = is_dir;
	}
	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
	@Override
	public int hashCode() {
		// category | desciption | download | id | name | score
		// subject | teacher | time | type | upload_time | is_dir
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + download;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + is_dir;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(score);
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((upload_time == null) ? 0 : upload_time.hashCode());
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
		File other = (File) obj;
		
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		return true;
	}

	public String getKey() {
		return "filename";
	}
	
	@Override
	public String toString() {
		return "File [id=" + id + ", filename=" + filename + ", name=" + name
				+ ", type=" + type + ", time=" + time + ", subject=" + subject
				+ ", category=" + category + ", description=" + description
				+ ", upload_time=" + upload_time + ", score=" + score
				+ ", is_dir=" + is_dir + ", download=" + download
				+ ", teacher=" + teacher + ", size=" + size + "]";
	}
	
	public File clone() {
		File file = new File();
		file.setId(this.id);
		file.setFilename(this.filename);
		file.setName(this.name);
		file.setType(this.type);
		file.setTime(this.time);
		file.setSubject(this.subject);
		file.setCategory(this.category);
		file.setDescription(this.description);
		file.setUpload_time(this.upload_time);
		file.setScore(this.score);
		file.setIs_dir(this.is_dir);
		file.setDownload(this.download);
		file.setTeacher(this.teacher);
		file.setSize(this.size);
		return file;
	}
}
