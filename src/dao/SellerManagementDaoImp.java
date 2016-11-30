package dao;

import javax.annotation.Resource;

import model.activation_code;
import model.seller;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;


@Repository("sellerManagementDaoImp")
public class SellerManagementDaoImp implements SellerManagementDao {
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
	public seller checkSeller(String username, String password) {
		String hql = "from seller where Seller_Username= :username and Seller_Password= :password";
		Query query = getSession().createQuery(hql);
		query.setString("username", username);
		query.setString("password", password);
		seller Seller = (seller) query.uniqueResult();
		return Seller;
	}
	
	@Override
	public seller validateUsername(String username) {
		String hql = "from seller where Seller_Username= :username";
		Query query = getSession().createQuery(hql);
		query.setString("username", username);
		seller Seller = (seller) query.uniqueResult();
		return Seller;
	}
	
	
	@Override
	public boolean updateSellerinfo(seller seller) {

		String hql = "update seller set  Seller_Name=:Seller_Name,Seller_Address=:Seller_Address,Seller_Telephone=:Seller_Telephone,"
				+ "Seller_Email=:Seller_Email,IndustryType_id=:IndustryType_id,Seller_Logo=:Seller_Logo ,Seller_Description=:Seller_Description where Seller_id=:Seller_id";
		Query query = getSession().createQuery(hql);
		query.setInteger("Seller_id", seller.getSeller_id());
		query.setString("Seller_Name", seller.getSeller_Name());
		query.setString("Seller_Address", seller.getSeller_Address());
		query.setString("Seller_Telephone", seller.getSeller_Telephone());
		query.setString("Seller_Email", seller.getSeller_Email());
		query.setString("IndustryType_id", seller.getIndustryType_id());
		query.setString("Seller_Logo", seller.getSeller_Logo());
		query.setString("Seller_Description", seller.getSeller_Description());
		int a = query.executeUpdate();
		if (a > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean regist(seller seller) {
		System.out.println(seller.getSeller_Name());
		seller.setSeller_Status("0");
		session=getSession(); 
		session.beginTransaction();
		session.save(seller);
		session.flush();	
		session.getTransaction().commit();
		session.close();
		return true;
	}

	@Override
	public seller getSellerById(int Seller_id) {
		seller s=(seller) getSession().createQuery("from seller where Seller_id=:Seller_id ").setInteger("Seller_id", Seller_id).uniqueResult();
		return s;
	}
	
	@Override
	public boolean checkActivationCode(String code, String sellerid){
		String hql = "from activation_code where Seller_id= :Seller_id and Activation_Code= :Activation_Code";
		Query query = getSession().createQuery(hql);
		query.setString("Seller_id", sellerid);
		query.setString("Activation_Code", code);
		activation_code record = (activation_code) query.uniqueResult();
		if(record==null){
		return false;
		}else{
			return true;
		}
	}
	
	public boolean updateSellerStatus(String sellerid){
		String hql = "from seller where Seller_id=:Seller_id";
		Query query = getSession().createQuery(hql);
		query.setString("Seller_id", sellerid);
		seller Seller = (seller) query.uniqueResult();
		String a = Seller.getSeller_Status();
		if(a.equals("1")){
			return false;
		}
		else{
		String hql2 = "update seller set Seller_Status =:Seller_Status where Seller_id=:Seller_id";
		Query query2 = getSession().createQuery(hql2);
		query2.setString("Seller_Status", "1");
		query2.setInteger("Seller_id",Integer.parseInt(sellerid));
		query2.executeUpdate();
		return true;
		}
	}
}
