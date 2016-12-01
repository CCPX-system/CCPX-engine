package service;

import java.util.List;

import model.Notification;
import model.Offer;
import model.Request;

public interface PlatformService {
	//Remove Exchange
	public Boolean removeExchange(Integer request_id, Integer user_from);
	//Decline Exchange
	public Boolean declineExchange(Integer request_id, Integer user_to, Integer user_from);
	//Remove Offer
	public Boolean removeOffer(Integer offer_id, Integer user_id);
	//Show Latest transaction list
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerTo);
	//Show user received request by status
	public List<Request> showUserReceivedRequest(Integer user_id, String status);
	//Show user sent request by status 
	public List<Request> showUserSentRequest(Integer user_id, String status);
	//Show offer list by status 
	public List<Offer> showUserOfferList(Integer user_id, String status); 
	//Search exchange 
	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);
	//Show recommendation list
	public List<Offer> showRecommendationList(Integer sellerFrom, Integer sellerTo, Integer pointsFrom,
			Integer pointsToMin);
	//Update offer status
	public Boolean updateOfferStatus(Integer offer_id, Integer user_from);

	public Boolean updateRequestStatus(Integer request_id, Integer user_from);
	//Accept request and all of exchange process 
	public Boolean acceptRequest(Integer request_id);
	
	public Boolean createNotification(Integer userId, Integer status, Integer eR_ID);
	
	
	//NOTIFACTION TESTPAGE
	public List<Notification> NotifListsByUserId(Integer userId);
	public List<Notification> getNotifUnread(Integer userId);

	
}
