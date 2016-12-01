package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import com.alibaba.fastjson.parser.ParseContext;

import model.Notification;
import model.Request;
import model.User;
import utils.JdbcUtils_C3P0;
import dao.UserDao;



public class UserDaoImpl implements UserDao{

	@Override
	/*
	 * @return "ok" for ok,
	 * @return "error" for error
	 * */
	public String add(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		
		String sql = "insert into user(u_wechat_id,u_name,u_email_address,u_pw_hash,u_full_name,u_token)"
				+ "values(?,?,?,?,?,?)";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			//ps.setInt(1, user.getId());
			ps.setString(1, user.getWechatid());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getFullname());
			ps.setString(6, user.getToken());
			ps.executeUpdate();
			return "ok";

		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
			//throw new SQLException("用户注册失败");
			
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}

	@Override
	public void delete(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
        String sql = "delete from user where id=?";
        try {
        	conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("用户注销失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}

	@Override
	public void update(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update user set u_wechat_id=?,u_name=?,u_email_address=?,u_full_name=? where u_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getWechatid());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getFullname());
			ps.setInt(5, user.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("修改用户信息失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}

	@Override
	public User findById(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		String sql = "select u_wechat_id,u_name,u_email_address,u_full_name,u_token from user where u_id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
			    user = new User();
				user.setId(id);
				user.setWechatid(rs.getString(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				//user.setPassword(rs.getString(4));
				user.setFullname(rs.getString(4));
				user.setToken(rs.getString(5));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("获取用户信息失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return user;
	}
	
	//zhuyifan
	public User findByName(String name) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		String sql = "select u_id,u_wechat_id,u_name,u_email_address,u_full_name,u_token from user where u_name=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while(rs.next()){
			    user = new User();
				user.setId(rs.getInt(1));
				user.setWechatid(rs.getString(2));
				user.setName(rs.getString(3));
				user.setEmail(rs.getString(4));
//				user.setPassword(rs.getString(4));
				user.setFullname(rs.getString(5));
				user.setToken(rs.getString(6));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("获取用户信息失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return user;
	}

	@Override
	public ArrayList<User> findAll() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<User> users = new ArrayList<User>();
		String sql = "select u_id,u_wechat_id,u_name,u_email_address,u_pw_hash,u_full_name,u_token from user";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
			    User user = new User();
				user.setId(rs.getInt(1));
				user.setWechatid(rs.getString(2));
				user.setName(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setFullname(rs.getString(6));
				user.setToken(rs.getString(7));
				users.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("获取全部用户失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return users;
	}

	@Override
	public void updatepassword(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update user set u_pw_hash=? where id=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getPassword());
			ps.setInt(2, user.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("修改用户密码失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		
	}

	@Override
	public int checklogin(String username, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		String sql = "select u_id from user where u_name='"+username+"' and u_pw_hash='"+password+"'";
		try {
			System.out.println(sql);
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			//ps.setString(1, username);
			//ps.setString(2, password);
			rs = ps.executeQuery();
			if(rs.next()){
				id = rs.getInt(1);
				//BigDecimal lastIdBd = rs.getBigDecimal(1);
				//id = lastIdBd.intValue();
			}else{
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("登录失败");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
		return id;
	}
	
	//zhuyfian 
	@Override
	public boolean token_auth(int u_id,String u_token) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = -1;
		String sql = "select u_id from user where u_id=? and u_token=?";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			ps.setString(2, u_token);
			rs = ps.executeQuery();
			if(rs.next()){
			BigDecimal lastIdBd = rs.getBigDecimal(1);
		    id = lastIdBd.intValue();}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("验证错误");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
		if(id != -1){
			return true;
		}
		return false;
	}
	
	//zhuyifan
	@Override
	public ArrayList<Request> findRequests(int uId, String condition) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT r_id,U_ID_FROM,U_ID_TO,request.POINTS_FROM,request.POINTS_TO,request.STATUS,request.UPDATE_TIME, "
					+"a.u_name AS userNameFrom,b.u_name AS userNameTo, "
					+"sa.Seller_Name AS sellerNameFrom,sb.Seller_Name AS sellerNameTo , request.offer_from, request.offer_to FROM `request` " 
					+"LEFT JOIN user a ON request.U_ID_FROM = a.u_id "
					+"LEFT JOIN user b on request.U_ID_TO = b.u_id "
					+"LEFT JOIN seller sa on request.SELLER_ID_FROM = sa.Seller_id "
					+"LEFT JOIN seller sb on request.SELLER_ID_TO = sb.Seller_id "
					+"WHERE request.U_ID_FROM = ? OR request.U_ID_TO = ?;";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, uId);
			ps.setInt(2, uId);
			System.out.println(sql);
			ArrayList<Request> result = new ArrayList<Request>();
			rs = ps.executeQuery();
			while(rs.next()){
				Request tempOne = new Request();
				tempOne.setRid(rs.getInt(1));
				tempOne.setUserFrom(rs.getInt(2));
				tempOne.setUserTo(rs.getInt(3));
				tempOne.setPointsFrom(rs.getInt(4));
				tempOne.setPointsTo(rs.getInt(5));
				tempOne.setStatus(rs.getString(6));
				tempOne.setUpdateTime(rs.getString(7));
				tempOne.setUserNameFrom(rs.getString(8));
				tempOne.setUserNameTo(rs.getString(9));
				tempOne.setSellerNameFrom(rs.getString(10));
				tempOne.setSellerNameTo(rs.getString(11));
				tempOne.setOfferFrom(rs.getInt(12));
				tempOne.setOfferTo(rs.getInt(13));
				result.add(tempOne);
		    }
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("故障");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
	}
	
	//zhuyifan
		//@Override
		public ArrayList<Request> findRequests() throws SQLException{
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql = "SELECT r_id,U_ID_FROM,U_ID_TO,request.POINTS_FROM,request.POINTS_TO,request.STATUS,request.UPDATE_TIME, "
						+"a.u_name AS userNameFrom,b.u_name AS userNameTo, "
						+"sa.Seller_Name AS sellerNameFrom,sb.Seller_Name AS sellerNameTo , request.offer_from, request.offer_to FROM `request` " 
						+"LEFT JOIN user a ON request.U_ID_FROM = a.u_id "
						+"LEFT JOIN user b on request.U_ID_TO = b.u_id "
						+"LEFT JOIN seller sa on request.SELLER_ID_FROM = sa.Seller_id "
						+"LEFT JOIN seller sb on request.SELLER_ID_TO = sb.Seller_id "
						+"WHERE 1 ORDER BY request.UPDATE_TIME LIMIT 20;";
			try {
				conn = JdbcUtils_C3P0.getConnection();
				ps = conn.prepareStatement(sql);
				System.out.println(sql);
				ArrayList<Request> result = new ArrayList<Request>();
				rs = ps.executeQuery();
				while(rs.next()){
					Request tempOne = new Request();
					tempOne.setRid(rs.getInt(1));
					tempOne.setUserFrom(rs.getInt(2));
					tempOne.setUserTo(rs.getInt(3));
					tempOne.setPointsFrom(rs.getInt(4));
					tempOne.setPointsTo(rs.getInt(5));
					tempOne.setStatus(rs.getString(6));
					tempOne.setUpdateTime(rs.getString(7));
					tempOne.setUserNameFrom(rs.getString(8));
					tempOne.setUserNameTo(rs.getString(9));
					tempOne.setSellerNameFrom(rs.getString(10));
					tempOne.setSellerNameTo(rs.getString(11));
					tempOne.setOfferFrom(rs.getInt(12));
					tempOne.setOfferTo(rs.getInt(13));
					result.add(tempOne);
			    }
				return result;

			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("故障");
			} finally {
				JdbcUtils_C3P0.release(conn, ps, rs);
			}
		}
	
	@Override
	//zhuyifan
	public ArrayList<Notification> findNotifications(int uId, String condition) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `Notification` " 
					+"WHERE USER_ID = ? ;";
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, uId);
			System.out.println(sql);
			ArrayList<Notification> result = new ArrayList<Notification>();
			rs = ps.executeQuery();
			while(rs.next()){
				Notification tempOne = new Notification();
				tempOne.setNotifiId(rs.getInt(1));
				tempOne.setUserId(rs.getInt(2));
				tempOne.setContent(rs.getString(3));
				tempOne.setStatus(rs.getInt(4));
				tempOne.setSeen(rs.getInt(5));
				tempOne.setExchId(rs.getInt(6));
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");  
				//String str=sdf.format(date);  
				tempOne.setNotiDate(sdf.format(rs.getDate(6)));
				result.add(tempOne);
		    }
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("故障");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
	}
	
	@Override
	//zhuyifan
	public String setNotifications(int nId,String seen) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "UPDATE `Notification` SET `SEEN` = '"+ seen +"' WHERE `Notification`.`NOTIFI_ID` = ?;" ;
		System.out.println(sql);
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, nId);
			
			ps.executeUpdate();
			return "OK";

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("故障");
		} finally {
			JdbcUtils_C3P0.release(conn, ps, rs);
		}
	}
	
	public User get_profile(String id, String token)throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		String sql = "select u_wechat_id,u_name,u_email_address,u_full_name,u_token,u_id from user where u_id=? and u_token = ?";
		
		if(token == ""){
			sql = "select u_wechat_id,u_name,u_email_address,u_full_name,u_token,u_id from user where u_id=?";
		}
		
		try {
			conn = JdbcUtils_C3P0.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			if (token != "") {
				ps.setString(2, token);
			}
			
			rs = ps.executeQuery();
			while(rs.next()){
			    user = new User();
				user.setId(rs.getInt(6));
				user.setWechatid(rs.getString(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				//user.setPassword(rs.getString(4));
				user.setFullname(rs.getString(4));
				if (token != "") {
					user.setToken(rs.getString(5));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtils_C3P0.release(conn, ps, null);
		}
		return user;
	}
}
