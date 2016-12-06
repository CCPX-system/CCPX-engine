package dao;

import java.util.List;

import model.industry_type;
import model.seller;

public interface InfoManagementDao {

	public List<industry_type> getIndustryInfo();
	
	public List<seller> getSellerInfoByIndustryID(String id);
	
	public List<seller> getSellerInfoByKeyWord(String keyword);
	
	public List<seller> getSellerInfo();
	
	public seller getCompanyDetail(String id);
	
	public String getIndustryInfoByID(String id);
	
	public List<seller> getAllSellerInfo();

}

