package model;

import javax.persistence.Transient;

public class Request {
	int Rid;
	int userFrom;
	int userTo;
	int sellerFrom;
	int sellerTo;
	int pointsFrom;
	int pointsTo;
	int offerFrom;
	int offerTo;
	@Transient
	String userNameFrom;
	@Transient
	String userNameTo;
	@Transient
	String sellerNameFrom;
	@Transient
	String sellerNameTo;
	String updateTime;
	String status;

	public int getRid() {
		return Rid;
	}

	public void setRid(int rid) {
		Rid = rid;
	}

	public int getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(int userFrom) {
		this.userFrom = userFrom;
	}

	public int getUserTo() {
		return userTo;
	}

	public void setUserTo(int userTo) {
		this.userTo = userTo;
	}

	public int getSellerFrom() {
		return sellerFrom;
	}

	public void setSellerFrom(int sellerFrom) {
		this.sellerFrom = sellerFrom;
	}

	public int getSellerTo() {
		return sellerTo;
	}

	public void setSellerTo(int sellerTo) {
		this.sellerTo = sellerTo;
	}

	public int getPointsFrom() {
		return pointsFrom;
	}

	public void setPointsFrom(int pointsFrom) {
		this.pointsFrom = pointsFrom;
	}

	public int getPointsTo() {
		return pointsTo;
	}

	public void setPointsTo(int pointsTo) {
		this.pointsTo = pointsTo;
	}

	public int getOfferFrom() {
		return offerFrom;
	}

	public void setOfferFrom(int offerFrom) {
		this.offerFrom = offerFrom;
	}

	public int getOfferTo() {
		return offerTo;
	}

	public void setOfferTo(int offerTo) {
		this.offerTo = offerTo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserNameFrom() {
		return userNameFrom;
	}

	public void setUserNameFrom(String userNameFrom) {
		this.userNameFrom = userNameFrom;
	}

	public String getUserNameTo() {
		return userNameTo;
	}

	public void setUserNameTo(String userNameTo) {
		this.userNameTo = userNameTo;
	}

	public String getSellerNameFrom() {
		return sellerNameFrom;
	}

	public void setSellerNameFrom(String sellerNameFrom) {
		this.sellerNameFrom = sellerNameFrom;
	}

	public String getSellerNameTo() {
		return sellerNameTo;
	}

	public void setSellerNameTo(String sellerNameTo) {
		this.sellerNameTo = sellerNameTo;
	}

}
