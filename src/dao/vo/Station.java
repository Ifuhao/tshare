package dao.vo;

public class Station {
	private int upload_time;
	private int download_time;
	private int file_number;
	
	public Station() {}

	public int getUpload_time() {
		return upload_time;
	}

	public void setUpload_time(int upload_time) {
		this.upload_time = upload_time;
	}

	public int getDownload_time() {
		return download_time;
	}

	public void setDownload_time(int download_time) {
		this.download_time = download_time;
	}

	public int getFile_number() {
		return file_number;
	}

	public void setFile_number(int file_number) {
		this.file_number = file_number;
	}

	@Override
	public String toString() {
		return "Station [upload_time=" + upload_time + ", download_time="
				+ download_time + ", file_number=" + file_number + "]";
	}
	
	public String getKey() {
		return null;
	}
}
