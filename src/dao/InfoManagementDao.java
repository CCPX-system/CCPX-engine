package dao;

import java.util.List;
import model.industry_type;
import model.seller;

public interface InfoManagementDao {

	public List<industry_type> getIndustryInfo();
	
	public List<seller> getSellerInfoByIndustryID(String id);
	
	public List<seller> getSellerInfo();
}
