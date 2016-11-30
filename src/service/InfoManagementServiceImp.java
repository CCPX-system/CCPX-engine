package service;

import java.util.List;

import javax.annotation.Resource;

import model.industry_type;
import model.seller;

import org.springframework.stereotype.Service;

import dao.InfoManagementDao;

@Service("infoManagementServiceImp")
public class InfoManagementServiceImp implements InfoManagementService {

     
	@Resource(name = "infoManagementDaoImp")
	private InfoManagementDao infoManagementDaoImp;

	@Override
	public List<industry_type> getIndustryInfo() {

		return infoManagementDaoImp.getIndustryInfo();
	}
	@Override
	public List<seller> getSellerInfoByIndustryID(String id){
		return infoManagementDaoImp.getSellerInfoByIndustryID(id);
	}
	
	@Override
	public List<seller> getSellerInfoByKeyWord(String keyword){
		return infoManagementDaoImp.getSellerInfoByKeyWord(keyword);
	}
	
	@Override
	public List<seller> getSellerInfo(){
		return infoManagementDaoImp.getSellerInfo();
	}
	
	@Override
	public seller getCompanyDetail(String id){
		return infoManagementDaoImp.getCompanyDetail(id);
	}
	@Override
	public String getIndustryInfoByID(String id){
		return infoManagementDaoImp.getIndustryInfoByID(id);
	}
}
