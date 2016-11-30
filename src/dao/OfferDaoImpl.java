package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Offer;
import model.Record;
import utils.JdbcUtils_C3P0;

public class OfferDaoImpl implements OfferDao{

	@Override
	public ArrayList<Offer> getExchangeOffers( int seller_from, int seller_to,int points_from, int points_to_min)
			throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Offer> list=new ArrayList<Offer>();

		String sql = "select  * from Offer where seller_from=? and seller_to=? and points_from>=? and points_to_min<=? and status=? ";
		//String sql = "select  * from Offer ";
		
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,seller_to);
			ps.setInt(2,seller_from);
			ps.setInt(3,points_to_min);
			ps.setInt(4, points_from);
			ps.setString(5, "1");
			rs = ps.executeQuery();
			while(rs.next()){
				//System.out.println("1查询开始");
				Offer offer=new Offer();
				offer.setOffer_id( rs.getInt(1));
				offer.setUser_id(rs.getInt(2));
				offer.setSeller_from(rs.getInt(3));
				offer.setSeller_to(rs.getInt(4));
				offer.setPoints_from(rs.getInt(5));
				offer.setPoints_to_min(rs.getInt(6));
				offer.setStatus(rs.getString(7));
				list.add(offer);
			}
//			for(Offer offer2:list){
//				System.out.println(offer2.getOffer_id());
//			}
			//System.out.println("1查询完成");
						
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("获取交易请求信息失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return (ArrayList<Offer>) list;
		
	}

	@Override
		public ArrayList<Offer> getExchangeOffers( int seller_from, int seller_to)
				throws SQLException {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			List<Offer> list=new ArrayList<Offer>();
			String sql = "select  * from Offer where seller_from=? and seller_to=? and status=?";
			System.out.println(seller_from);
			System.out.println(seller_to);
			try {
				conn = JdbcUtils_C3P0.getConnection();
				ps = conn.prepareStatement(sql);
				ps.setInt(1,seller_to);
				ps.setInt(2,seller_from);
				ps.setString(3, "avaliable");
				rs = ps.executeQuery();
				while(rs.next()){
					System.out.println("2查询开始");
					Offer offer=new Offer();
					offer.setOffer_id( rs.getInt(1));
					offer.setUser_id(rs.getInt(2));
					offer.setSeller_from(rs.getInt(3));
					offer.setSeller_to(rs.getInt(4));
					offer.setPoints_from(rs.getInt(5));
					offer.setPoints_to_min(rs.getInt(6));
					offer.setStatus(rs.getString(7));
					list.add(offer);
				}
				for(Offer offer2:list){
					System.out.println(offer2.getOffer_id());
				}
				System.out.println("2查询完成");
							
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("获取交易请求信息失败");
			} finally {
				JdbcUtils_C3P0.release(conn, ps, null);
			}
			//System.out.println(list.size());
			return (ArrayList<Offer>) list;
			
		}
	public String making_an_offer(Offer offer) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		
		String sql = "insert into Offer(user_id,seller_from, seller_to, points_from, points_to_min,status)"
				+ " values(?,?,?,?,?,?)";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			//ps.setInt(1, user.getId());
			ps.setInt(1, offer.getUser_id());
			ps.setInt(2, offer.getSeller_from());
			ps.setInt(3, offer.getSeller_to());
			ps.setInt(4, offer.getPoints_from());
			ps.setInt(5, offer.getPoints_to_min());
			ps.setString(6, offer.getStatus());
			ps.executeUpdate();
			return "ok";

		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
			//throw new SQLException("增加请求失败");
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}

	@Override
	public String cancelOffers(int offer_id, int user_id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update Offer set status=? where offer_id=? and user_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "removed");
			ps.setInt(2, offer_id);
			ps.setInt(3, user_id);
			ps.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
	}
}


