package model;

public class Offer {
	int offer_id;
	int user_id;
	int seller_from;
	int seller_to;
	int points_from;
	int points_to_min;
	String status;
	
	
	public int getOffer_id() {
		return offer_id;
	}
	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getSeller_from() {
		return seller_from;
	}
	public void setSeller_from(int seller_from) {
		this.seller_from = seller_from;
	}
	public int getSeller_to() {
		return seller_to;
	}
	public void setSeller_to(int seller_to) {
		this.seller_to = seller_to;
	}
	public int getPoints_from() {
		return points_from;
	}
	public void setPoints_from(int points_from) {
		this.points_from = points_from;
	}
	public int getPoints_to_min() {
		return points_to_min;
	}
	public void setPoints_to_min(int points_to_min) {
		this.points_to_min= points_to_min;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status= status;
	}
}
