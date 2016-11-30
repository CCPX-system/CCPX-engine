package controller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ad_present;
import model.advertisement;
import model.seller;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import service.AdService;

@Controller
@RequestMapping(value ="/ad")
public class AdController {
	@Resource(name = "adServiceImp")
	private AdService adServiceImp;
			
	
@RequestMapping("getAd")
@ResponseBody
	public void getAd(HttpServletRequest req, HttpServletResponse res) {
		List<ad_present> advertisement_list = adServiceImp.getAd();
		JSONArray json = JSONArray.fromObject(advertisement_list);
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

@RequestMapping("getAdBySellerID")
@ResponseBody
	public void getAdBySellerID(HttpServletRequest req, HttpServletResponse res, String id) {
		List<advertisement> advertisement_list = adServiceImp.getAdBySellerID(id);
		JSONArray json = JSONArray.fromObject(advertisement_list);
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

@RequestMapping("addAd")
@ResponseBody
	public void addAd(HttpServletRequest req, HttpServletResponse res, String id, String title, MultipartFile Adimage) {
	
	int num = adServiceImp.getNumberOfAdBySellerID(id);
	res.setCharacterEncoding("UTF-8"); 
    res.setContentType("text/json");
    PrintWriter out =null;
	if(num>=3){
		String message = "more_than_three";
		        try{
		        out = res.getWriter();
		        out.write(message);
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
		    String path = null;
	        String type = null;
	        String fileName = Adimage.getOriginalFilename();
	        type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
		  if (Adimage.getSize()<2000000) {
         	 type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
         	 if (type!=null) {
         		 if ("PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {//图片格式正确，执行添加广告操作
         			String realPath=req.getSession().getServletContext().getRealPath("/")+"images/";
		                String trueFileName="ad"+id+"_"+String.valueOf(num+1)+"."+type;
		                path=realPath+trueFileName;
		                try{
		                	Adimage.transferTo(new File(path));}
		                catch (IOException e) {  
	       		            e.printStackTrace();  
	       		        } finally {  
	       		        } 
		                advertisement newad = new advertisement();
		                newad.setAdvertisement_Image("images/"+trueFileName);
		                newad.setAdvertisement_Title(title);
		                newad.setSeller_id(Integer.parseInt(id));
		                boolean b = adServiceImp.addAd(newad);
		               if (b) {
           			    String message = "success";
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            } 
           		        } 
           		        
           		} else {
           			    String message = "false_exception";  
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            }//if 
           		        } //finally
           		}//else
         		 }//图片格式正确，执行regist操作
         		 else{//图片格式不正确
         			    String message = "false_format_not_correct";  
	           		        try{
	           		        out = res.getWriter();
	           		        out.write(message);
	           		        out.flush();
	           		        out.close();
	           		        }catch (IOException e) {  
	           		            e.printStackTrace();  
	           		        } finally {  
	           		            if (out != null) {  
	           		                out.close();  
	           		            }//if 
	           		        } //finally
         		 }//图片格式不正确
         	 }//type!=null
         	 else{//type==null
         		 String message = "false_type_null";  
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            }//if 
           		        } //finally
         	 }//type!==null
         }
         else{//Logo.getSize()>2000000
         	String message = "false_size_too_big";  
    		        try{
    		        out = res.getWriter();
    		        out.write(message);
    		        out.flush();
    		        out.close();
    		        }catch (IOException e) {  
    		            e.printStackTrace(); 
    		        } finally {  
    		            if (out != null) {  
    		                out.close();  
    		            }//if 
    		        } //finally
         }//Logo.getSize()>2000000
	}
	
	
	}



@RequestMapping("editAd")
@ResponseBody
	public void aditAd(HttpServletRequest req, HttpServletResponse res, String id, String title, MultipartFile image) {
	
	res.setCharacterEncoding("UTF-8"); 
    res.setContentType("text/json");
    PrintWriter out =null;
    advertisement ad =  adServiceImp.getAdByAdID(id);
	if(image==null){//未上传图片
		ad.setAdvertisement_Title(title);
		boolean b = adServiceImp.editAdd(ad);
		String message =null;
		if(b){
		message = "success";
		}else{
		message = "system_exception";
		}
		        try{
		        out = res.getWriter();
		        out.write(message);
		        out.flush();
		        out.close();
		        }catch (IOException e) {  
		            e.printStackTrace();  
		        } finally {  
		            if (out != null) {  
		                out.close();  
		            } 
		        } 
	}else{//上传了图片
		    String path = null;
	        String type = null;
	        String fileName = image.getOriginalFilename();
	        type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
		  if (image.getSize()<2000000) {
         	 type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
         	 if (type!=null) {
         		 if ("PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {//图片格式正确，执行修改广告操作
         			    String realPath=req.getSession().getServletContext().getRealPath("/")+"/";
         			    String oldimage = ad.getAdvertisement_Image();
         			    File f = new File(realPath+oldimage);
         	            f.delete();
		                String trueFileName=oldimage.substring(0, oldimage.indexOf("."))+"."+type;
		                path=realPath+trueFileName;
		                try{
		                	image.transferTo(new File(path));}
		                catch (IOException e) {  
	       		            e.printStackTrace();  
	       		        } finally {  
	       		        } 
		                ad.setAdvertisement_Title(title);
		                ad.setAdvertisement_Image(trueFileName);
		                boolean b = adServiceImp.editAdd(ad);
		               if (b) {
           			    String message = "success";
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            } 
           		        } 
           		        
           		} else {
           			    String message = "false_exception";  
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            }//if 
           		        } //finally
           		}//else
         		 }//图片格式正确，执行regist操作
         		 else{//图片格式不正确
         			    String message = "false_format_not_correct";  
	           		        try{
	           		        out = res.getWriter();
	           		        out.write(message);
	           		        out.flush();
	           		        out.close();
	           		        }catch (IOException e) {  
	           		            e.printStackTrace();  
	           		        } finally {  
	           		            if (out != null) {  
	           		                out.close();  
	           		            }//if 
	           		        } //finally
         		 }//图片格式不正确
         	 }//type!=null
         	 else{//type==null
         		 String message = "false_type_null";  
           		        try{
           		        out = res.getWriter();
           		        out.write(message);
           		        out.flush();
           		        out.close();
           		        }catch (IOException e) {  
           		            e.printStackTrace();  
           		        } finally {  
           		            if (out != null) {  
           		                out.close();  
           		            }//if 
           		        } //finally
         	 }//type!==null
         }
         else{//Logo.getSize()>2000000
         	String message = "false_size_too_big";  
    		        try{
    		        out = res.getWriter();
    		        out.write(message);
    		        out.flush();
    		        out.close();
    		        }catch (IOException e) {  
    		            e.printStackTrace();  
    		        } finally {  
    		            if (out != null) {  
    		                out.close();  
    		            }//if 
    		        } //finally
         }//Logo.getSize()>2000000
	}
	
	
	}


@RequestMapping("deleteAd")
@ResponseBody
	public void deleteAd(HttpServletRequest req, HttpServletResponse res, String id, String sellerid) {
    advertisement ad =  adServiceImp.getAdByAdID(id);
    String realPath=req.getSession().getServletContext().getRealPath("/")+"/";
	String oldimage = ad.getAdvertisement_Image();
	File f = new File(realPath+oldimage);
    f.delete();
    boolean b = adServiceImp.deleteAdd(id);
	List<advertisement> advertisement_list = adServiceImp.getAdBySellerID(sellerid);
	JSONArray json = JSONArray.fromObject(advertisement_list);
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
