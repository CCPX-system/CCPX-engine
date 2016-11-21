package service;


public interface PlatformService {

	public Boolean removeExchange(String request_id, String user_from);
	public Boolean declineExchange(String request_id, String user_to);
	public Boolean removeOffer(String offer_id, String user_id);
	public Boolean acceptRequest(Integer request_id);
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerTo);
	
}
