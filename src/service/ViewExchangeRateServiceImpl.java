package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import controller.IntelligentAnalysisController;
import dao.IntelligentAnalysisDao;
import model.ExchangeRate;
import model.ExchangeRateVO;
import model.ExchangeRecord;

@Repository("ViewExchangeRateServiceImpl")
public class ViewExchangeRateServiceImpl implements ViewExchangeRateService{

	@Resource(name = "IntelligentAnalysisDaoImpl")
	private IntelligentAnalysisDao intelligentAnalysisDaoImpl;
	
	static Logger log=Logger.getLogger(IntelligentAnalysisController.class);
	
	@Override
	public ExchangeRateVO getExchangeRates(int sellerId, String startTime, String endTime) {
		
		ExchangeRateVO ervo=new ExchangeRateVO();
		
		try{
			
			//store seller id, sum of exchange rate
			Map<Integer, Double> exchangeRateSum=new LinkedHashMap<>();
			//store seller id, sum of record count 
			Map<Integer, Integer> exchangeCountSum=new LinkedHashMap<>();
			
			//get records by invoke intelligent analysis's dao 
			List<ExchangeRecord> records=intelligentAnalysisDaoImpl.getIAExRec(sellerId, startTime, endTime);
	        
			for(int i=0;i<records.size();i++){
				//get trade points of seller and industry type of seller with exchange
				ExchangeRecord er=records.get(i);
				
				double rate=0;
				int sellerIdWithExchange=-1;
				
				if(er.getSeller_A_Id()==sellerId){
					rate=er.getPoint_A()/er.getPoint_B();
					sellerIdWithExchange=er.getSeller_B_Id();
				}else{
					sellerIdWithExchange=er.getSeller_A_Id();
					rate=er.getPoint_B()/er.getPoint_A();
				}
			
				//calculate sum of exchange rate for each seller
				if(exchangeRateSum.get(sellerIdWithExchange)!=null){
					exchangeRateSum.put(sellerIdWithExchange, exchangeRateSum.get(sellerIdWithExchange)+rate);
					exchangeCountSum.put(sellerIdWithExchange, exchangeCountSum.get(sellerIdWithExchange)+1);
				}else{
					exchangeRateSum.put(sellerIdWithExchange, rate);
					exchangeCountSum.put(sellerIdWithExchange, 1);
				}
			}
			
			List ers=new ArrayList<ExchangeRate>();
			//calculate average of exchange rate and set seller name and industry type
			for (Entry<Integer, Double> s : exchangeRateSum.entrySet()) {
				int sellerIdWithExchange=s.getKey();
				String sellerName=intelligentAnalysisDaoImpl.getSellerNameBySellerId(sellerIdWithExchange);
				String industryType=intelligentAnalysisDaoImpl.getIndustryTypeBySellerId(sellerIdWithExchange);
				double avgRate=s.getValue()/(double)exchangeCountSum.get(sellerIdWithExchange);
				BigDecimal bg = new BigDecimal(avgRate);  
	            double avgRateFormat = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	            
	            ExchangeRate er=new ExchangeRate(sellerIdWithExchange, sellerName, industryType, avgRateFormat);
	            ers.add(er);
			}
			
			ervo.setExchangeRates(ers);
			
		}catch(Exception e){
			log.error(e.toString());
		}

		return ervo;
	}
		
}
