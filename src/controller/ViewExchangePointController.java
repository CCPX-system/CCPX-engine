package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import model.ExchangePointVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.ViewExchangePointService;

@Controller
@RequestMapping("/")
public class ViewExchangePointController {

	static Logger log=Logger.getLogger(ViewExchangePointController.class);
	static{
		log.info("right");
	}
	
	@Resource(name = "ViewExchangePointServiceImpl")
	private ViewExchangePointService  viewExchangePointServiceImpl ;
	
	
	@RequestMapping(value="viewExchangePoint",method=RequestMethod.POST)
	@ResponseBody
	public ExchangePointVO returnExchangePoints(HttpServletRequest req) {
		System.out.println("------------------------viewExchangePoint start--------------------------");
		ExchangePointVO epvo=new ExchangePointVO();
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
				epvo.setErrno(4);
				epvo.setErr("wrong parameter/missing parameters");
				return epvo;
			}
			
		}catch(Exception e){
			log.error(e.toString());
			epvo.setErrno(4);
			epvo.setErr("wrong parameter/missing parameters");
			return epvo;
		}
		
				
		try{
			epvo=viewExchangePointServiceImpl.getExchangePoint(sellerId, startTime, endTime);
		}catch(Exception e){			
			epvo.setErr("logical error");
			epvo.setErrno(5);
			log.error(e.toString());
			e.printStackTrace();
		}
		
		if(epvo.getExchangePoints()==null){
			epvo.setErr("empty result/empty response");
			epvo.setErrno(3);
		}
		return epvo;
	}
	
	
	@RequestMapping(value="viewLatestExchangePoint",method=RequestMethod.POST)
	@ResponseBody
	public ExchangePointVO returnLatestExchangePoints(HttpServletRequest req) {
		System.out.println("------------------------viewLatestExchangePoint start--------------------------");
		ExchangePointVO epvo=new ExchangePointVO();
		String sellerIdStr=null;
		int sellerId=-1;
		try{
			sellerIdStr=req.getParameter("sellerId");		

			//check int type of sellerId
			sellerId=Integer.parseInt(sellerIdStr);
			//check whether null of parameters
			if(sellerIdStr.equals("")){
				epvo.setErrno(4);
				epvo.setErr("wrong parameter/missing parameters");
				return epvo;
			}
			
		}catch(Exception e){
			log.error(e.toString());
			epvo.setErrno(4);
			epvo.setErr("wrong parameter/missing parameters");
			return epvo;
		}
		
				
		try{
			epvo=viewExchangePointServiceImpl.getLatestExchangePoint(sellerId);
		}catch(Exception e){			
			epvo.setErr("logical error");
			epvo.setErrno(5);
			log.error(e.toString());
			e.printStackTrace();
		}
		
		if(epvo.getExchangePoints()==null){
			epvo.setErr("empty result/empty response");
			epvo.setErrno(3);
		}
		return epvo;
	}
	
}
