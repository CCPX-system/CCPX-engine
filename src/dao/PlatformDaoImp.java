package dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.JdbcUtils_C3P0;
import org.apache.commons.io.IOUtils;

import dao.PlatformDao;

import javax.annotation.Resource;

import model.Notification;
import model.Offer;
import model.Request;
import model.user_to_seller;
import service.NotificationWebSocket;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.annotation.Resource;

import org.hibernate.Query;

@Repository("PlatformDaoImp")
public class PlatformDaoImp implements PlatformDao {
	// 声明sessionFactory

	private SessionFactory sessionFactory;

	private Session session;
	
	private SessionFactory getSessionFactory(){
	    return sessionFactory;
	}

	@Resource(name="sessionFactory")
	private void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
	}

	// 获取当前session的方法
	private Session getSession() {

		if (session == null) {
			session = sessionFactory.getCurrentSession();
		} else {
			session = sessionFactory.getCurrentSession();
		}
		return session;
	}

	// OFFER: -OPEN -CLOSED -REMOVED
	// EXCHANGE: -PENDING -CLOSED -REMOVED -DECLINED

	@Override
	public List<Offer> searchExcahnge(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin) {
		List<Offer> list = new ArrayList<Offer>();
		String sql = "from Offer where pointsFrom>= :pointsToMin and pointsToMin<= :pointsFrom "
				+ "and sellerFrom =:sellerTo and sellerTo =:sellerFrom and status= :status";
		Query query = getSession().createQuery(sql);
		query.setInteger("sellerFrom", sellerFrom);
		query.setInteger("sellerTo", sellerTo);
		query.setInteger("pointsFrom", pointsFrom);
		query.setInteger("pointsToMin", pointsToMin);
		query.setString("status", "OPEN");
		list = query.list();
		return list;
	}

	@Override
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerTo) {

		// TODO Auto-generated method stub
		String sql = "from Request where " + "(sellerFrom =? and sellerTo =? " + "and status = 'CLOSED')"
				+ " or (sellerTo =? and sellerFrom =? " + "and status = 'CLOSED')" + " order by updateTime desc";
		
		
		Query query = getSession().createQuery(sql);
		query.setMaxResults(5);// cann't write limit 5 in sql
		query.setInteger(0, sellerFrom);
		query.setInteger(1, sellerTo);
		query.setInteger(2, sellerFrom);
		query.setInteger(3, sellerTo);
		System.out.println(query.list());
		List<Request> requests = query.list();
		return requests;
	}

	@Override
	public List<Request> showUserReceivedRequest(Integer user_id, String status) {
		List<Request> requests = null;
		String sql = "from Request where userTo=?0 and status=?1 order by updateTime desc";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, user_id);
		query.setString(1, status);
		System.out.println(query.list());
		requests = query.list();
		return requests;
	}

	@Override
	public List<Request> showUserSentRequest(Integer user_id, String status) {
		List<Request> requests = null;
		String sql = "from Request where userFrom=?0 and status=?1 order by updateTime desc";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, user_id);
		query.setString(1, status);
		System.out.println(query.list());
		requests = query.list();
		return requests;
	}

	@Override
	public List<Offer> showUserOfferList(Integer user_id, String status) {
		List<Offer> list = new ArrayList<Offer>();
		String hql = "from Offer where userFrom= :userFrom status= :status";
		Query query = getSession().createQuery(hql);
		query.setInteger("userFrom", user_id);
		query.setString("status", status);
		Offer offer = (Offer) query.uniqueResult();
		list.add(offer);
		return list;
	}

	@Override
	public List<Offer> showRecommendationList(Integer seller_from, Integer seller_to, Integer points_from,
			Integer points_to_min) {
		List<Offer> list = new ArrayList<Offer>();
		String hql = "from Offer where points_from>= :points_to_min and points_to_min<= :points_from "
				+ "and seller_from =:seller_to and seller_to =:seller_from and status= :status order by points_to_min";
		int m = 0;
		int n = 5;
		Query query = getSession().createQuery(hql);
		query.setFirstResult(m); // å¼€å§‹è®°å½•
		query.setMaxResults(n);// æŸ¥è¯¢å¤šå°‘æ ¡
		query.setInteger("seller_from", seller_from);
		query.setInteger("seller_to", seller_to);
		query.setInteger("points_from", points_from);
		query.setInteger("points_to_min", points_to_min);
		query.setString("status", "open");
		Offer offer = new Offer();
		list = query.list();
		return list;
	}

	@Override
	public Boolean removeExchange(Integer request_id, Integer user_from) {
		System.out.println("PlatformDaoImp");
		System.out.println("request_id: " + request_id);
		System.out.println("user_from: " + user_from);

		String hql = "update Request set  status=:status where Rid=:Rid and userFrom=:userFrom";
		Query query = getSession().createQuery(hql);
		query.setInteger("Rid", request_id);
		query.setInteger("userFrom", user_from);
		query.setString("status", "REMOVED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("Request_ID " + request_id + " has been removed! Successfully");
		} else {
			System.out.println("Request_ID " + request_id + " has not been removed! Failed");
		}
		return (a > 0 ? true : false);
	}

	@Override
	public Boolean declineExchange(Integer request_id, Integer user_to) {
		System.out.println("PlatformDaoImp");
		System.out.println("request_id: " + request_id);
		System.out.println("user_to: " + user_to);
		// status 2 = Declined
		String hql = "update Request set  status=:status where Rid=:Rid and userTo=:userTo";
		Query query = getSession().createQuery(hql);
		query.setInteger("Rid", request_id);
		query.setInteger("userTo", user_to);
		query.setString("status", "DECLINED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("Request_ID " + request_id + " has been declined! Successfully");
		} else {
			System.out.println("Request_ID " + request_id + " has not been declined! Failed");
		}
		return (a > 0 ? true : false);
	}

	@Override
	public Boolean removeOffer(Integer offer_id, Integer user_id) {
		System.out.println("PlatformDaoImp");
		System.out.println("offer_id: " + offer_id);
		System.out.println("user_id: " + user_id);
		// status 3= removed status 1=avalaible 2=done

		String hql = "update Offer set  STATUS=:STATUS where OFFER_ID=:OFFER_ID and USER_ID=:USER_ID";
		Query query = getSession().createQuery(hql);
		query.setInteger("OFFER_ID", offer_id);
		query.setInteger("USER_ID", user_id);
		query.setString("STATUS", "REMOVED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("OFFER_ID " + offer_id + " has been removed! Successfully");
		} else {
			System.out.println("OFFER_ID " + offer_id + " has not been removed! Failed");
		}
		return (a > 0 ? true : false);
	}

	@Override
	public Boolean updateOfferStatus(Integer offer_id) {
		String sql = "update Offer set  STATUS=:STATUS where OFFER_ID=:OFFER_ID";
		Query query = getSession().createQuery(sql);
		query.setInteger("OFFER_ID", offer_id);
		query.setString("STATUS", "CLOSED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("OFFER_ID " + offer_id + " has been removed! Successfully");
		} else {
			System.out.println("OFFER_ID " + offer_id + " has not been removed! Failed");
		}
		return (a > 0 ? true : false);
	}

	@Override
	// Update all of related request status
	public Boolean updateRequestStatus(Integer request_id) {
		String sql = "update Request set  status=:status where Rid=:Rid";
		Query query = getSession().createQuery(sql);
		query.setInteger("Rid", request_id);
		query.setString("status", "CLOSED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("Request_ID " + request_id + " has been declined! Successfully");
		} else {
			System.out.println("Request_ID " + request_id + " has not been declined! Failed");
		}
		return (a > 0 ? true : false);
	}

	@Override
	public Boolean acceptRequest(Integer request_id, Integer OfferFrom, Integer OfferTo) {
		// Update 1 request and two offers status
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime = dateFormat.format(new Date());
		String hql = "update Request set status=:status, updateTime=:UPDATE_DATE where Rid=:Rid";
		System.out.println(hql);
		Query query = getSession().createQuery(hql);
		query.setString("status", "CLOSED");
		query.setInteger("Rid", request_id);
		query.setString("UPDATE_DATE", updateTime);
		Boolean rr = null;
		int a = query.executeUpdate();
		int b = 0;
		if (OfferFrom != null) {
			String sql1 = "update Offer set  status=:STATUS, update_time=:UPDATE_DATE where offer_id=:OFFER_ID1";
			Query query1 = getSession().createQuery(sql1);
			query1.setInteger("OFFER_ID1", OfferFrom);
			query1.setString("STATUS", "CLOSED");
			query1.setString("UPDATE_DATE", updateTime);
			b = query1.executeUpdate();
		}
		String sql2 = "update Offer set  status=:STATUS, update_time=:UPDATE_DATE where offer_id=:OFFER_ID2";
		Query query2 = getSession().createQuery(sql2);
		query2.setInteger("OFFER_ID2", OfferTo);
		query2.setString("STATUS", "CLOSED");
		query2.setString("UPDATE_DATE", updateTime);
		int c = query2.executeUpdate();

		if (OfferFrom != null) {
			if (a > 0 & b > 0 & c > 0) {
				System.out.println("request has been accepted");
				rr = true;
			} else {
				System.out.println("request acceptting is failed");
				rr = false;
			}

		} else {
			if (a > 0 & c > 0) {
				System.out.println("request has been accepted");
				rr = true;
			} else {
				System.out.println("request acceptting is failed");
				rr = false;
			}
		}
		return rr;

	}

	@Override
	public Request requestData(Integer request_id) {
		// TODO Auto-generated method stub
		String sql = "from Request where Rid=:Rid";

		Query query = getSession().createQuery(sql);
		query.setInteger("Rid", request_id);
		Request request = (Request) query.uniqueResult();
		return request;

	}

	@Override
	public List<Integer> listOfRequest(Integer OfferFrom, Integer OfferTo) {
		List<Integer> requests;
		// OfferFrom is not null means userFrom found offer from make offer
		// recommendation
		// here we select all of exchange request sent to OfferTo and OfferFrom
		if (OfferFrom != null) {
			String sql = "SELECT Rid from Request where offerTo =:A1 OR offerFrom=:A2 OR offerTo =:A3 OR offerFrom=:A4";
			Query query = getSession().createQuery(sql);
			query.setInteger("A1", OfferFrom);
			query.setInteger("A2", OfferFrom);
			query.setInteger("A3", OfferTo);
			query.setInteger("A4", OfferTo);
			System.out.println(sql);
			System.out.println(OfferFrom);
			System.out.println("OFFERFROM!=NULL " + query.list());
			requests = query.list();

		} else {
			// OfferFrom is null means userFrom found offer from searchExchange
			// here we select all of exchange request sent to OfferTo
			String sql = "SELECT E.Rid from Request E where " + "offerTo =? OR offerFrom=?";
			Query query = getSession().createQuery(sql);
			query.setInteger(0, OfferTo);
			query.setInteger(1, OfferTo);
			System.out.println(query.list());
			requests = query.list();
		}

		return requests;
	}

	@Override
	public Boolean declineRequests(Integer request_id) {
		boolean success;
		String sql = "update Request set status=:status where Rid=:Rid";
		Query query = getSession().createQuery(sql);
		query.setInteger("Rid", request_id);
		query.setString("status", "CLOSED");
		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("Request_ID " + request_id + " has been declined! Successfully");
			success = true;
		} else {
			System.out.println("Request_ID " + request_id + " has not been declined! Failed");
			success = false;
		}
		return success;
	}

	@Override
	public Integer selectUserFrom(Integer request_id) {
		Integer user_from;
		String hql = "select E.userFrom from Request E where Rid=:Rid1";
		System.out.println("HQL:" + hql);
		Query query = getSession().createQuery(hql);
		query.setInteger("Rid1", request_id);
		user_from = (Integer) query.uniqueResult();

		return user_from;

	}

	@Override
	public boolean createNotification(Integer userId, Integer status, Integer eR_ID) {
		String content = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nDateString = dateFormat.format(new Date());

		// EXPLANATION OFFER AND REQUEST
		if (status.equals(0)) { // 0 = OFFER_OPEN
			content = "YOUR EXCHANGE OFFER WAITING FOR RESPOND";
		} else if (status.equals(1) || status.equals(2)) { // 1 = CLOSE_OFFER ;
															// 2 = CLOSE_REQUEST
			content = "YOUR EXCHANGE SUCCESFULLY DONE";
		} else if (status.equals(3)) { // 3 = REMOVE_OFFER
			content = "YOUR EXCHANGE OFFER HAS BEEN REMOVED";
		} else if (status.equals(4)) { // 4 = REMOVE_REQUEST
			content = "YOUR EXCHANGE REQUEST HAS BEEN REMOVED";
		} else if (status.equals(5)) { // 5 = DECLINE_REQUEST
			content = "YOUR EXCHANGE REQUEST HAS BEEN DECLINED";
		} else if (status.equals(6)) {// 6 = REQUEST_PENDING
			content = "YOUR EXCHANGE REQUEST IS PENDING";
		} else if (status.equals(7)) {// 6 = REQUEST_PENDING
			content = "YOUR DECLINED EXCHANGE REQUEST";
		}

		// SET DATA TO NOTIFICATION MODEL
		Notification notif = new Notification();
		notif.setUserId(userId);
		notif.setContent(content);
		notif.setStatus(status);
		notif.setSeen(0);
		notif.setNotiDate(nDateString);
		notif.setExchId(eR_ID);

		// QUERY EXECUTION
		session = getSession();
		session.beginTransaction();
		session.save(notif);
		session.flush();
		session.getTransaction().commit();
		session.clear();
		NotificationWebSocket ws = new NotificationWebSocket();
		ws.broadcast(userId + "|" + eR_ID + "|" + status);
		return true;

	}

	@Override
	public List<Notification> NotifListsByUserId(Integer userId) {
		String hql = "from Notification where USER_ID=:USER_ID order by USER_ID desc";
		Query query = getSession().createQuery(hql);
		query.setInteger("USER_ID", userId);
		List<Notification> notif_list = query.list();
		return notif_list;
	}

	@Override
	public int getNotifUnRead(Integer userId) {
		String hql = "from Notification where USER_ID=:USER_ID and SEEN=:SEEN";
		Query query = getSession().createQuery(hql);
		query.setInteger("USER_ID", userId);
		query.setInteger("SEEN", 0);
		List<Notification> notif_list = query.list();
		return notif_list.size();
	}

	@Override
	public boolean sendExchangeToBlockChain(Integer Request_id, Integer userFrom, Integer userTo, Integer sellerFrom,
			Integer sellerTo, Integer pointFrom, Integer pointTo) {
		// TODO Auto-generated method stub
		// String
		// sr=HttpRequest.sendPost("http://ccpx-blockchain.mybluemix.net/getTxInfo","Request_id=Request_id&user_A=userFrom&user_B=userTo&seller_A=sellerFrom&seller_B=sellerTo&point_A=pointFrom&point_B=pointTo&time=time");
		try {
			// Configure and open a connection to the site you will send the
			// request
			URL url = new URL("http://ccpx-blockchain.mybluemix.net/getTxInfo");
			URLConnection urlConnection = url.openConnection();
			System.out.println("send block Chain after URL");
			// set attribute of doOutput as true,表示将使用此urlConnection写入数据
			urlConnection.setDoOutput(true);
			// 定义待写入数据的内容类型，这里设置为application/x-www-form-urlencoded类型
			urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 得到请求的输出流对象
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			// 把数据写入Blockchain
			out.write(
					"Request_id=Request_id&user_A=userFrom&user_B=userTo&seller_A=sellerFrom&seller_B=sellerTo&point_A=pointFrom&point_B=pointTo");
			out.flush();
			out.close();

			// 从blockchain读取响应
			InputStream inputStream = urlConnection.getInputStream();
			String encoding = urlConnection.getContentEncoding();
			String respond = IOUtils.toString(inputStream, encoding);
			System.out.println(respond);

			JSONObject jsonObject = new JSONObject();
			String response = jsonObject.getString("respond");
			// System.out.println("RESPOND_CODE:"+response);
			if (response.equals('1')) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;

	}

	@Override
	public boolean setNotifUnread(Integer notifID) {
		String hql = "update Notification set SEEN=:SEEN where NOTIFI_ID=:NOTIFI_ID";
		Query query = getSession().createQuery(hql);
		query.setInteger("NOTIFI_ID", notifID);
		query.setInteger("SEEN", 1);

		int a = query.executeUpdate();
		if (a > 0) {
			System.out.println("Request_ID " + notifID + " has been declined! Successfully");
			return true;
		} else {
			System.out.println("Request_ID " + notifID + " has not been declined! Failed");
			return false;
		}

	}

	@Override
	public List<user_to_seller> selectPoints1(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo) {
		List<user_to_seller> list = new ArrayList<user_to_seller>();
		String sql = "from user_to_seller where (u_id =:A1 and seller_id=:A2) OR (u_id =:A3 and seller_id=:A4)";
		Query query = getSession().createQuery(sql);
		query.setInteger("A1", userFrom);
		query.setInteger("A2", sellerFrom);
		query.setInteger("A3", userTo);
		query.setInteger("A4", sellerTo);
		list = query.list();
		return list;

	}

	@Override
	public List<Integer> selectPoints2(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo) {
		List<Integer> list = new ArrayList<Integer>();
		System.out.println("userFrom" + userFrom);
		System.out.println("userTo" + userTo);
		System.out.println("sellerFrom" + sellerFrom);
		System.out.println("sellerTo" + sellerTo);
		String sql1 = "SELECT POINTS from user_to_seller WHERE (u_id =? and seller_id=?) OR (u_id =? and seller_id=?)";
		Query query1 = getSession().createSQLQuery(sql1);
		query1.setInteger(0, userFrom);
		query1.setInteger(1, sellerTo);
		query1.setInteger(2, userTo);
		query1.setInteger(3, sellerFrom);
		list = query1.list();
		System.out.println("LIST: " + list);
		return list;
	}

	@Override
	public Boolean updatePoints(Integer userFrom, Integer userTo, Integer sellerFrom, Integer sellerTo,
			Integer newPointsFrom, Integer newPointsTo) {

		boolean r1 = false;

		String sql2 = "update user_to_seller set  points_blocked=:set1 where u_id=:userFrom1 and seller_id=:sellerFrom1";
		Query query2 = getSession().createQuery(sql2);
		query2.setInteger("set1", 0);
		query2.setInteger("userFrom1", userFrom);
		query2.setInteger("sellerFrom1", sellerFrom);
		int b = query2.executeUpdate();

		String sql3 = "update user_to_seller set  points_blocked=:set1 where u_id=:userTo1 and seller_id=:sellerTo1";
		Query query3 = getSession().createQuery(sql3);
		query3.setInteger("set1", 0);
		query3.setInteger("userTo1", userTo);
		query3.setInteger("sellerTo1", sellerTo);
		int c = query3.executeUpdate();

		String sql4 = "update user_to_seller set  points=:set1 where u_id=:userFrom2 and seller_id=:sellerTo2";
		Query query4 = getSession().createQuery(sql4);
		query4.setInteger("set1", newPointsFrom);
		query4.setInteger("userFrom2", userFrom);
		query4.setInteger("sellerTo2", sellerTo);
		int d = query4.executeUpdate();

		String sql5 = "update user_to_seller set  points=:set1 where u_id=:userFrom2 and seller_id=:sellerTo2";
		Query query5 = getSession().createQuery(sql5);
		query5.setInteger("set1", newPointsTo);
		query5.setInteger("userFrom2", userTo);
		query5.setInteger("sellerTo2", sellerFrom);
		int f = query5.executeUpdate();

		if (b > 0 & c > 0 & d > 0 & f > 0) {
			r1 = true;
			System.out.println("Updated user_to_seller");
		}
		return r1;
	}

}
