package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import model.AllDiagramsData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.IntelligentAnalysisService;

@Controller
@RequestMapping("/")
public class IntelligentAnalysisController {

	static Logger log=Logger.getLogger(IntelligentAnalysisController.class);
	static{
		log.info("right");
	}
	
	@Resource(name = "IntelligentAnalysisServiceImpl")
	private IntelligentAnalysisService  intelligentAnalysisService ;
	
	@RequestMapping(value="intelligentAnalysis",method=RequestMethod.POST)
	@ResponseBody
	public AllDiagramsData doIntelligentAnalysis(HttpServletRequest req) {
		
		System.out.println("--------------------------IntelligentAnalysis start--------------------------");
		AllDiagramsData datas=new AllDiagramsData();
		String sellerIdStr=null;
		String startTime=null;
		String endTime=null;
		String tradeType=null;
		int sellerId=-1;
		try{
			sellerIdStr=req.getParameter("sellerId");		
			startTime=req.getParameter("startTime");
			endTime=req.getParameter("endTime");
			tradeType=req.getParameter("tradeType");
			
			//check int type of sellerId
			sellerId=Integer.parseInt(sellerIdStr);
			//check date type of startTime and endTime
			Date start=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(startTime);
			Date end=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(endTime);
			//check whether null of parameters
			if(sellerIdStr.equals("")||startTime.equals("")||endTime.equals("")||tradeType.equals("")||(start.getTime()>end.getTime())
					||!(tradeType.equals("0")||tradeType.equals("1"))){
				datas.setErrno(4);
				datas.setErr("wrong parameter/missing parameters");
				return datas;
			}
			
		}catch(Exception e){
			log.error("IntelligentAnalysisController:"+e.toString());
			datas.setErrno(4);
			datas.setErr("wrong parameter/missing parameters");
			return datas;
		}
		
				
		try{
			//0 is trade points, 1 is trade counts
			if(tradeType.equals("0")){
				datas=intelligentAnalysisService.getTradePointDiagramsData(sellerId, startTime, endTime);
			}else if(tradeType.equals("1")){
				datas=intelligentAnalysisService.getTradeCountDiagramsData(sellerId, startTime, endTime);
			}
		}catch(Exception e){
			log.error("IntelligentAnalysisController:"+e.toString());
			datas.setErr("logical error");
			datas.setErrno(5);
		}
		
		if(datas.getIndustryRatio()==null&&datas.getTimeSequence()==null&&
				datas.getTopDiffIndustry()==null&&datas.getTopSameIndustry()==null){
			datas.setErr("empty result/empty response");
			datas.setErrno(3);
		}
		return datas;
	}
	

}
