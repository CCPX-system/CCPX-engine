package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.industry_type;
import model.seller;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.InfoManagementService;

@Controller
@RequestMapping(value ="/info")
public class InfoManagementController {
	
	@Resource(name = "infoManagementServiceImp")
	private InfoManagementService infoManagementServiceImp;
	
	@RequestMapping("/getIndustryInfo")
	@ResponseBody
	public void getIndustryInfo(HttpServletRequest req, HttpServletResponse res) {
		List<industry_type> industry_type_list = infoManagementServiceImp.getIndustryInfo();
		JSONArray json = JSONArray.fromObject(industry_type_list);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
	}
	
	@RequestMapping("/getSellerInfo")
	@ResponseBody
	public void getSellerInfo(HttpServletRequest req, HttpServletResponse res) {
		List<seller> seller_list = infoManagementServiceImp.getSellerInfo();
		JSONArray json = JSONArray.fromObject(seller_list);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
	}
	
	@RequestMapping("/getSellerInfoByIndustryID")
	@ResponseBody
	public void getSellerInfoByIndustryID(HttpServletRequest req, HttpServletResponse res, String id) {
		List<seller> seller_list = infoManagementServiceImp.getSellerInfoByIndustryID(id);
		JSONArray json = JSONArray.fromObject(seller_list);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
	}
	
	@RequestMapping("/getCompanyDetail")
	@ResponseBody
	public void getCompanyDetail(HttpServletRequest req, HttpServletResponse res, String id) {
		
		
		seller seller_result = infoManagementServiceImp.getCompanyDetail(id);
		String industry_name = infoManagementServiceImp.getIndustryInfoByID(seller_result.getIndustryType_id());
		if(seller_result!=null){
		req.getSession().setAttribute("seller_result", seller_result);
		req.getSession().setAttribute("industry_name", industry_name);
		String message = "{'message':'success'}";
		JSONObject json = JSONObject.fromObject(message);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
		}
	}
	
	@RequestMapping("/getIndustryInfoByID")
	@ResponseBody
	public void getIndustryInfoByID(HttpServletRequest req,HttpServletResponse res, String id) {
		
		String industry_name = infoManagementServiceImp.getIndustryInfoByID(id);
		String message = "{'industry_name':'"+industry_name+"'}";
		JSONObject json = JSONObject.fromObject(message);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
	}
	
	@RequestMapping("/getSellerProfile")
	@ResponseBody
	public void getSellerProfile(HttpServletRequest req, HttpServletResponse res, String id) {
		
		
		seller seller_result = infoManagementServiceImp.getCompanyDetail(id);
		
		if(seller_result!=null){
		JSONObject json = JSONObject.fromObject(seller_result);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
		}else{
			String message = "{'message':'false'}";
			JSONObject json = JSONObject.fromObject(message);
	        System.out.print(json);
	        System.out.close();
	        PrintWriter out =null;
	        try{
	        out = res.getWriter();
	        out.write(json.toString());
	        out.flush();
	        out.close();
	        }catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (out != null) {  
	                out.close();  
	            } 
	        } 
		}
	}
	
	
	@RequestMapping("/getSellerInfoByKeyWord")
	@ResponseBody
	public void getSellerInfoByKeyWord(HttpServletRequest req, HttpServletResponse res, String keyword) {
		List<seller> seller_list = infoManagementServiceImp.getSellerInfoByKeyWord(keyword);
		JSONArray json = JSONArray.fromObject(seller_list);
        System.out.print(json);
        System.out.close();
        PrintWriter out =null;
        
        try{
        out = res.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (out != null) {  
                out.close();  
            } 
        } 
	}

}
