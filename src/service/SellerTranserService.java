package service;

import java.text.ParseException;
import java.util.List;

import model.AmountPoint;
import model.SellerStatusInfo;
import model.Seller_transferInfoBean;
import model.TransferRecord;

/*
 * @description  transfer point in and out
 * @group Seller function
 * @author:KEN
 * @datetime: 2016-11-02
 * */
public interface SellerTranserService {
		/*
		 * @description query point  from virtual seller point system
		 * @param name 
		 * @param password 
		 * @param seller_id 
		 * @param trade_type 0:transfer in,1:transfer out,2:query point
		 * @points points transfer in or out
		 * @return point seller have 
		 * */
		public SellerStatusInfo point(Seller_transferInfoBean transferbean);
		public List<Object> querypointrecord(int sellerid,String time1,String time2,int timelen) throws ParseException;
		public AmountPoint questamountPoint(int sellerid,String time1,String time2) throws ParseException;
		public int addMembership(int u_id,int seller_id,int points,int point_blocked);

}
