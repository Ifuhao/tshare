package dao.vo;

import java.io.Serializable;

/**
 * 用户实体类
 * @author fuhao
 */
@SuppressWarnings("serial")
public class User implements Serializable, Cloneable {
	private String id;		// 主键
	private String username;
	private String password;
	private String cookie_encode;
	private String cookie_decode;
	private int login_time;
	private int logout_time;
	private int session_id;
	
	private String head_image;		// 头像
	private int upload_time;		// 文件上传量
	private int transaction;		// 交易成功量
	private int money;				// 积分
	private int follow;				// 关注
	private int fans;				// 粉丝
	
	public User() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCookie_encode() {
		return cookie_encode;
	}
	public void setCookie_encode(String cookie_encode) {
		this.cookie_encode = cookie_encode;
	}
	public String getCookie_decode() {
		return cookie_decode;
	}
	public void setCookie_decode(String cookie_decode) {
		this.cookie_decode = cookie_decode;
	}
	public int getLogin_time() {
		return login_time;
	}
	public void setLogin_time(int login_time) {
		this.login_time = login_time;
	}
	public int getLogout_time() {
		return logout_time;
	}
	public void setLogout_time(int logout_time) {
		this.logout_time = logout_time;
	}
	public int getSession_id() {
		return session_id;
	}
	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}
	
	public String getHead_image() {
		return head_image;
	}
	public void setHead_image(String head_image) {
		this.head_image = head_image;
	}
	public int getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(int upload_time) {
		this.upload_time = upload_time;
	}
	public int getTransaction() {
		return transaction;
	}
	public void setTransaction(int transaction) {
		this.transaction = transaction;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getFollow() {
		return follow;
	}
	public void setFollow(int follow) {
		this.follow = follow;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cookie_decode == null) ? 0 : cookie_decode.hashCode());
		result = prime * result
				+ ((cookie_encode == null) ? 0 : cookie_encode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + login_time;
		result = prime * result + logout_time;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + session_id;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getKey() {
		return "id";
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", cookie_encode=" + cookie_encode
				+ ", cookie_decode=" + cookie_decode + ", login_time="
				+ login_time + ", logout_time=" + logout_time + ", session_id="
				+ session_id + ", head_image=" + head_image + ", upload_time="
				+ upload_time + ", transaction=" + transaction + ", money="
				+ money + ", follow=" + follow + ", fans=" + fans + "]";
	}
	
	
	public User clone() {
		User user = new User();
		user.setId(this.id);
		user.setUsername(this.username);
		user.setPassword(this.password);
		user.setCookie_encode(this.cookie_encode);
		user.setCookie_decode(this.cookie_decode);
		user.setLogin_time(this.login_time);
		user.setLogout_time(this.logout_time);
		user.setSession_id(this.session_id);
		user.setHead_image(this.head_image);
		user.setUpload_time(this.upload_time);
		user.setTransaction(this.transaction);
		user.setMoney(this.money);
		user.setFollow(this.follow);
		user.setFans(this.fans);
		return user;
	}
}
