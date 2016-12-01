package model;

public class User {

	int u_id;
	String u_wechat_id;
	String u_name;
	String u_email;
	String u_pw_hash;
	String u_fullname;
	String u_token;
	
	public User(){};
	public User(int id, String wechatid, String name, String email,
			String password, String fullname, String token) {
		super();
		this.u_id = id;
		this.u_wechat_id = wechatid;
		this.u_name = name;
		this.u_email = email;
		this.u_pw_hash = password;
		this.u_fullname = fullname;
		this.u_token = token;
	}
	public int getId() {
		return u_id;
	}
	public void setId(int id) {
		this.u_id = id;
	}
	public String getWechatid() {
		return u_wechat_id;
	}
	public void setWechatid(String wechatid) {
		this.u_wechat_id = wechatid;
	}
	public String getName() {
		return u_name;
	}
	public void setName(String name) {
		this.u_name = name;
	}
	public String getEmail() {
		return u_email;
	}
	public void setEmail(String email) {
		this.u_email = email;
	}
	public String getPassword() {
		return u_pw_hash;
	}
	public void setPassword(String password) {
		this.u_pw_hash = password;
	}
	public String getFullname() {
		return u_fullname;
	}
	public void setFullname(String fullname) {
		this.u_fullname = fullname;
	}
	public String getToken() {
		return u_token;
	}
	public void setToken(String token) {
		this.u_token = token;
	}
	public String getU_wechat_id() {
		return u_wechat_id;
	}
	public void setU_wechat_id(String u_wechat_id) {
		this.u_wechat_id = u_wechat_id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_email() {
		return u_email;
	}
	public void setU_email(String u_email) {
		this.u_email = u_email;
	}
	public String getU_pw_hash() {
		return u_pw_hash;
	}
	public void setU_pw_hash(String u_pw_hash) {
		this.u_pw_hash = u_pw_hash;
	}
	public String getU_fullname() {
		return u_fullname;
	}
	public void setU_fullname(String u_fullname) {
		this.u_fullname = u_fullname;
	}
	public String getU_token() {
		return u_token;
	}
	public void setU_token(String u_token) {
		this.u_token = u_token;
	}
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	
	
}
