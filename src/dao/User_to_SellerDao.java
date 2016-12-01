package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.user_to_seller;

public interface User_to_SellerDao {

	public ArrayList<user_to_seller> queryPoints(int id) throws SQLException;
	public boolean lockPoints(int user_id,int seller_id,int points) throws SQLException;
	public boolean addPoints(int user_id,int seller_id,int points) throws SQLException;
	public boolean substractLockedPoints(int user_id,int seller_id,int points) throws SQLException;
}
