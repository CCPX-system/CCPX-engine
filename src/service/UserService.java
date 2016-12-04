package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AllRecords;
import model.Notification;
import model.Offer;
//import model.Record;
import model.Request;
import model.Response;
import model.User;
import model.user_to_seller;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import utils.MD5Util;
import dao.OfferDaoImpl;
import dao.PlatformDaoImp;
import dao.RequestDaoImpl;
//import dao.RecordDaoImpl;
import dao.UserDaoImpl;
import dao.User_to_SellerDaoImpl;

@Controller
@RequestMapping(value = "/user")
public class UserService {

	public boolean check_token(int u_id, String u_token,
			HttpServletResponse response) throws IOException, SQLException {
		UserDaoImpl impl = new UserDaoImpl();
		if (!impl.token_auth(u_id, u_token)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			Response rs = new Response(1, "authorization failed", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
			out.close();
			return false;
			// response.sendError(401);
		}
		return true;
	}

	public String filter(String input) {
		if (input == null) {
			return "";
		}
		input = input.replaceAll("'", "");
		input = input.replaceAll("\\\\", "");
		input = input.replaceAll(";", "");
		input = input.replaceAll("<", "");
		input = input.replaceAll(">", "");
		input = input.replaceAll("%", "");
		input = input.replaceAll("#", "");
		return input;
	}

	//gong changjin
	public boolean checkPoints(int u_id, int seller_id, int points_from,
			HttpServletResponse response) throws SQLException, IOException {
		User_to_SellerDaoImpl impl = new User_to_SellerDaoImpl();
		int usablepoints = impl.pointsUsable(u_id, seller_id);
		if (usablepoints < points_from) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			Response rs = new Response(2, "no enough points", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
			out.close();
			return false;
		}
		return true;
	}

	// gong changjin
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public void register(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String name = request.getParameter("u_name");
		String password = request.getParameter("u_pw_hash");
		String fullname = request.getParameter("u_fullname");
		String email = request.getParameter("u_email");
		String wechat = request.getParameter("u_wechat_id");
		User user = new User();
		// user.setId(UserCommonUtil.getcount()+1);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setFullname(fullname);
		user.setWechatid(wechat);
		user.setToken(MD5Util.MD5(name + password));
		UserDaoImpl impl = new UserDaoImpl();
		String status = impl.add(user);
		PrintWriter out = response.getWriter();

		if (status == "ok") {
			user = impl.findByName(name);
			Response rs = new Response(0, "", user);
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		} else if (status == "error") {
			Response rs = new Response(5, "username repeated", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		} else {
			Response rs = new Response(6, "internal server error", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		}
		out.close();

	}

	// gong changjin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("u_name");
		String password = request.getParameter("u_pw_hash");
		UserDaoImpl impl = new UserDaoImpl();
		int flag = impl.checklogin(name, password);
		if (flag == 0) {
			Response rs = new Response(5,
					"username or password is not correct", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);

		} else {
			User user = impl.findById(flag);
			Response rs = new Response(0, "", user);
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		}

		out.close();
		return;
	}

	// gong changjin
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	public void modify(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.valueOf(request.getParameter("u_id"));
		String token = request.getParameter("u_token");
		String name = request.getParameter("u_name");
		String fullname = request.getParameter("u_full_name");
		String email = request.getParameter("u_email_address");
		String wechat = request.getParameter("u_wechat_id");
		if(!check_token(id, token, response))
			return;
		User user = new User();
		user.setId(id);
		user.setToken(token);
		user.setName(name);
		user.setEmail(email);
		user.setFullname(fullname);
		user.setWechatid(wechat);
		UserDaoImpl impl = new UserDaoImpl();
		impl.update(user);
		PrintWriter out = response.getWriter();
		Response res = new Response(0, "", user);
		JSONObject json = JSONObject.fromObject(res);
		out.print(json);
		out.close();
	}

	// by zhuyifan
	@RequestMapping(value = "/all_records", method = RequestMethod.GET)
	@ResponseBody
	public void allRecords(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.valueOf(request.getParameter("u_id"));
		String token = filter(request.getParameter("u_token"));
		if(!check_token(id, token, response))
			return;
		UserDaoImpl impl = new UserDaoImpl();
		ArrayList<Request> requestList = impl.findRequests(id, "");
		ArrayList<Offer> offerList = impl.findOffers(id, "");
		AllRecords resultList = new AllRecords(requestList,offerList);

		PrintWriter out = response.getWriter();
		JSONObject result = JSONObject.fromObject(resultList);
		Response rs = new Response(0, "", result);
		JSONObject json = JSONObject.fromObject(rs);
		out.print(json);
	}

	// by zhuyifan
	@RequestMapping(value = "/read_notification", method = RequestMethod.GET)
	@ResponseBody
	public void readNotification(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.valueOf(request.getParameter("u_id"));
		String token = filter(request.getParameter("u_token"));
		if(!check_token(id, token, response))
			return;
		UserDaoImpl impl = new UserDaoImpl();
		ArrayList<Notification> notificationList = impl.findNotifications(id,
				"");

		PrintWriter out = response.getWriter();
		JSONArray result = JSONArray.fromObject(notificationList);
		Response rs = new Response(0, "", result);
		JSONObject json = JSONObject.fromObject(rs);
		out.print(json);
	}

	// by zhuyifan
	@RequestMapping(value = "/seen_notification", method = RequestMethod.POST)
	@ResponseBody
	public void seenNotification(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.valueOf(request.getParameter("u_id"));
		String token = filter(request.getParameter("u_token"));
		int nId = Integer.valueOf(request.getParameter("n_id"));
		if(!check_token(id, token, response))
			return;
		UserDaoImpl impl = new UserDaoImpl();
		impl.setNotifications(nId, "1");

		PrintWriter out = response.getWriter();
		Response rs = new Response(0, "", "operation succeed");
		JSONObject json = JSONObject.fromObject(rs);
		out.print(json);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public void test(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		Response rs = new Response(0, "test ok", new Object());
		JSONObject json = JSONObject.fromObject(rs);
		out.print(json.toString());
		out.close();
	}

	 @RequestMapping(value = "/getReference", method = RequestMethod.GET)  
	 @ResponseBody
	 public void getReference(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
    	 response.setCharacterEncoding("UTF-8"); 
         response.setContentType("text/json");
         PrintWriter out = response.getWriter();
         JSONObject json = new JSONObject();
         
        	 int seller_from=Integer.parseInt(request.getParameter("seller_from"));
             int seller_to =Integer.parseInt(request.getParameter("seller_to"));
             PlatformDaoImp platformDaoImp=new PlatformDaoImp();
             List<Request>requests=new ArrayList<Request>();
             
             requests=platformDaoImp.showLatestTransaction(seller_from, seller_to);
             
             JSONArray jsonArr = new JSONArray();//json格式的数组  
             JSONObject jsonObjArr = new JSONObject(); 
             Request request1=new Request();
              for(int i=0;i<requests.size();i++){
            	  request1=requests.get(i);
            	// System.out.println(""+record.getR_id()+record.getU_id_from()+record.getU_id_to()+record.getSeller_id_from()+record.getSeller_id_to());
            	  jsonObjArr=new JSONObject(); 
            	  jsonObjArr.put("r_id", request1.getRid());
            	  jsonObjArr.put("user_from",request1.getUserFrom());
            	  jsonObjArr.put("user_to",	 request1.getUserTo());
            	  jsonObjArr.put("seller_from",	 request1.getSellerFrom());
            	  jsonObjArr.put("seller_to",	 request1.getSellerTo());
            	  jsonObjArr.put("points_from",	 request1.getPointsFrom());
            	  jsonObjArr.put("ponits_to",	 request1.getPointsTo());
            	  jsonObjArr.put("status",	 request1.getStatus());
            	  jsonArr.add(jsonObjArr);   //??
            	  jsonObjArr=null;
              }
          Response response2=new Response(0,"",jsonArr);
          json=JSONObject.fromObject(response2);
          out.print(json);
          System.out.println(json.toString());
          out.close();
    }
    
    @RequestMapping(value = "/searchExchangeOffer", method = RequestMethod.GET)  
    @ResponseBody
    public void searchExchangeOffer(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 response.setCharacterEncoding("UTF-8"); 
         response.setContentType("text/json");
         int size=request.getParameterMap().size();
         PlatformDaoImp platformDaoImp=new PlatformDaoImp();
         List<Offer>offers=new ArrayList<Offer>();
         PrintWriter out = response.getWriter();
         JSONObject json=new JSONObject();
          int seller_from=Integer.parseInt(request.getParameter("seller_from"));
          int seller_to =Integer.parseInt(request.getParameter("seller_to"));
          int points_from=Integer.parseInt(request.getParameter("points_from"));
          int points_to_min=Integer.parseInt(request.getParameter("points_to_min"));
         offers=platformDaoImp.showRecommendationList(seller_from, seller_to,points_from,points_to_min);
				
             JSONArray jsonArr = new JSONArray();//json格式的数组  
             JSONObject jsonObjArr = new JSONObject(); 
             //JSONObject json1 = new JSONObject();
             //System.out.println(offers.size());
             Offer offer=new Offer();         
              for(int i=0;i<offers.size();i++){
            	  offer=offers.get(i);
            	  jsonObjArr=new JSONObject(); 
            	  jsonObjArr.put("offer_id",offer.getOffer_id());
            	  jsonObjArr.put("user_id",offer.getUser_id());
            	  jsonObjArr.put("seller_from",	 offer.getSeller_from());
            	  jsonObjArr.put("seller_to",	 offer.getSeller_to());
            	  jsonObjArr.put("points_from",	  offer.getPoints_from());
            	  jsonObjArr.put("ponits_to_min",	 offer.getPoints_to_min());
            	  jsonObjArr.put("status",	offer.getStatus());
            	  jsonArr.add(jsonObjArr);   
            	  jsonObjArr=null;
              }
              
              //json1.put("exchange_offer", jsonArr);
          Response response2=new Response(0,"",jsonArr);
          json=JSONObject.fromObject(response2);
          out.print(json);
          System.out.println(json.toString());
          out.close();
    }
	@RequestMapping(value = "/makingoffer", method = RequestMethod.GET)
	@ResponseBody
	public void makingOffer(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		OfferDaoImpl offerDaoImpl = new OfferDaoImpl();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime = dateFormat.format(new Date());
			int user_id = Integer.parseInt(request.getParameter("u_id"));
			String token = request.getParameter("u_token");
			if(!check_token(user_id, token, response))
				return;
			int seller_from = Integer.parseInt(request.getParameter("seller_from"));
			int seller_to = Integer.parseInt(request.getParameter("seller_to"));
			int points_from = Integer.parseInt(request
					.getParameter("points_from"));
			int points_to_min = Integer.parseInt(request
					.getParameter("points_to_min"));
			//首先判断可用积分是否足够本次offer使用
			if(!checkPoints(user_id, seller_from, points_from, response))
				return;
			System.out.println(user_id);
			String status = "OPEN";
			Offer offer1 = new Offer(user_id, seller_from, seller_to,
					points_from, points_to_min, status, updateTime);
			System.out.println(offerDaoImpl.making_an_offer(offer1));
			User_to_SellerDaoImpl impl = new User_to_SellerDaoImpl();
			impl.lockPoints(user_id, seller_from, points_from);
			
		PrintWriter out = response.getWriter();

		JSONObject json = new JSONObject();

		JSONObject json1 = new JSONObject();
		// System.out.println(offers.size());

		json.put("errno", 0);
		json.put("err", "");
		json1.put("status", "OK");
		json.put("rsm", json1);
		out.print(json);
		System.out.println(json.toString());
		out.close();
	}

	// gong changjin
	@RequestMapping(value = "/canceloffer", method = RequestMethod.GET)
	@ResponseBody
	public void cancelOffer(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.parseInt(request.getParameter("u_id"));
		String token = request.getParameter("u_token");
		int offer_id = Integer.parseInt(request.getParameter("offer_id"));
		if(!check_token(id, token, response))
			return;
		OfferDaoImpl impl = new OfferDaoImpl();
		String status = impl.cancelOffers(offer_id, id);
		PrintWriter out = response.getWriter();
		if (status == "ok") {
			JSONObject js = new JSONObject();
			js.put("status", "ok");
			Response rs = new Response(0, "", js);
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		} else if (status == "error") {
			Response rs = new Response(6, "internal server error", new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		}
		out.close();
	}

	// gong changjin
	@RequestMapping(value = "/view_points", method = RequestMethod.GET)
	@ResponseBody
	public void viewPoints(HttpServletRequest request,
			HttpServletResponse response) throws IOException, SQLException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		int id = Integer.parseInt(request.getParameter("u_id"));
		String token = request.getParameter("u_token");
		if(!check_token(id, token, response))
			return;
		User_to_SellerDaoImpl impl = new User_to_SellerDaoImpl();
		ArrayList<user_to_seller> list = impl.queryPoints(id);
		PrintWriter out = response.getWriter();
		JSONArray result = JSONArray.fromObject(list);
		Response rs = new Response(0, "", result);
		JSONObject json = JSONObject.fromObject(rs);
		out.print(json);
		out.close();
	}

	// zhuyifan
	@RequestMapping(value = "/myprofile", method = RequestMethod.GET)
	@ResponseBody
	public void my_profile(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("u_id");
		String token = request.getParameter("u_token");
		UserDaoImpl impl = new UserDaoImpl();
		User one = impl.get_profile(id, token);
		if (one == null) {
			Response rs = new Response(5, "user_id or u_token is not matched",
					new Object());
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);

		} else {
			Response rs = new Response(0, "", one);
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		}

		out.close();
		return;
	}
		
		// zhuyifan
		@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
		@ResponseBody
		public void user_profile(HttpServletRequest request, HttpServletResponse response)
				throws SQLException, IOException {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			PrintWriter out = response.getWriter();
			String id = request.getParameter("u_id");
			//String token = request.getParameter("u_token");
			UserDaoImpl impl = new UserDaoImpl();
			User one = impl.get_profile(id, "");
			if (one == null) {
				Response rs = new Response(6,"user is not exist", new Object());
				JSONObject json = JSONObject.fromObject(rs);
				out.print(json);
			} else {
				Response rs = new Response(0, "", one);
				JSONObject json = JSONObject.fromObject(rs);
				out.print(json);
			}

			out.close();
			return;
		}
		
		// zhuyifan
		@RequestMapping(value = "/exchange_streaming", method = RequestMethod.GET)
		@ResponseBody
		public void exchange_streaming(HttpServletRequest request,
				HttpServletResponse response) throws IOException, SQLException {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			UserDaoImpl impl = new UserDaoImpl();
			ArrayList<Request> requestList = impl.findRequests();

			PrintWriter out = response.getWriter();
			JSONArray result = JSONArray.fromObject(requestList);
			Response rs = new Response(0, "", result);
			JSONObject json = JSONObject.fromObject(rs);
			out.print(json);
		}
		
		@RequestMapping(value = "/makeRequest", method = RequestMethod.POST)  
	    @ResponseBody
	    public void makeRequest(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
	    	response.setCharacterEncoding("UTF-8"); 
	        response.setContentType("text/json");
	        PrintWriter out = response.getWriter();
	        JSONObject json = new JSONObject();
	        JSONObject json1 = new JSONObject();
	        Request request1=null;
	        HashMap<String, String[]>mp=(HashMap<String, String[]>) request.getParameterMap();
	        if (mp.containsKey("offer_from")&&mp.containsKey("offer_to")) {
	       	    int offer_from=Integer.parseInt(request.getParameter("offer_from"));
	       	    int offer_to=Integer.parseInt(request.getParameter("offer_to"));
	       		 try {
	       			request1=new RequestDaoImpl().makeRequestByOfferId(offer_from, offer_to);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	       	 }
	       	 else if(mp.containsKey("user_from")&&mp.containsKey("offer_to")) {
	       		 int user_from=Integer.parseInt(request.getParameter("user_from"));
	             int offer_to =Integer.parseInt(request.getParameter("offer_to"));
	       		 try {
	       			request1=new RequestDaoImpl().makeRequestByUserId(user_from, offer_to);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	       	 }else {
	       		Response response3=new Response(4,"wrong parameters",new Object());
	        	json=JSONObject.fromObject(response3);
	        	out.print(json);
	            out.close(); 
	        	return ;
			}
	        if (request1!=null) {
	        	//json1.put("status", "ok");
	        	json1.put("request_id", request1.getRid());
	        	Response response3=new Response(0,"",json1);
	        	json=JSONObject.fromObject(response3);
			}
	        else {
	        	json1.put("status", "error");
	        	Response response3=new Response(5,"makeRequest failed",json1);
	        	json=JSONObject.fromObject(response3);
			}
	        out.print(json);
	        out.close();      
	    }
	    @RequestMapping(value = "/rejectRequest", method = RequestMethod.POST)  
	    @ResponseBody
	    public void rejectRequest(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
	    	response.setCharacterEncoding("UTF-8"); 
	        response.setContentType("text/json");
	        PrintWriter out = response.getWriter();
	        JSONObject json = new JSONObject();
	        JSONObject json1 = new JSONObject();
	        int r_id=Integer.parseInt(request.getParameter("r_id"));
	        int user_to=Integer.parseInt(request.getParameter("user_to"));
	        boolean b=new RequestDaoImpl().rejectRequest(r_id, user_to);
	        if(b){
	        	json1.put("status", "ok");
	            Response response2=new Response(0,"",json1);
	            json=JSONObject.fromObject(response2);
	        }
	        else {
	        	json1.put("status", "error");
	            Response response2=new Response(5,"acceptRequest failed",json1);
	            json=JSONObject.fromObject(response2);
			} 
	        out.print(json);
	        out.close(); 
	    }
	    @RequestMapping(value = "/acceptRequest", method = RequestMethod.POST)  
	    @ResponseBody
	    public void acceptRequest(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
	    	response.setCharacterEncoding("UTF-8"); 
	        response.setContentType("text/json");
	        PrintWriter out = response.getWriter();
	        JSONObject json = new JSONObject();
	        JSONObject json1 = new JSONObject();
	        int r_id=Integer.parseInt(request.getParameter("r_id"));
	        int user_to=Integer.parseInt(request.getParameter("user_to"));
	        boolean b=new RequestDaoImpl().acceptRequest(r_id, user_to);
	        if(b){
	        	json1.put("status", "ok");
	            Response response2=new Response(0,"",json1);
	            json=JSONObject.fromObject(response2);
	        }
	        else {
	        	json1.put("status", "error");
	            Response response2=new Response(5,"acceptRequest failed",json1);
	            json=JSONObject.fromObject(response2);
			}   
	        out.print(json);
	        out.close(); 
			
	    }
}