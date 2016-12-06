package service;

import javax.annotation.Resource;

import model.seller;

import org.springframework.stereotype.Service;

import dao.SellerManagementDao;

import java.util.Date;
import java.text.SimpleDateFormat;


@Service("sellerManagementServiceImp")
public class SellerManagementServiceImp implements SellerManagementService {

     
	@Resource(name = "sellerManagementDaoImp")
	private SellerManagementDao sellerManagementDaoImp;
	
	

	@Override
	public seller checkSeller(String username, String password) {

		return sellerManagementDaoImp.checkSeller(username, password);
	}
	
	@Override
	public seller validateUsername(String username){
		return sellerManagementDaoImp.validateUsername(username);
	}
	
	@Override
	public boolean updateSellerinfo(seller seller) {

		return sellerManagementDaoImp.updateSellerinfo(seller);
	}
	

	@Override
	public boolean regist(seller seller) {
		if(sellerManagementDaoImp.regist(seller)) return true;
		else return false;
	}
	@Override
	public boolean retrievePassword(String uname, String email){
		
		seller Seller = sellerManagementDaoImp.retrievePassword(uname,email);
		if(Seller == null){
			return false;
		}else{
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String time = dateFormat.format( now );
			String to = Seller.getSeller_Email();
			String subject = "Dear Customer:[ "+Seller.getSeller_Name()+" ] Respond to your request from CCPX on "+time;
			String text = "Please check your message in time.\n"+"Your password: "+Seller.getSeller_Password()+"\nRemember your message and change it as soon as possible. You can click the following address to jump to the CCPX home page for comany.\n"
					+ "http://localhost:8080//ccpx/LoginPageSeller.html";
			sellerManagementDaoImp.sentEmails(to, subject, text); 
			return true;
		}
	}

	
	@Override
	public seller getSellerById(int Seller_id) {
		// TODO Auto-generated method stub
		return sellerManagementDaoImp.getSellerById(Seller_id);
	}
	
	@Override
	public boolean checkActivationCode(String code, String sellerid){
		return sellerManagementDaoImp.checkActivationCode(code,sellerid);
	}
	
	@Override
	public boolean updateSellerStatus(String sellerid){
		return sellerManagementDaoImp.updateSellerStatus(sellerid);
	}

}
