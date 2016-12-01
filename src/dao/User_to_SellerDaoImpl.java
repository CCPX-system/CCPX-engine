package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.JdbcUtils_C3P0;
import model.User;
import model.user_to_seller;

public class User_to_SellerDaoImpl implements User_to_SellerDao{

	@Override
	public ArrayList<user_to_seller> queryPoints(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<user_to_seller> points = new ArrayList<>();
		String sql = "select user_to_seller.seller_id,points,points_blocked,seller.Seller_Name from user_to_seller join seller on seller.Seller_id = user_to_seller.SELLER_ID where u_id = ?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
			    user_to_seller uts = new user_to_seller();
			    uts.setseller_id(rs.getInt(1));
			    uts.setpoints(rs.getInt(2));
			    uts.setpoints_blocked(rs.getInt(3));
			    uts.setu_id(id);
			    uts.setSeller_name(rs.getString(4));
			    points.add(uts);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("查询积分失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
		return points;
	}
	@Override
	public boolean lockPoints(int user_id, int seller_id, int points) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int allPoints=0;
		String sql1="select points from user_to_seller where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, user_id);
			ps.setInt(2, seller_id);
			rs=ps.executeQuery();
			while (rs.next()) {
				allPoints=rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		if(allPoints<points) return false;
		
		int allLockedPoints=0;
		String sql2="select points_blocked from user_to_seller where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, user_id);
			ps.setInt(2, seller_id);
			rs=ps.executeQuery();
			while (rs.next()) {
				allLockedPoints=rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
		String sql = "update user_to_seller set points=?,points_blocked=? where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, allPoints-points);
			ps.setInt(2, allLockedPoints+points);
			ps.setInt(3,user_id);
			ps.setInt(4, seller_id);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return true;
	}
	@Override
	public boolean addPoints(int user_id, int seller_id, int points) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int allPoints=0;
		String sql1="select points from user_to_seller where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, user_id);
			ps.setInt(2, seller_id);
			rs=ps.executeQuery();
			while (rs.next()) {
				allPoints=rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		if (allPoints+points<0) return false;
		String sql = "update user_to_seller set points=? where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, allPoints+points);
			ps.setInt(2,user_id);
			ps.setInt(3, seller_id);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return true;
	}
	@Override
	public boolean substractLockedPoints(int user_id, int seller_id, int points) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int allLockedPoints=0;
		String sql1="select points_blocked from user_to_seller where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql1);
			ps.setInt(1, user_id);
			ps.setInt(2, seller_id);
			rs=ps.executeQuery();
			while (rs.next()) {
				allLockedPoints=rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		if (allLockedPoints-points<0) return false;
		String sql = "update user_to_seller set points_blocked=? where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, allLockedPoints-points);
			ps.setInt(2,user_id);
			ps.setInt(3, seller_id);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return true;
	}
	

	//查询某用户在某商家中的当前可用积分
	//@Override
	public int pointsUsable(int u_id, int seller_id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int points = 0;
		int points_blocked =0;
		String sql = "select points,points_blocked from user_to_seller where u_id=? and seller_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			ps.setInt(2, seller_id);
			rs = ps.executeQuery();
			while(rs.next()){
				 points = rs.getInt(1);
				 points_blocked = rs.getInt(2);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("查询可用积分失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
		return points-points_blocked;
	}
	/*
	//当用户发出offer请求时的锁分操作
	@Override
	public void lockPoints(int u_id, int seller_id,int points_from) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update user_to_seller set points_blocked=points_blocked + '"+points_from+"' where u_id =? and seller_id =?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			ps.setInt(2, seller_id);
			ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("更新锁分失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}
	*/
}
