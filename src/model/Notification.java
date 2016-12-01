package model;

public class Notification {
	int notifiId;
	int userId;
	String content;
	int status;
	int seen;
	String notiDate;
	int exchId;
	
	public int getNotifiId() {
		return notifiId;
	}
	public void setNotifiId(int notifiId) {
		this.notifiId = notifiId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSeen() {
		return seen;
	}
	public void setSeen(int seen) {
		this.seen = seen;
	}
	public String getNotiDate() {
		return notiDate;
	}
	public void setNotiDate(String notiDate) {
		this.notiDate = notiDate;
	}
	public int getExchId() {
		return exchId;
	}
	public void setExchId(int exchId) {
		this.exchId = exchId;
	}
	
	
}
