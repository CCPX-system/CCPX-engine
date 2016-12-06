package service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import model.ExchangePoint;
import model.ExchangePointVO;
import model.ExchangeRecord;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import utils.ListSortUtil;
import controller.IntelligentAnalysisController;
import dao.IntelligentAnalysisDao;
import dao.ViewExchangePointDao;

@Repository("ViewExchangePointServiceImpl")
public class ViewExchangePointServiceImpl implements ViewExchangePointService{

	static Logger log=Logger.getLogger(IntelligentAnalysisController.class);
	
	@Resource(name = "IntelligentAnalysisDaoImpl")
	private IntelligentAnalysisDao intelligentAnalysisDaoImpl;
	
	@Resource(name = "ViewExchangePointDaoImpl")
	private ViewExchangePointDao viewExchangePointDaoImpl;
	
	@Override
	public ExchangePointVO getExchangePoint(int sellerId, String startTime, String endTime) {
		
		ExchangePointVO epvo=new ExchangePointVO();
		
		try{
			List<ExchangeRecord> records=intelligentAnalysisDaoImpl.getIAExRec(sellerId, startTime, endTime);
			//sort by exchange time
			ListSortUtil<ExchangeRecord> listSort= new ListSortUtil<>();  	
	        listSort.Sort(records, "getEx_time", "asc");  
	        
	        double totalPoint=0;
	        List<ExchangePoint> exchangePoints=new ArrayList<ExchangePoint>();
	        
	        for(int i=0;i<records.size();i++){
	        	ExchangeRecord er=records.get(i);
	        	String user_A_name=viewExchangePointDaoImpl.getUserNameByUserId(er.getUser_A_Id());
	        	String seller_B_name=intelligentAnalysisDaoImpl.getSellerNameBySellerId(er.getSeller_B_Id());
	        	String seller_A_name=intelligentAnalysisDaoImpl.getSellerNameBySellerId(er.getSeller_A_Id());
	        	ExchangePoint ep1=new ExchangePoint(user_A_name,seller_B_name,er.getPoint_B(),seller_A_name,er.getPoint_A(),er.getEx_time());
	        	String user_B_name=viewExchangePointDaoImpl.getUserNameByUserId(er.getUser_B_Id());
	        	ExchangePoint ep2=new ExchangePoint(user_B_name,seller_A_name,er.getPoint_A(),seller_B_name,er.getPoint_B(),er.getEx_time());
	        	exchangePoints.add(ep1);
	        	exchangePoints.add(ep2);
	        	if(er.getSeller_A_Id()==sellerId){
					totalPoint+=er.getPoint_A();
				}else{
					totalPoint+=er.getPoint_B();
				}
	        }
	        
	        epvo.setExchangePoints(exchangePoints);
	        epvo.setTotalPoint(totalPoint);
			
		}catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}

		return epvo;
	}

	@Override
	public ExchangePointVO getLatestExchangePoint(int sellerId) {
		ExchangePointVO epvo=new ExchangePointVO();
		
		try{
			List<ExchangeRecord> records=viewExchangePointDaoImpl.getLatExRec(sellerId, 5);
			//sort by exchange time
			ListSortUtil<ExchangeRecord> listSort= new ListSortUtil<>();  	
	        listSort.Sort(records, "getEx_time", "desc");  
	        
	        List<ExchangePoint> exchangePoints=new ArrayList<ExchangePoint>();
	        
	        for(int i=0;i<records.size();i++){
	        	ExchangeRecord er=records.get(i);
	        	String user_A_name=viewExchangePointDaoImpl.getUserNameByUserId(er.getUser_A_Id());
	        	String seller_B_name=intelligentAnalysisDaoImpl.getSellerNameBySellerId(er.getSeller_B_Id());
	        	String seller_A_name=intelligentAnalysisDaoImpl.getSellerNameBySellerId(er.getSeller_A_Id());
	        	ExchangePoint ep1=new ExchangePoint(user_A_name,seller_B_name,er.getPoint_B(),seller_A_name,er.getPoint_A(),er.getEx_time());
	        	String user_B_name=viewExchangePointDaoImpl.getUserNameByUserId(er.getUser_B_Id());
	        	ExchangePoint ep2=new ExchangePoint(user_B_name,seller_A_name,er.getPoint_A(),seller_B_name,er.getPoint_B(),er.getEx_time());
	        	exchangePoints.add(ep1);
	        	exchangePoints.add(ep2);
	        }
	        
	        epvo.setExchangePoints(exchangePoints);
			
		}catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}

		return epvo;
	}
		
}
