package dao;

import java.util.List;

import model.Offer;
import model.Request;
public interface PlatformDao {

	public Boolean removeExchange(String request_id, String user_from);
	public Boolean declineExchange(String request_id, String user_to);
	public Boolean removeOffer(String offer_id, String user_id);
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerto);  
	public Boolean acceptRequest(Integer request_id);
	
 	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);
 	
 	public List<Offer> showRecommendationList(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);
}
