package service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.PlatformDao;
import model.Notification;
import model.Offer;
import model.Request;
import model.user_to_seller;

@Service("PlatformServiceImp")
public class PlatformServiceImp implements PlatformService {

	@Resource(name = "PlatformDaoImp")
	private PlatformDao PlatformDaoImp;

	@Override
	public Boolean removeExchange(Integer request_id, Integer user_from) {
		Boolean flag = PlatformDaoImp.removeExchange(request_id, user_from);
		Boolean flag1 = PlatformDaoImp.createNotification(user_from, 4, request_id);
		System.out.println("flag: " + flag + "flag1:" + flag1);
		return flag;
	}

	@Override
	public Boolean declineExchange(Integer request_id, Integer user_to, Integer user_from) {
		Boolean flag = PlatformDaoImp.declineExchange(request_id, user_to);
		Boolean flag1 = PlatformDaoImp.createNotification(user_to, 7, request_id);
		Boolean flag2 = PlatformDaoImp.createNotification(user_from, 5, request_id);
		System.out.println("flag: " + flag + "flag1: " + flag1 + "flag2: " + flag2);
		return flag;
	}

	@Override
	public Boolean removeOffer(Integer offer_id, Integer user_id) {
		Boolean flag = PlatformDaoImp.removeOffer(offer_id, user_id);
		Boolean flag1 = PlatformDaoImp.createNotification(user_id, 3, offer_id);
		List<Integer> requestList = PlatformDaoImp.listOfRequest(null, offer_id);
		for (int i = 0; i < requestList.size(); i++) {
			Integer r1 = requestList.get(i);
			Boolean decline = PlatformDaoImp.declineRequests(r1);
			Integer userFrom1 = PlatformDaoImp.selectUserFrom(r1);
			Boolean createNotification = PlatformDaoImp.createNotification(userFrom1, 5, r1);
		}
		System.out.println("flag: " + flag);
		return flag;
	}

	@Override
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerTo) {
		List<Request> requests = PlatformDaoImp.showLatestTransaction(sellerFrom, sellerTo);
		return requests;
	}

	@Override
	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin) {
		List<Offer> offers = PlatformDaoImp.searchExcahnge(sellerFrom, sellerTo, pointsFrom, pointsToMin);
		return offers;
	}

	@Override
	public List<Offer> showRecommendationList(Integer sellerFrom, Integer sellerTo, Integer pointsFrom,
			Integer pointsToMin) {
		List<Offer> offers = PlatformDaoImp.showRecommendationList(sellerFrom, sellerTo, pointsFrom, pointsToMin);
		return offers;
	}

	@Override
	public Boolean updateOfferStatus(Integer offer_id, Integer user_from) {
		Boolean flag = PlatformDaoImp.updateOfferStatus(offer_id);
		System.out.println("flag: " + flag);
		return flag;
	}

	@Override
	public Boolean updateRequestStatus(Integer request_id, Integer user_from) {
		Boolean flag = PlatformDaoImp.updateRequestStatus(request_id);
		System.out.println("flag: " + flag);
		return flag;

	}

	@Override
	public Boolean acceptRequest(Integer request_id) {
		System.out.println("enter accept request in serviceImp");
		Request request = new Request();
		request = PlatformDaoImp.requestData(request_id);
		Integer UserFrom = request.getUserFrom();
		Integer UserTo = request.getUserTo();
		Integer PointsFrom = request.getPointsFrom();
		Integer PointsTo = request.getPointsTo();
		Integer SellerFrom = request.getSellerFrom();
		Integer SellerTo = request.getSellerTo();
		Integer OfferFrom = request.getOfferFrom();
		Integer OfferTo = request.getOfferTo();
		System.out.println("Succes read request data" + request.getOfferFrom() + " " + request.getOfferTo());
		// Boolean success = PlatformDaoImp.sendExchangeToBlockChain(request_id,
		// UserFrom, UserTo, SellerFrom,
		// SellerTo, PointsFrom, PointsTo);
		// // call notification notifi(OfferFrom) notifi(OfferTo)
		// notifi(UserFrom)
		// notifi(UserTo)
		// System.out.println("Succes send to BC"+success);
		Boolean acc = PlatformDaoImp.acceptRequest(request_id, OfferFrom, OfferTo);
		List<user_to_seller> list_points = new ArrayList<user_to_seller>();
		list_points = PlatformDaoImp.selectPoints1(UserFrom, UserTo, SellerFrom, SellerTo);
		user_to_seller userFromPoint = new user_to_seller();
		user_to_seller userToPoint = new user_to_seller();
		if (list_points.get(0).getu_id() == UserFrom) {
			userFromPoint = list_points.get(0);
			userToPoint = list_points.get(1);
		} else {
			userFromPoint = list_points.get(1);
			userToPoint = list_points.get(0);
		}
		Integer uFromBlock = userFromPoint.getpoints_blocked();
		Integer uToBlock = userToPoint.getpoints_blocked();

		List<Integer> list_points_to_change = new ArrayList<Integer>();
		list_points_to_change = PlatformDaoImp.selectPoints2(UserFrom, UserTo, SellerFrom, SellerTo);
		Integer from_points_to_change;
		Integer to_points_to_change;
		System.out.println("uFROMBLOCK" + uFromBlock);
		System.out.println("uTOBLOCK" + uToBlock);
		from_points_to_change = list_points_to_change.get(0);
		to_points_to_change = list_points_to_change.get(1);

		System.out.println("FROM POINTS TO CHANGE: " + from_points_to_change);
		System.out.println("TO POINTS TO CHANGE: " + to_points_to_change);
		Integer uFromNewPoint = from_points_to_change + uToBlock;
		Integer uToNewPoint = to_points_to_change + uFromBlock;

		Boolean change = PlatformDaoImp.updatePoints(UserFrom, UserTo, SellerFrom, SellerTo, uFromNewPoint,
				uToNewPoint);
		System.out.println("Succes accept request");
		List<Integer> requestList = PlatformDaoImp.listOfRequest(OfferFrom, OfferTo);
		System.out.println("Succes read request list" + requestList);
		requestList.remove(request_id);
		for (int i = 0; i < requestList.size(); i++) {
			Integer r1 = requestList.get(i);
			Boolean decline = PlatformDaoImp.declineRequests(r1);
			Integer userFrom1 = PlatformDaoImp.selectUserFrom(r1);
			Boolean createNotification = PlatformDaoImp.createNotification(userFrom1, 5, r1);
		}
		Boolean succesToUserFrom = PlatformDaoImp.createNotification(UserFrom, 2, request_id);
		Boolean succesToUserTo = PlatformDaoImp.createNotification(UserTo, 2, request_id);

		return null;

	}

	@Override
	public Boolean createNotification(Integer userId, Integer status, Integer eR_ID) {
		if (PlatformDaoImp.createNotification(userId, status, eR_ID))
			return true;
		else
			return false;
	}

	// NOTIFACTION TESTPAGE
	@Override
	public List<Notification> NotifListsByUserId(Integer userId) {
		return PlatformDaoImp.NotifListsByUserId(userId);
	}

	@Override
	public int getNotifUnread(Integer userId) {
		return PlatformDaoImp.getNotifUnRead(userId);
	}

	@Override
	public List<Request> showUserReceivedRequest(Integer user_id, String status) {
		return PlatformDaoImp.showUserReceivedRequest(user_id, status);
	}

	@Override
	public List<Request> showUserSentRequest(Integer user_id, String status) {
		return PlatformDaoImp.showUserSentRequest(user_id, status);

	}

	@Override
	public List<Offer> showUserOfferList(Integer user_id, String status) {
		return PlatformDaoImp.showUserOfferList(user_id, status);
	}

	@Override
	public boolean setNotifUnread(Integer notifID) {
		return PlatformDaoImp.setNotifUnread(notifID);
	}

}
