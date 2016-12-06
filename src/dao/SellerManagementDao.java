package dao;

import model.seller;

public interface SellerManagementDao {

	public seller checkSeller(String username, String password);
	
	public seller validateUsername(String username);
	
	public boolean updateSellerinfo(seller seller);

	public boolean regist(seller seller);

	public seller getSellerById(int Seller_id);	
	
	public boolean checkActivationCode(String code, String sellerid);
	
	public seller retrievePassword(String uname, String email);
	
	public boolean updateSellerStatus(String sellerid);
	
	public void sentEmails(String email,String subject,String text);
}
