package dao;

import java.util.List;

import model.Notification;
import model.Offer;
import model.Request;
public interface PlatformDao {


	public Boolean removeExchange(Integer request_id, Integer user_from);
	public Boolean declineExchange(Integer request_id, Integer user_to);
	public Boolean removeOffer(Integer offer_id, Integer user_id);
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerto);  
	public Boolean acceptRequest(Integer request_id, Integer OfferFrom, Integer OfferTo);
	
 	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);
 	
 	public List<Offer> showRecommendationList(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);
 	
 	public Boolean updateOfferStatus(Integer offer_id, Integer user_from);
 	public Boolean updateRequestStatus(Integer request_id, Integer user_from); 
 	
 	public Request requestData (Integer request_id); 
 	public List<Integer> listOfRequest(Integer OfferFrom, Integer OfferTo);
	
 	
 	
 	public boolean createNotification(Integer userId, Integer status, Integer eR_ID);
 	//NOTIFACTION TESTPAGE
 	public List<Notification> NotifListsByUserId(Integer userId);
	public List<Notification> getNotifUnRead(Integer userId); 

}
