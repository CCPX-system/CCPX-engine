package dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;

import utils.HttpUtil;
import model.ExchangeRecord;

@Repository("IntelligentAnalysisDaoImpl")
public class IntelligentAnalysisDaoImpl implements IntelligentAnalysisDao{

	static Logger log=Logger.getLogger(IntelligentAnalysisDaoImpl.class);
	
	@Resource(name="sessionFactory")
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
	public List<ExchangeRecord> getIAExRec(int sellerId, String startTime, String endTime) {
		
		List<ExchangeRecord> recs=new ArrayList<ExchangeRecord>();
		
		try{
			// send request to blockchain then get data
			String url="http://148.100.5.138:9999/getToExPo";
			String param="SELLER_ID="+sellerId+"&START_TIME="+startTime+"&END_TIME="+endTime;
			String result=HttpUtil.sendPost(url, param);		
			//System.out.println(result);

			String respond=JSON.parseObject(result).get("respond").toString();
			if(respond.equals("300")){
				String content=JSON.parseObject(result).get("content").toString();
				recs=JSON.parseArray(content, ExchangeRecord.class);
			}else{
				log.error("can't get data from blockchain, the responde code is "+respond);
			}
		}catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}
		
		return recs;
	}

	@Override
	public String getIndustryTypeBySellerId(int sellerId) {
		String hql="SELECT b.Industry_Name FROM seller a, industry_type b WHERE b.Industry_id=a.IndustryType_id AND a.Seller_id= :sellerId";
		Query query=getSession().createQuery(hql);
		query.setInteger("sellerId", sellerId);
		String industryType=(String) query.uniqueResult();
		return industryType;
	}

	@Override
	public String getSellerNameBySellerId(int sellerId) {
		String hql="SELECT Seller_Name FROM seller WHERE Seller_id= :sellerId";
		Query query=getSession().createQuery(hql);
		query.setInteger("sellerId", sellerId);
		String industryType=(String) query.uniqueResult();
		return industryType;
	}

}
