package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import utils.ListSortUtil;
import dao.IntelligentAnalysisDao;
import model.AllDiagramsData;
import model.ExchangeRecord;
import model.HistogramData;
import model.LineGraphData;
import model.PieChartData;

@Repository("IntelligentAnalysisServiceImpl")
public class IntelligentAnalysisServiceImpl implements IntelligentAnalysisService{

	@Resource(name = "IntelligentAnalysisDaoImpl")
	private IntelligentAnalysisDao intelligentAnalysisDaoImpl;
	
	@Override
	public AllDiagramsData getTradePointDiagramsData(int sellerId, String startTime, String endTime) {
		
		//store seller,trade points in same industry with sellerId
		Map<Integer, Double> sellersInSameIndustry=new HashMap<>();
		//store seller, trade points in different industry with sellerId
		Map<Integer, Double> sellersInDiffIndustry=new HashMap<>();
		//store industryType, trade points
		Map<String, Double> tradePointsForIndustry=new HashMap<>();
		//store time stamp, trade points' average
		Map<String, Double> tradePointTimeSequence=new LinkedHashMap<>();
		
		//get records
		List<ExchangeRecord> records=intelligentAnalysisDaoImpl.getIAExRec(sellerId, startTime, endTime);
		
		//sort by exchange time
		ListSortUtil<ExchangeRecord> listSort= new ListSortUtil<>();  	
        listSort.Sort(records, "getEx_time", "asc");  
        
        //ensure how long of a period time for time sequence diagram
        String timeUnit=getCalTimeUnit(records.get(0).getEx_time(),records.get(records.size()-1).getEx_time());
        //System.out.print("---------------------------"+timeUnit) ;
        
		//get industry type of the sellerId
		String industryTypeOfSellerId=intelligentAnalysisDaoImpl.getIndustryTypeBySellerId(sellerId);

		String firstTime=getTimeOfUnit(records.get(0).getEx_time(), timeUnit);
		double tradePointInUnit=0;
		int count=0;
		for(int i=0;i<records.size();i++){
			//get trade points of seller and industry type of seller with exchange
			ExchangeRecord er=records.get(i);			
			double tradePoints=0;
			int sellerIdWithExchange=-1;
			if(er.getSeller_A_Id()==sellerId){
				tradePoints=er.getPoint_A();
				sellerIdWithExchange=er.getSeller_B_Id();
			}else{
				sellerIdWithExchange=er.getSeller_A_Id();
				tradePoints=er.getPoint_B();
			}
			String industryType=intelligentAnalysisDaoImpl.getIndustryTypeBySellerId(sellerIdWithExchange);
			
			//get data for seller rank in different and same industry and industry ratio of trade points
			if(industryType.equals(industryTypeOfSellerId)){
				if(sellersInSameIndustry.get(sellerIdWithExchange)!=null){
					sellersInSameIndustry.put(sellerIdWithExchange, sellersInSameIndustry.get(sellerIdWithExchange)+tradePoints);
				}else{
					sellersInSameIndustry.put(sellerIdWithExchange, tradePoints);
				}
			}else{
				if(sellersInDiffIndustry.get(sellerIdWithExchange)!=null){
					sellersInDiffIndustry.put(sellerIdWithExchange, sellersInDiffIndustry.get(sellerIdWithExchange)+tradePoints);
				}else{
					sellersInDiffIndustry.put(sellerIdWithExchange, tradePoints);
				}			
			}
			if(tradePointsForIndustry.get(industryType)!=null){
				tradePointsForIndustry.put(industryType, tradePointsForIndustry.get(industryType)+tradePoints);
			}else{
				tradePointsForIndustry.put(industryType, tradePoints);
			}
			
			//get data for trade point time sequence
			String timeUnitValue=getTimeOfUnit(er.getEx_time(), timeUnit);
			if(timeUnitValue.equals(firstTime)){
				tradePointInUnit+=tradePoints;
				count++;
			}else{
				double avg=(double)tradePointInUnit/(double)count;
				tradePointTimeSequence.put(firstTime, avg);
				tradePointInUnit=tradePoints;
				count=1;
				firstTime=er.getEx_time();
			}
			double avg=(double)tradePointInUnit/(double)count;
			tradePointTimeSequence.put(firstTime, avg);
		}
				
		//get all data of allDiagramData class
		String tradeType="Trading Points";
		AllDiagramsData datas=new AllDiagramsData();
		if(sellersInSameIndustry.size()!=0){
			datas.setTopSameIndustry(getTopSameIndustry(sellersInSameIndustry, 5, tradeType));
		}
		if(sellersInDiffIndustry.size()!=0){
			datas.setTopDiffIndustry(getTopDiffIndustry(sellersInDiffIndustry, 5, tradeType));
		}
		if(tradePointsForIndustry.size()!=0){
			datas.setIndustryRatio(getPieChartData(tradePointsForIndustry, tradeType));
		}
		if(tradePointTimeSequence.size()!=0){
			datas.setTimeSequence(getLineGraphData(tradePointTimeSequence, tradeType));
		}
		
		return datas;
	}
	
	/**
	 * get top 5 sellers in same industry
	 * @param sellersInSameIndustry
	 * @param top
	 * @return
	 */
	private HistogramData getTopSameIndustry(Map<Integer, Double> sellersInSameIndustry, int top, String tradeType){
	    List<Map.Entry<Integer, Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(sellersInSameIndustry.entrySet());
	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer, Double>>()
	    {  
	      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2)
	      {
	        if ((o2.getValue() - o1.getValue())>0)
	          return 1;
	        else if((o2.getValue() - o1.getValue())==0)
	          return 0;
	        else 
	          return -1;
	      }
	    });
	    
	    List seriesData=new ArrayList();
	    for(int i=0;i<list_Data.size()&&i<top;i++){
	    	int sellerId=list_Data.get(i).getKey();
	    	String sellerName=intelligentAnalysisDaoImpl.getSellerNameBySellerId(sellerId);
	    	double tradePoints=list_Data.get(i).getValue();
	    	Object[] pt=new Object[2];
	    	pt[0]=sellerName;
	    	pt[1]=tradePoints;
	    	seriesData.add(pt);
	    }
	    HistogramData hd=new HistogramData();
	    hd.setSeriesData(seriesData);
	    hd.setyAxisTitleText(tradeType);
	    return hd;
	}
	
	/**
	 * get top 5 sellers in different industry
	 * @param sellersInDiffIndustry
	 * @param top
	 * @return
	 */
	private HistogramData getTopDiffIndustry(Map<Integer, Double> sellersInDiffIndustry, int top, String tradeType){
	    List<Map.Entry<Integer, Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(sellersInDiffIndustry.entrySet());
	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer, Double>>()
	    {  
	      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2)
	      {
	        if ((o2.getValue() - o1.getValue())>0)
	          return 1;
	        else if((o2.getValue() - o1.getValue())==0)
	          return 0;
	        else 
	          return -1;
	      }
	    });
	    
	    List seriesData=new ArrayList();
	    for(int i=0;i<list_Data.size()&&i<top;i++){
	    	int sellerId=list_Data.get(i).getKey();
	    	String sellerName=intelligentAnalysisDaoImpl.getSellerNameBySellerId(sellerId);
	    	double tradePoints=list_Data.get(i).getValue();
	    	Object[] pt=new Object[2];
	    	pt[0]=sellerName;
	    	pt[1]=tradePoints;
	    	seriesData.add(pt);
	    }
	    HistogramData hd=new HistogramData();
	    hd.setSeriesData(seriesData);
	    hd.setyAxisTitleText(tradeType);
	    return hd;
	}
	
	/**
	 * get ratio of each industry
	 * @param tradePointsForIndustry
	 * @return
	 */
	private PieChartData getPieChartData(Map<String, Double> tradePointsForIndustry, String tradeType){
		List seriesData=new ArrayList();
		double sum=0;
		for (Double v : tradePointsForIndustry.values()) {  
			sum+=v;
		}
		for (Entry<String, Double> entry : tradePointsForIndustry.entrySet()) {  
			String industryType=entry.getKey();
			double percentage=entry.getValue()/sum;
			BigDecimal bg = new BigDecimal(percentage);  
            double perFormat = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            Object[] pt=new Object[2];
            pt[0]=industryType;
            pt[1]=perFormat;
			seriesData.add(pt);
		}  
		PieChartData pcd=new PieChartData();
		pcd.setSeriesData(seriesData);
		pcd.setTitleText(tradeType);
		
		return pcd;
	}
	
	/**
	 * get time and trade points for time sequence diagram
	 * @param tradePointTimeSequence
	 * @return
	 */
	private LineGraphData getLineGraphData(Map<String, Double> tradePointTimeSequence, String tradeType){
		ArrayList<String> times=new ArrayList<>();
		ArrayList<Double> tradePoints=new ArrayList<>();
		
		for (Entry<String, Double> entry : tradePointTimeSequence.entrySet()) {  
			String time=entry.getKey();
			double tradePoint=entry.getValue();
			
			times.add(time);
			tradePoints.add(tradePoint);
		}  
		LineGraphData lgd=new LineGraphData();
		lgd.setSeriesData(tradePoints);
		lgd.setxAxisCategories(times);
		lgd.setyAxisTitleText(tradeType);
		return lgd;
	}
	
	/**
	 * ensure the calculate time unit for trade point time sequence
	 * @param minTime
	 * @param maxTime
	 * @return
	 */
	private String getCalTimeUnit(String minTime, String maxTime){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try{
			Date minDate = df.parse(minTime);
			Date maxDate=df.parse(maxTime);
			Calendar minCalendar = Calendar.getInstance();
			minCalendar.setTime(minDate);
			Calendar maxCalendar = Calendar.getInstance();
			maxCalendar.setTime(maxDate);
			
			if(maxCalendar.get(Calendar.YEAR)!=minCalendar.get(Calendar.YEAR)){
				return "year";
			}else if(maxCalendar.get(Calendar.MONTH)!=minCalendar.get(Calendar.MONTH)){
				return "month";
			}else if(maxCalendar.get(Calendar.DATE)!=minCalendar.get(Calendar.DATE)){
				return "day";
			}else if(maxCalendar.get(Calendar.HOUR_OF_DAY)!=minCalendar.get(Calendar.HOUR_OF_DAY)){
				return "hour";
			}else if(maxCalendar.get(Calendar.MINUTE)!=minCalendar.get(Calendar.MINUTE)){
				return "minute";
			}else if(maxCalendar.get(Calendar.SECOND)!=minCalendar.get(Calendar.SECOND)){
				return "second";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "second";
	}
	
	/**
	 * get each exchange time of unit format
	 * @param time
	 * @param unit
	 * @return
	 */
	private String getTimeOfUnit(String time, String unit){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try{
			Date date = df.parse(time);			
			calendar.setTime(date);
			
			if(unit.equals("year")){
				df=new SimpleDateFormat("yyyy");
			}else if(unit.equals("month")){
				df = new SimpleDateFormat("yyyy/MM");
			}else if(unit.equals("day")){
				df = new SimpleDateFormat("yyyy/MM/dd");
			}else if(unit.equals("hour")){
				calendar.set(Calendar.MINUTE, 0); 
				calendar.set(Calendar.SECOND, 0); 
			}else if(unit.equals("minute")){
				calendar.set(Calendar.SECOND, 0); 
				int minute=calendar.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE)%10;
				calendar.set(Calendar.MINUTE, minute);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return  df.format(calendar.getTime());
	}
	
	@Override
	public AllDiagramsData getTradeCountDiagramsData(int sellerId, String startTime, String endTime) {
		//store seller,trade points in same industry with sellerId
		Map<Integer, Double> sellersInSameIndustry=new HashMap<>();
		//store seller, trade points in different industry with sellerId
		Map<Integer, Double> sellersInDiffIndustry=new HashMap<>();
		//store industryType, trade points
		Map<String, Double> tradePointsForIndustry=new HashMap<>();
		//store time stamp, trade points' average
		Map<String, Double> tradePointTimeSequence=new LinkedHashMap<>();
		
		//get records
		List<ExchangeRecord> records=intelligentAnalysisDaoImpl.getIAExRec(sellerId, startTime, endTime);
		
		//sort by exchange time
		ListSortUtil<ExchangeRecord> listSort= new ListSortUtil<>();  	
        listSort.Sort(records, "getEx_time", "asc");  
        
        //ensure how long of a period time for time sequence diagram
        String timeUnit=getCalTimeUnit(records.get(0).getEx_time(),records.get(records.size()-1).getEx_time());
        //System.out.print("---------------------------"+timeUnit) ;
        
		//get industry type of the sellerId
		String industryTypeOfSellerId=intelligentAnalysisDaoImpl.getIndustryTypeBySellerId(sellerId);

		String firstTime=getTimeOfUnit(records.get(0).getEx_time(), timeUnit);
		double tradeCountInUnit=0;
		for(int i=0;i<records.size();i++){
			//get trade points of seller and industry type of seller with exchange
			ExchangeRecord er=records.get(i);			
			double tradeCounts=1;
			int sellerIdWithExchange=-1;
			if(er.getSeller_A_Id()==sellerId){
				sellerIdWithExchange=er.getSeller_B_Id();
			}else{
				sellerIdWithExchange=er.getSeller_A_Id();
			}
			String industryType=intelligentAnalysisDaoImpl.getIndustryTypeBySellerId(sellerIdWithExchange);
			
			//get data for seller rank in different and same industry and industry ratio of trade points
			if(industryType.equals(industryTypeOfSellerId)){
				if(sellersInSameIndustry.get(sellerIdWithExchange)!=null){
					sellersInSameIndustry.put(sellerIdWithExchange, sellersInSameIndustry.get(sellerIdWithExchange)+tradeCounts);
				}else{
					sellersInSameIndustry.put(sellerIdWithExchange, tradeCounts);
				}
			}else{
				if(sellersInDiffIndustry.get(sellerIdWithExchange)!=null){
					sellersInDiffIndustry.put(sellerIdWithExchange, sellersInDiffIndustry.get(sellerIdWithExchange)+tradeCounts);
				}else{
					sellersInDiffIndustry.put(sellerIdWithExchange, tradeCounts);
				}			
			}
			if(tradePointsForIndustry.get(industryType)!=null){
				tradePointsForIndustry.put(industryType, tradePointsForIndustry.get(industryType)+tradeCounts);
			}else{
				tradePointsForIndustry.put(industryType, tradeCounts);
			}
			
			//get data for trade point time sequence
			String timeUnitValue=getTimeOfUnit(er.getEx_time(), timeUnit);
			if(timeUnitValue.equals(firstTime)){
				tradeCountInUnit+=tradeCounts;
			}else{
				tradePointTimeSequence.put(firstTime, tradeCountInUnit);
				tradeCountInUnit=tradeCounts;
				firstTime=er.getEx_time();
			}
			tradePointTimeSequence.put(firstTime, tradeCountInUnit);
		}		
		
		//get all data of allDiagramData class
		String tradeType="Trading Count";
		AllDiagramsData datas=new AllDiagramsData();
		if(sellersInSameIndustry.size()!=0){
			datas.setTopSameIndustry(getTopSameIndustry(sellersInSameIndustry, 5, tradeType));
		}
		if(sellersInDiffIndustry.size()!=0){
			datas.setTopDiffIndustry(getTopDiffIndustry(sellersInDiffIndustry, 5, tradeType));
		}
		if(tradePointsForIndustry.size()!=0){
			datas.setIndustryRatio(getPieChartData(tradePointsForIndustry, tradeType));
		}
		if(tradePointTimeSequence.size()!=0){
			datas.setTimeSequence(getLineGraphData(tradePointTimeSequence, tradeType));
		}
		
		return datas;
	}
	
	
}
