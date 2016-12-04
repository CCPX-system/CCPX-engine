package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Notification;
import model.Offer;
import model.Request;
import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.PlatformDaoImp;
import service.NotificationWebSocket;
import service.PlatformService;
import utils.VerifyCode;

@Controller
public class PlatformController {

	Calendar c = java.util.Calendar.getInstance(); // yyyy年MM月dd日hh时mm分ss秒
	SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy.MM.dd");

	// static Logger log=Logger.getLogger(SellerManagementController.class);
	// static{
	// log.info("right");
	// }

	@Resource(name = "PlatformServiceImp")
	private PlatformService PlatformServiceImp;

	@RequestMapping("/removeExchange")

	public String removeExchange(HttpServletRequest req, Integer request_id, Integer user_from) {

		Boolean flag = PlatformServiceImp.removeExchange(request_id, user_from);
		System.out.println("flag: " + flag);
		return "index";
	}

	@RequestMapping("/declineExchange")

	public String declineExchange(HttpServletRequest req, Integer request_id, Integer user_to, Integer user_from) {

		Boolean flag = PlatformServiceImp.declineExchange(request_id, user_to, user_from);
		System.out.println("flag: " + flag);
		return "index";
	}

	@RequestMapping("/removeOffer")

	public String removeOffer(HttpServletRequest req, Integer offer_id, Integer user_id) {

		Boolean flag = PlatformServiceImp.removeOffer(offer_id, user_id);
		System.out.println("flag: " + flag);
		return "index";
	}

	@RequestMapping("/acceptRequest")
	public String acceptRequest(@RequestParam Integer request_id) {
		int requestID = request_id;
		Boolean flag = PlatformServiceImp.acceptRequest(requestID);
		System.out.println("flag: " + flag);
		return "index";
	}

	@RequestMapping("/exitTest")
	public String exit(HttpServletRequest req) {

		req.getSession().removeAttribute("user");

		return "index";
	}

	@RequestMapping("/verifyCodeTest")
	public void verifycode(HttpServletRequest request, HttpServletResponse response) throws IOException {

		VerifyCode vc = new VerifyCode();

		BufferedImage image = vc.getImage();// 获取一次性验证码图片

		// 该方法必须在getImage()方法之后来调用
		// System.out.println(vc.getText());//获取图片上的文本
		VerifyCode.output(image, response.getOutputStream());// 把图片写到指定流中

		// 把文本保存到session中，为验证做准备
		request.getSession().setAttribute("vCode", vc.getText());

	}

	@RequestMapping("/showLatestTransaction")
	public List<Request> showLatestTransaction(HttpServletRequest req, String sellerFrom, String sellerTo) {
		int from = Integer.valueOf(sellerFrom);
		int to = Integer.valueOf(sellerTo);
		List<Request> requests = PlatformServiceImp.showLatestTransaction(from, to);
		for (Request re : requests) {
			System.out.println(
					re.getRid() + " " + re.getSellerFrom() + " " + re.getSellerTo() + " " + re.getUpdateTime());
		}
		return requests;
	}

	public List<Offer> searchExchange(Integer sellerFrom, Integer sellerTo, Integer pointsFrom, Integer pointsToMin) {
		return new PlatformDaoImp().searchExcahnge(sellerFrom, sellerTo, pointsFrom, pointsToMin);
	}

	@RequestMapping("/showRecommendationList")
	public void showRecommendationList(HttpServletRequest req, HttpServletResponse resp, String seller_from,
			String seller_to, String points_from, String points_to_min) throws ServletException, IOException {

		try {
			int sellerfrom = Integer.valueOf(seller_from);
			int sellerto = Integer.valueOf(seller_to);
			int pointsfrom = Integer.valueOf(points_from);
			int pointstoMin = Integer.valueOf(points_to_min);
			List<Offer> list = PlatformServiceImp.showRecommendationList(sellerfrom, sellerto, pointsfrom, pointstoMin);
			req.getSession().setAttribute("list", list);

			RequestDispatcher dispatcher = req.getRequestDispatcher("/showRecommendationResultsList.jsp");
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			RequestDispatcher dispa = req.getRequestDispatcher("/wrongEmpty.jsp");
			dispa.forward(req, resp);

		}

	}

	private void CreateNotifications(Integer UserId, Integer Status, Integer ER_ID) {
		System.out.println(UserId + " " + Status);

		Boolean b = PlatformServiceImp.createNotification(UserId, Status, ER_ID);
		if (b) {
			System.out.println("NotifFlag: " + b + " Success!");
		} else {
			System.out.println("NotifFlag: " + b + " Failed!");
		}
	}

	// ------------NOTIFACTION TESTPAGE--------------
	@RequestMapping(value = "/create_notification", method = RequestMethod.GET)
	public @ResponseBody String CreateNotification(@RequestParam Integer UserId, Integer Status, Integer ER_ID) {
		System.out.print(UserId);
		String msg = null;
		Boolean flag = PlatformServiceImp.createNotification(UserId, Status, ER_ID);
		if (flag) {
			System.out.println("NotifFlag: " + flag + " Success!");
			msg = "success";
		} else {
			System.out.println("NotifFlag: " + flag + " Failed!");
			msg = "failed";
		}
		return msg;
	}

	@RequestMapping(value = "/notif_list_by_user_id", method = RequestMethod.GET)
	public @ResponseBody JSONArray NotifListsByUserId(@RequestParam Integer userId) {
		List<Notification> notiflitsList = PlatformServiceImp.NotifListsByUserId(userId);
		JSONArray json = JSONArray.fromObject(notiflitsList);
		System.out.print(json);
		return json;
	}

	@RequestMapping(value = "/set_unread_notif", method = RequestMethod.GET)
	public @ResponseBody boolean setNotifUnread(@RequestParam Integer notifID) {
		boolean notif = PlatformServiceImp.setNotifUnread(notifID);
		if (notif) {
			System.out.println("NotifFlag: " + notif + " Success!");
			return notif;
		} else {
			System.out.println("NotifFlag: " + notif + " Failed!");
			return false;
		}
	}

	@RequestMapping(value = "/notif_unread", method = RequestMethod.GET)
	public @ResponseBody int getNotifUnread(@RequestParam Integer userId) {
		int notif = PlatformServiceImp.getNotifUnread(userId);
		return notif;
	}

}
