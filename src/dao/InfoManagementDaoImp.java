package dao;

import java.util.List;

import javax.annotation.Resource;

import model.industry_type;
import model.seller;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository("infoManagementDaoImp")
public class InfoManagementDaoImp implements InfoManagementDao {
	// 声明sessionFactory
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	private Session session;
	// 获取当前session的方法
	private Session getSession() {

		if (session == null) {
			session = sessionFactory.openSession();
		} else {
			session = sessionFactory.getCurrentSession();
		}
		return session;
	}

	@Override
	public List<industry_type> getIndustryInfo() {
		String hql = "from industry_type";
		Query query = getSession().createQuery(hql);
		List<industry_type> industry_type_list = query.list();
		return industry_type_list;
	}
	
	@Override
	public List<seller> getSellerInfoByIndustryID(String id){
		String hql = "from seller where IndustryType_id=:IndustryType_id";
		Query query = getSession().createQuery(hql);
		query.setInteger("IndustryType_id", Integer.parseInt(id));
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<seller> seller_list = query.list();
		return seller_list;
	}
	
	@Override
	public List<seller> getSellerInfoByKeyWord(String keyword){
		String hql = "from seller where Seller_Name like '%"+keyword+"%'";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<seller> seller_list = query.list();
		return seller_list;
	}
	
	@Override
	public List<seller> getSellerInfo(){
		String hql = "from seller";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<seller> seller_list = query.list();
		return seller_list;
	}
	
	@Override
	public seller getCompanyDetail(String id){
		String hql = "from seller where Seller_id =:Seller_id";
		Query query = getSession().createQuery(hql);
		query.setString("Seller_id", id);
		seller seller_result = (seller) query.uniqueResult();
		return seller_result;
	}
	
	@Override
	public String getIndustryInfoByID(String id){
		String hql = "from industry_type where Industry_id=:Industry_id";
		Query query = getSession().createQuery(hql);
		query.setString("Industry_id", id);
		industry_type result = (industry_type) query.uniqueResult();
		return result.getIndustry_Name();
	}


}
