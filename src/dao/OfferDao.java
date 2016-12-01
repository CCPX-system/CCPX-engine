package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Offer;

public interface OfferDao {
	public ArrayList<Offer> getExchangeOffers(int seller_from,int seller_to,int points_from,int points_to_min)  throws SQLException;
	public ArrayList<Offer> getExchangeOffers(int seller_from,int seller_to)  throws SQLException;
	public String making_an_offer(Offer offer) throws SQLException;
	public Offer getOfferByID(int offer_id) throws SQLException;
	public boolean setStatus(int offer_id,String status) throws SQLException;
	public String cancelOffers(int offer_id, int user_id) throws SQLException;
	public boolean offerFinished(int offer_id,int points_from,int points_to) throws SQLException;
	
}