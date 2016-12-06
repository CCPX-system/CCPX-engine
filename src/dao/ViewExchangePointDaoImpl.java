package dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import utils.HttpUtil;

import com.alibaba.fastjson.JSON;
import controller.IntelligentAnalysisController;
import model.ExchangeRecord;

@Repository("ViewExchangePointDaoImpl")
public class ViewExchangePointDaoImpl implements ViewExchangePointDao{

	static Logger log=Logger.getLogger(IntelligentAnalysisController.class);

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
	public List<ExchangeRecord> getLatExRec(int seller_id, int record_num) {
		List<ExchangeRecord> recs=new ArrayList<ExchangeRecord>();
		
		try{
			// send request to blockchain then get data
			String url="http://148.100.5.138:9999/getLatExRec";
			String param="SELLER_ID="+seller_id+"&RECORD_NUM="+record_num;
			String result=HttpUtil.sendPost(url, param);
			
			//just for test
			//String result=TestInterfaceOfBlockChain.getIAExRec();
			String respond=JSON.parseObject(result).get("respond").toString();
			if(respond.equals("300")){
				String content=JSON.parseObject(result).get("content").toString();
				recs=JSON.parseArray(content, ExchangeRecord.class);
			}else{
				log.error("can't get data from blockchain, the responde code is "+respond);
			}
		}catch(Exception e){
			log.error(e.toString());
		}

		return recs;
	}

	@Override
	public String getUserNameByUserId(int userId) {
		String hql="SELECT u_name FROM user WHERE u_id= :userId";
		Query query=getSession().createSQLQuery(hql);
		query.setInteger("userId", userId);
		String userName=(String) query.uniqueResult();
		return userName;
	}

}
