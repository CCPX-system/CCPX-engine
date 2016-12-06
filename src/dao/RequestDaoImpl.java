package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;


import org.springframework.data.redis.connection.srp.SrpConnection;

import model.Offer;
import model.Request;
import model.user_to_seller;
import utils.JdbcUtils_C3P0;

public class RequestDaoImpl implements RequestDao{


	@Override
	public Request makeRequestByOfferId(int offer_from, int offer_to) throws SQLException {
		OfferDaoImpl offerDaoImpl=new OfferDaoImpl();
		Offer offer1=offerDaoImpl.getOfferByID(offer_from);
		Offer offer2=offerDaoImpl.getOfferByID(offer_to);
		if (!offer2.getStatus().equals("OPEN")) {
			if (offer1.getStatus() != null&&!offer1.getStatus().equals("OPEN")) {
				return null;
			}
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		//更新offer的status 正常则执行后面的，否则直接返回
		String sqlString="update Offer set status='CLOSED' where (offer_id=? or offer_id=?)  and status='OPEN'";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setInt(1,offer_from);
			ps.setInt(2, offer_to);
			ps.executeUpdate();	
			//ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
		Date date = new Date();  
		Timestamp time = new Timestamp(date.getTime()); 
		System.out.println(time.toString().substring(0,19));
		String sql = "insert  into request(u_id_from,u_id_to,seller_id_from,seller_id_to,points_from,points_to,offer_from,offer_to,status,update_time) "
				+ "values(?,?,?,?,?,?,?,?,?,'"+time.toString().substring(0,19)+"')";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,offer1.getUser_id());
			ps.setInt(2,offer2.getUser_id());
			ps.setInt(3,offer1.getSeller_from());
			ps.setInt(4, offer1.getSeller_to());
			ps.setInt(5, offer2.getPoints_to_min());
			ps.setInt(6, offer2.getPoints_from());
			ps.setInt(7, offer1.getOffer_id());
			ps.setInt(8, offer2.getOffer_id());
			ps.setString(9, "PENDING");//设置状态值
			ps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		//System.out.println(list.size());
		Request request=getRequestByOfferId(offer_from, offer_to);
		return request;
	}

	@Override
	public Request makeRequestByUserId(int user_from, int offer_to) throws SQLException {
		OfferDaoImpl offerDaoImpl=new OfferDaoImpl();
		Offer offer1=offerDaoImpl.getOfferByID(offer_to);
		
		if (!offer1.getStatus().equals("OPEN")) {
			return null;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		//更新offer的status 正常则执行后面的，否则直接返回
		String sqlString="update Offer set status='CLOSED' where offer_id=?  and status='OPEN'";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setInt(1,offer_to);
			ps.executeUpdate();	
			//ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		Date date = new Date();  
		Timestamp time = new Timestamp(date.getTime()); 
		System.out.println(time.toString().substring(0,19));
		String sql = "insert  into request(u_id_from,u_id_to,seller_id_from,seller_id_to,points_from,points_to,offer_from,offer_to,status,update_time) "
				+ "values(?,?,?,?,?,?,'0',?,?,'"+time.toString().substring(0,19)+"')";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,user_from);
			ps.setInt(2,offer1.getUser_id());
			ps.setInt(3,offer1.getSeller_to());
			ps.setInt(4, offer1.getSeller_from());
			ps.setInt(5, offer1.getPoints_to_min());
			ps.setInt(6, offer1.getPoints_from());
			ps.setInt(7, offer1.getOffer_id());
			ps.setString(8, "PENDING");//设置状态值
			ps.executeUpdate();				
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		if (!new User_to_SellerDaoImpl().addPoints(user_from, offer1.getSeller_to(), 0-offer1.getPoints_to_min())) 
			return null;
		if(!new User_to_SellerDaoImpl().substractLockedPoints(user_from, offer1.getSeller_to(), 0-offer1.getPoints_to_min())) 
			return null;
		Request request=getRequestByOfferId(0, offer_to);
		return request;
	}

	@Override
	public ArrayList<Request> getReferenceRecords(int seller_id_from, int seller_id_to) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Request> list=new ArrayList<Request>();
		
		//通过卖家获取信息的对称性
		String sql = "select  * from request where ((seller_id_from=? and seller_id_to=?) or (seller_id_from=? and seller_id_to=?))"
				+ " and status=? order by update_time desc limit 10";
		
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,seller_id_from);
			ps.setInt(2, seller_id_to);
			ps.setInt(3,seller_id_to);
			ps.setInt(4, seller_id_from);
			ps.setString(5, "CLOSED");//设置成功的状态值
			rs = ps.executeQuery();
			while(rs.next()&&list.size()<10){
				Request request = new Request();
				request.setRid( rs.getInt(1));
				request.setUserFrom(rs.getInt(2));
				request.setUserTo(rs.getInt(3));
				request.setSellerFrom(rs.getInt(4));
				request.setSellerTo(rs.getInt(5));
				request.setPointsFrom(rs.getInt(6));
				request.setPointsTo(rs.getInt(7));
				request.setOfferFrom(rs.getInt(8));
				request.setOfferTo(rs.getInt(9));
				request.setStatus(rs.getString(10));
				request.setUpdateTime(rs.getString(11));
				list.add(request);
			}
//			for(Record record2:list){
//				System.out.println(record2.getR_id());
//			}
						
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("获取参考记录信息失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		//System.out.println(list.size());
		return (ArrayList<Request>) list;
		
	}

	

	@Override
	public boolean rejectRequest(int r_id, int user_to) throws SQLException {
		User_to_SellerDaoImpl user_to_SellerDaoImpl=new User_to_SellerDaoImpl();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="update request set status='DECLINE' where r_id=? and u_id_to=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, r_id);	
			ps.setInt(2, user_to);	
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}

		Request request =getRequestById(r_id);
		
		if (request.getOfferFrom()==0) {
			if (user_to_SellerDaoImpl.addPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom())) 
				if (user_to_SellerDaoImpl.substractLockedPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom()))
					 if(new OfferDaoImpl().setStatus(request.getOfferTo(), "OPEN"))
			return true;
		}
		else{
			if(new OfferDaoImpl().setStatus(request.getOfferFrom(), "OPEN"))
				if(new OfferDaoImpl().setStatus(request.getOfferTo(), "OPEN"))
			      return true;
		}
		
		return false;
	}
	
	public boolean removeRequest(int r_id,int u_id, int s_id, int points) throws SQLException {
		User_to_SellerDaoImpl user_to_SellerDaoImpl=new User_to_SellerDaoImpl();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql1="update request set status='REMOVED' where r_id=? ;";
		String sql2="update user_to_seller set POINTS = POINTS + ?,POINTS_BLOCKED =  POINTS_BLOCKED - ? where U_ID=? and SELLER_ID = ?;";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, r_id);	
			ps.executeUpdate();
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, points);
			ps2.setInt(2, points);
			ps2.setInt(3, u_id);
			ps2.setInt(4, s_id);
			ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}

		Request request =getRequestById(r_id);
		
		if (request.getOfferFrom()==0) {
			if (user_to_SellerDaoImpl.addPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom())) 
				if (user_to_SellerDaoImpl.substractLockedPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom()))
					 if(new OfferDaoImpl().setStatus(request.getOfferTo(), "OPEN"))
			return true;
		}
		else{
			if(new OfferDaoImpl().setStatus(request.getOfferFrom(), "OPEN"))
				if(new OfferDaoImpl().setStatus(request.getOfferTo(), "OPEN"))
			      return true;
		}
		
		return false;
	}

	@Override
	public boolean acceptRequest(int r_id, int user_to) throws SQLException {
		User_to_SellerDaoImpl user_to_SellerDaoImpl=new User_to_SellerDaoImpl();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Request request=new RequestDaoImpl().getRequestById(r_id);
		if (!request.getStatus().equals("PENDING")||request.getUserTo()!=user_to) {
			return false;
		}
		String sql="update request set status='CLOSED' where r_id=? and u_id_to=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, r_id);	
			ps.setInt(2, user_to);	
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
	    
		if (request.getOfferFrom()==0) {
		 if (user_to_SellerDaoImpl.substractLockedPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom())) 
			if (user_to_SellerDaoImpl.addPoints(request.getUserFrom(), request.getSellerFrom(), request.getPointsFrom())) 
				if (new OfferDaoImpl().offerFinished(request.getOfferTo(), request.getPointsTo(), request.getPointsFrom())) 
			return true;
		}
		else{	
			if (new OfferDaoImpl().offerFinished(request.getOfferFrom(), request.getPointsFrom(), request.getPointsTo())) 
			if (new OfferDaoImpl().offerFinished(request.getOfferTo(), request.getPointsTo(), request.getPointsFrom())) 
				return true;
		}

		return false;
	}

	@Override
	public Request getRequestById(int r_id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql1="select * from request where r_id=? ";
		Request request=new Request();
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, r_id);
			rs=ps.executeQuery();
			while (rs.next()) {
				request.setRid(rs.getInt(1));
				request.setUserFrom(rs.getInt(2));
				request.setUserTo(rs.getInt(3));
				request.setSellerFrom(rs.getInt(4));
				request.setSellerTo(rs.getInt(5));
				request.setPointsFrom(rs.getInt(6));
				request.setPointsTo(rs.getInt(7));
				request.setOfferFrom(rs.getInt(8));
				request.setOfferTo(rs.getInt(9));
				request.setStatus(rs.getString(10));
				request.setUpdateTime(rs.getString(11));	
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return request;
	}



	@Override
	public Request getRequestByOfferId(int offer_from, int offer_to) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql1="select * from request where offer_from=? and offer_to=? ";
		Request request=new Request();
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, offer_from);
			ps.setInt(2, offer_to);
			rs=ps.executeQuery();
			while (rs.next()) {
				request.setRid(rs.getInt(1));
				request.setUserFrom(rs.getInt(2));
				request.setUserTo(rs.getInt(3));
				request.setSellerFrom(rs.getInt(4));
				request.setSellerTo(rs.getInt(5));
				request.setPointsFrom(rs.getInt(6));
				request.setPointsTo(rs.getInt(7));
				request.setOfferFrom(rs.getInt(8));
				request.setOfferTo(rs.getInt(9));
				request.setStatus(rs.getString(10));
				request.setUpdateTime(rs.getString(11));	
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return request;
	}
	
}

