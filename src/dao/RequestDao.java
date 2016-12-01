package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Request;

public interface RequestDao {
	public boolean makeRequestByOfferId(int offer_from,int offer_to) throws SQLException;
	public boolean makeRequestByUserId(int user_from,int offer_to) throws SQLException;
	public ArrayList<Request> getReferenceRecords(int seller_id_from,int seller_id_to)  throws SQLException;
	public Request getRequestById(int r_id) throws SQLException;
	public boolean rejectRequest(int r_id,int user_to) throws SQLException;
	public boolean acceptRequest(int r_id,int user_to) throws SQLException;
}
