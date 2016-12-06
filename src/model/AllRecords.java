package model;

import java.util.ArrayList;

public class AllRecords {
	ArrayList<Request> requests;
	ArrayList<Offer> offers;
	public AllRecords(ArrayList<Request> requests,ArrayList<Offer> offers){
		this.requests = requests;
		this.offers = offers;
	}
	public ArrayList<Request> getRequests() {
		return requests;
	}
	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
	public ArrayList<Offer> getOffers() {
		return offers;
	}
	public void setOffers(ArrayList<Offer> offers) {
		this.offers = offers;
	}

}
