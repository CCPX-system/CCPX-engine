package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;


import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import model.AmountPoint;
import model.SellerStatusInfo;
import model.Seller_transferInfoBean;
import model.TransferRecord;
import model.industry_type;
import model.user_to_seller;

@Service("SellerTranserServiceImp")
public class SellerTranserServiceImp implements SellerTranserService {
	protected Logger logger = Logger.getLogger(getClass());
	private final static String URL = "http://tangmocd.cn/seller/v1.0.0/index.php?s=/home/index/points/";

	// 声明sessionFactory
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	private Session session;

	// 获取当前session的方法
	private Session getSession() {

		if (session == null) {
			session = sessionFactory.openSession();
		} else {
			session = sessionFactory.getCurrentSession();
		}
		return session;
	}

	/*
	 * 获取积分交易记录列表
	 */
	public List<Object> querypointrecord(int sellerid, String time1,
			String time2, int timelen) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒

		String hql = "select  Pointtransfer_Time as time ,POintTransfer_Points as points,PointTransfer_Type as type,user.u_name as username  "
				+ " from  pointTransfer "
				+ " join user on user.u_id=pointTransfer.User_id "
				+ " where seller_id= " + sellerid;

		if (("".equals(time1) || time1 == null)
				|| ("".equals(time2) || time2 == null)) {
			// 只要有一个为空，则不进行时间戳的拦截
			hql = hql + ";";
		} else {

			hql = hql + " and date(PointTransfer_Time) between \'" + time1
					+ "\' and \'" + time2 + "\' ;";
		}

		return getSession().createSQLQuery(hql).list();
	}

	/*
	 * 交换积分
	 */
	public SellerStatusInfo point(Seller_transferInfoBean transferbean) {

		String str = connect(transferbean.getName(),
				transferbean.getPassword(), transferbean.getPoints(),
				transferbean.getTrade_type(), transferbean.getSeller_id());
		// SellerStatusInfo
		// statusinfo=JSON.parseObject(str,SellerStatusInfo.class);
		SellerStatusInfo statusinfo = new SellerStatusInfo();
		
		// 更新积分到本地积分库中
		statusinfo.setStatus(0);
		// 插入记录
		if (statusinfo.getStatus() == 0
				&& ("0".equalsIgnoreCase(transferbean.getTrade_type()) || "1"
						.equalsIgnoreCase(transferbean.getTrade_type()))) {
			// 转移积分成功,这样才记录

			int points = transferbean.getPoints();

			if ("0".equalsIgnoreCase(transferbean.getTrade_type())) {
				// 积分转出，要进行积分的总数检验
				// 查询当前积分
				String hql = "select points from user_to_seller where U_ID="
						+ transferbean.getUserid() + " and SELLER_ID="
						+ transferbean.getSeller_id();
				List<Object> list = getSession().createSQLQuery(hql).list();

				if (list.size() > 0) {

					int amountpoints = (int) list.get(0);

					logger.warn("amountpoints" + amountpoints);

					if (amountpoints >= points) {
						// 没有问题则进行下面的更新
					} else {
						// 总积分数要大于外部积分，这部分是少于
						statusinfo.setStatus(400);
						statusinfo.setMsg("no enough points");
						return statusinfo;

					}

				} else {
					// 总积分数要大于外部积分，这部分是少于
					statusinfo.setStatus(401);
					statusinfo.setMsg("no enough points");
					return statusinfo;
				}

				points=-1*points;
			} else if ("1".equalsIgnoreCase(transferbean.getTrade_type())) {
				// 积分转入不需要进行积分验证

			}
			Transaction tx = null;
			try {

				session = getSession();
				tx = session.beginTransaction();
				String sql2 = "update  user_to_seller set points=points+"
						+ points
						+ " where U_ID=1 and SELLER_ID=5";
				int result2 = session.createSQLQuery(sql2).executeUpdate();
				logger.warn("sql3_result=" + result2);

				Session session = getSession();

				String sql = "insert into pointTransfer ( "
						+ " PointTransfer_Time,PointTransfer_Points,PointTransfer_Type,User_id,Seller_id,SellerPointSystem_Username "
						+ " ) " + " values " + " ( " + "	'"
						+ transferbean.getCreatetime() + "',"
						+ transferbean.getPoints() + ","
						+ transferbean.getTrade_type() + ","
						+ transferbean.getUserid() + ","
						+ transferbean.getSeller_id() + ",'"
						+ transferbean.getName() + "' " + "	);";

				Query customerinsert = session.createSQLQuery(sql);
				int result0 = customerinsert.executeUpdate();

				logger.warn("sql1+" + result0);
				tx.commit();
			} catch (HibernateException ex) {
				if (tx != null) {
					tx.rollback();
				}
			} finally {
				session.close();
			}

		}

		return statusinfo;
	}

	/*
	 * 添加成员列表，在这里注册并在外面的数据库中注册
	 */
	@SuppressWarnings("finally")
	@Override
	public int addMembership(int u_id, int seller_id, int points,
			int point_blocked) {
		Session session = getSession();
		// 检验是否是合法的用户，userid和selelrid 是否存在
		BigInteger utempresult = (BigInteger) session
				.createSQLQuery(
						"select count(*) from user where u_id=" + u_id + ";")
				.list().get(0);
		BigInteger sellerresult = (BigInteger) session
				.createSQLQuery(
						"select count(*) from seller where seller_id="
								+ seller_id + ";").list().get(0);
		if (!utempresult.equals(1) || !sellerresult.equals(1)) {
			// 用户无法找到用户和卖家，返回错误
			return 0;
		}
		int result = 0;
		String sql = "insert  into user_to_seller (U_ID,SELLER_ID,POINTS,POINTS_BLOCKED) values( "
				+ u_id
				+ ","
				+ seller_id
				+ ","
				+ points
				+ ","
				+ point_blocked
				+ ");";
		try {
			Query customerinsert = session.createSQLQuery(sql);

			result = customerinsert.executeUpdate();
		} catch (HibernateException ex) {

			result = 0;
		} finally {
			return result;
		}
	}

	@Override
	public AmountPoint questamountPoint(int sellerid, String time1, String time2)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		String hql = null;

		if ((time1 == null || "".equals(time1))
				|| (time2 == null || "".equals(time2))) {
			// 只要有一个为空，则不进行时间戳的拦截
			hql = "select a.income,b.outcome from "
					+ " (select sum(POintTransfer_Points) as income from pointTransfer "
					+ " where PointTransfer_Type=1 and seller_id="
					+ sellerid
					+ " ) a, "
					+ " (select sum(POintTransfer_Points) as outcome from pointTransfer "
					+ " where PointTransfer_Type=0 and seller_id=" + sellerid
					+ ") b " + " ;";
		} else {
			time1 = time1 + " 00:00:00";
			time2 = time2 + " 00:00:00";
			Date time1data = df.parse(time1);
			Date time2date = df.parse(time2);
			Timestamp timstamp1 = new Timestamp(time1data.getTime());
			Timestamp timstamp2 = new Timestamp(time2date.getTime());

			hql = "select a.income,b.outcome from "
					+ " (select sum(POintTransfer_Points) as income from pointTransfer "
					+ " where PointTransfer_Type=1 and seller_id="
					+ sellerid
					+ " and date(PointTransfer_Time) between \'"
					+ time1
					+ "\' and \'"
					+ time2
					+ "\' ) a, "
					+ " (select sum(POintTransfer_Points) as outcome from pointTransfer "
					+ " where PointTransfer_Type=0 and seller_id=" + sellerid
					+ " and date(PointTransfer_Time) between \'" + time1
					+ "\' and \'" + time2 + "\') b " + " ;";

		}

		List<Object> obj = getSession().createSQLQuery(hql).list();
		if (obj != null && obj.size() > 0) {
			Object[] result = (Object[]) obj.get(0);
			AmountPoint point = new AmountPoint();
			point.setIncome((BigDecimal) result[0]);
			point.setOutcome((BigDecimal) result[1]);

			return point;
		} else {
			return null;
		}

	}

	private static String getResponse(String requsetUrl, String content) {
		try {
			URL url = new URL(requsetUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoOutput(true); // 使用 URL 连接进行输出
			httpConn.setDoInput(true); // 使用 URL 连接进行输入
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setUseCaches(false); // 忽略缓存
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpConn.setRequestMethod("POST"); // 设置URL请求方法
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String readLine;
			StringBuffer responseSb = new StringBuffer();
			while ((readLine = responseReader.readLine()) != null) {
				responseSb.append(readLine);
			}
			return responseSb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}

	}

	private static String connect(String name, String password, int points,
			String tradetype, String seller_id) {
		StringBuilder param = new StringBuilder();
		param.append("name=" + name + "&");
		param.append("password=" + password + "&");
		param.append("trade_type=" + tradetype + "&");
		param.append("seller_id=" + seller_id + "&");
		param.append("points=" + points);

		return getResponse(URL, param.toString());
	}

}
