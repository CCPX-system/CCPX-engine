package dao;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import model.ad_present;
import model.advertisement;
import model.seller;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
@Repository("adDaoImp")
public class AdDaoImp implements AdDao{
	
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	private Session session;
	
	private Session getSession()
	{
		 
		if (session == null) {
			
			session = sessionFactory.openSession();
		} else {
			session=sessionFactory.getCurrentSession();
		}
		return session;
	}
	
	@Override
	public List<ad_present> getAd(){
		String hql = "from advertisement";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		List<advertisement> list = query.list();
		
		List<ad_present> advertisement_list = new ArrayList<ad_present>();
		
		for(int i=0;i<list.size();i++){
			int id = list.get(i).getSeller_id();
			String hql1 = "from seller where Seller_id=:Seller_id";
			Query query1 = getSession().createQuery(hql1);
			query1.setInteger("Seller_id", id);
			seller Seller = (seller) query1.uniqueResult();
			ad_present Ad = new ad_present();
			Ad.setSeller_name(Seller.getSeller_Name());
			Ad.setSeller_Description(Seller.getSeller_Description());
			Ad.setAdvertisement_Image(list.get(i).getAdvertisement_Image());
			advertisement_list.add(Ad);
		}
		return advertisement_list;
	}
	
	@Override
	public List<advertisement> getAdBySellerID(String id){
		String hql = "from advertisement where Seller_id =:Seller_id";
		Query query = getSession().createQuery(hql);
		query.setString("Seller_id", id);
		query.setFirstResult(0);
		query.setMaxResults(3);
		List<advertisement> list = query.list();
		return list;
	}
	
	@Override
	public int getNumberOfAdBySellerID(String id){
		String hql = "from advertisement where Seller_id =:Seller_id";
		Query query = getSession().createQuery(hql);
		query.setString("Seller_id", id);
		int num = query.list().size();
		return num;
	}
	
	@Override
	public boolean addAd(advertisement newad){
		session=getSession(); 
		session.beginTransaction();
		session.save(newad);
		session.flush();	
		session.getTransaction().commit();
		session.close();
		return true;
	}
	
	@Override
	public advertisement getAdByAdID(String id){
		String hql = "from advertisement where Advertisement_id =:Advertisement_id";
		Query query = getSession().createQuery(hql);
		query.setString("Advertisement_id", id);
		advertisement result =(advertisement) query.uniqueResult();
		return result;
	}
	
	@Override
	public boolean editAdd(advertisement ad){
		String hql = "update advertisement set Advertisement_Title=:Advertisement_Title,Advertisement_Image=:Advertisement_Image where Advertisement_id=:Advertisement_id";
		Query query = getSession().createQuery(hql);
		query.setString("Advertisement_Title", ad.getAdvertisement_Title());
		query.setString("Advertisement_Image", ad.getAdvertisement_Image());
		query.setInteger("Advertisement_id", ad.getAdvertisement_id());
		int a = query.executeUpdate();
		if (a > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean deleteAdd(String id){
		Query query=getSession().createQuery("delete from advertisement where Advertisement_id =:Advertisement_id");
		query.setString("Advertisement_id", id);
		query.executeUpdate();
		return true;
	}
}
