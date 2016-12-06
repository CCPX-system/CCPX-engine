package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import model.ExchangeRateVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.ViewExchangeRateService;

@Controller
@RequestMapping("/")
public class ViewExchangeRateController {

	static Logger log=Logger.getLogger(ViewExchangeRateController.class);
	static{
		log.info("right");
	}
	
	@Resource(name = "ViewExchangeRateServiceImpl")
	private ViewExchangeRateService  viewExchangeRateServiceImpl ;
	
	
	@RequestMapping(value="viewExchangeRate",method=RequestMethod.POST)
	@ResponseBody
	public ExchangeRateVO returnExchangeRates(HttpServletRequest req) {
		System.out.println("------------------------viewExchangeRate start--------------------------");
		ExchangeRateVO rates=new ExchangeRateVO();
		String sellerIdStr=null;
		String startTime=null;
		String endTime=null;
		int sellerId=-1;
		try{
			sellerIdStr=req.getParameter("sellerId");		
			startTime=req.getParameter("startTime");
			endTime=req.getParameter("endTime");

			//check int type of sellerId
			sellerId=Integer.parseInt(sellerIdStr);
			//check date type of startTime and endTime
			Date start=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(startTime);
			Date end=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(endTime);
			//check whether null of parameters
			if(sellerIdStr.equals("")||startTime.equals("")||endTime.equals("")||(start.getTime()>end.getTime())){
				rates.setErrno(4);
				rates.setErr("wrong parameter/missing parameters");
				return rates;
			}
			
		}catch(Exception e){
			log.error(e.toString());
			rates.setErrno(4);
			rates.setErr("wrong parameter/missing parameters");
			return rates;
		}
		
				
		try{
			rates=viewExchangeRateServiceImpl.getExchangeRates(sellerId, startTime, endTime);
		}catch(Exception e){			
			rates.setErr("logical error");
			rates.setErrno(5);
			log.error(e.toString());
			e.printStackTrace();
		}
		
		if(rates.getExchangeRates()==null){
			rates.setErr("empty result/empty response");
			rates.setErrno(3);
		}
		return rates;
	}
	
}
