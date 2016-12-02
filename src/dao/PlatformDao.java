package dao;

import java.util.List;

import model.Notification;
import model.Offer;
import model.Request;
import model.user_to_seller;

public interface PlatformDao {

	public Boolean removeExchange(Integer request_id, Integer user_from);

	public Boolean declineExchange(Integer request_id, Integer user_to);

	public Boolean removeOffer(Integer offer_id, Integer user_id);

	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerto);

	public List<Request> showUserReceivedRequest(Integer user_id, String status);

	public List<Request> showUserSentRequest(Integer user_id, String status);

	public List<Offer> showUserOfferList(Integer user_id, String status);

	public Boolean acceptRequest(Integer request_id, Integer OfferFrom, Integer OfferTo);

	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin);

	public List<Offer> showRecommendationList(Integer sellerFrom, Integer sellerTo, Integer pointsFrom,
			Integer pointsToMin);

	public Boolean updateOfferStatus(Integer offer_id);

	public Boolean updateRequestStatus(Integer request_id);

	public Boolean declineRequests(Integer request_id);

	public Request requestData(Integer request_id);

	public List<Integer> listOfRequest(Integer OfferFrom, Integer OfferTo);

	public boolean createNotification(Integer userId, Integer status, Integer eR_ID);

	// NOTIFACTION TESTPAGE
	public List<Notification> NotifListsByUserId(Integer userId);

	public int getNotifUnRead(Integer userId);

	boolean sendExchangeToBlockChain(Integer Request_id, Integer userFrom, Integer userTo, Integer sellerFrom,
			Integer sellerTo, Integer pointFrom, Integer pointTo);

	public Integer selectUserFrom(Integer request_id);

	public boolean setNotifUnread(Integer notifID);

	public Boolean updatePoints(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo,
			Integer pointsFrom, Integer pointsTo);

	public List<user_to_seller> selectPoints1(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo);

	public List<Integer> selectPoints2(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo);

}
