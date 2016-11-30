package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import model.AmountPoint;
import model.SellerStatusInfo;
import model.Seller_transferInfoBean;
import model.TransferRecord;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.SellerTranserService;

@Controller
@RequestMapping("seller")
public class SellerTransferController extends SellerBaseController {

	@Resource(name="SellerTranserServiceImp")
	private SellerTranserService transferserviceImp;
	
	
	@RequestMapping(value="transfer",method=RequestMethod.POST)
	@ResponseBody
	public SellerStatusInfo transferpoint(Seller_transferInfoBean transferbean){
		
		SellerStatusInfo si = super.CreateStatus();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		String nowtime=df.format(new Date());
		transferbean.setCreatetime(nowtime);
		if(transferbean.getUserid()==null||"".equalsIgnoreCase(transferbean.getUserid())){
			si.setMsg("invalid parameter ");
			si.setStatus(1);
			return si;
		}
		//设置时间
		
		si=transferserviceImp.point(transferbean);
		
		logger.warn(transferbean.toString());
		return si;
	}
	@RequestMapping(value="listrecord",method=RequestMethod.POST)
	@ResponseBody
	public SellerStatusInfo transrecord(@RequestParam(value="sellerid", defaultValue="0") int sellerid,String time1,String time2,@RequestParam(value="timelen", defaultValue="0") int timelen) throws ParseException{
		SellerStatusInfo si = super.CreateStatus();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		logger.warn(time2+" "+time1+" "+timelen);
		if(sellerid<=0){
			si.setMsg("invalid parameter");
			si.setStatus(101);
			return si;
		}
		List<Object>  records=transferserviceImp.querypointrecord(sellerid,time1,time2,timelen);
		List<TransferRecord> recordresult=new ArrayList<>();
		
		
		
		TransferRecord record=null;
		for(int i=0;i<records.size();i++){
			Object[] obj=(Object[]) records.get(i);
			record=new TransferRecord();
			 record.setTime((String)df.format(obj[0]));
			 record.setPoints((int)obj[1]);
			 record.setType((boolean)obj[2]?"income":"outcome");
			 record.setUsername((String)obj[3]);	
			 recordresult.add(record);		
		}
		si.setData(recordresult);
		return si;
	}
	@RequestMapping(value="amoutpoint",method=RequestMethod.POST)
	@ResponseBody
	public SellerStatusInfo amountpoint(@RequestParam(value="sellerid", defaultValue="0") int sellerid,String time1,String time2) throws ParseException{
		SellerStatusInfo si = super.CreateStatus();
		if(sellerid==0){
			si.setMsg("need sellerid");
			si.setStatus(101);
			return si;
		}
		AmountPoint points=transferserviceImp.questamountPoint(sellerid,time1,time2);
		si.setData(points);
		return si;
	}
	@RequestMapping(value="addmembership",method=RequestMethod.POST)
	@ResponseBody
	public SellerStatusInfo membership(@RequestParam(value="u_id", defaultValue="0") int u_id,@RequestParam(value="seller_id", defaultValue="0") int seller_id,@RequestParam(value="points", defaultValue="0") int points,@RequestParam(value="point_blocked", defaultValue="0") int point_blocked){
		SellerStatusInfo si = super.CreateStatus();
		if(u_id>0&&seller_id>0&&points>=0&&point_blocked>=0){
			int result=transferserviceImp.addMembership(u_id, seller_id, points, point_blocked);
			if(result<=0){
				si.setStatus(300);
				si.setMsg("error in param");
			}
		}else{
			si.setStatus(301);
			si.setMsg("invalid param");
		}
		
		return si;
	}
	
	
}
