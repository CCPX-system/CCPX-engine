package service;

import javax.annotation.Resource;

import model.seller;

import org.springframework.stereotype.Service;

import dao.SellerManagementDao;

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
