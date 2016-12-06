package dao;

import java.util.List;

import model.ExchangeRecord;

public interface ViewExchangePointDao {
	public List<ExchangeRecord> getLatExRec(int seller_id, int record_num);
	public String getUserNameByUserId(int userId);
}
