package service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.PlatformDao;


@Service("PlatformServiceImp")
public class PlatformServiceImp implements PlatformService{

	@Resource(name = "PlatformDaoImp")
	private PlatformDao PlatformDaoImp;
	
	@Override
	public Boolean removeExchange(String request_id, String user_from)
	{
		Boolean flag = PlatformDaoImp.removeExchange(request_id, user_from);		
		System.out.println("flag: " + flag);
		return flag;
	}
	
	@Override
	public Boolean declineExchange(String request_id, String user_to)
	{
		Boolean flag = PlatformDaoImp.declineExchange(request_id, user_to);		
		System.out.println("flag: " + flag);
		return flag;		
	}
	
	@Override
	public Boolean removeOffer(String offer_id, String user_id)
	{
		Boolean flag = PlatformDaoImp.removeOffer(offer_id, user_id);		
		System.out.println("flag: " + flag);
		return flag;	
	}
	
	@Override
	public Boolean acceptRequest(Integer request_id)
	{
		Boolean flag = PlatformDaoImp.acceptRequest(request_id);
		System.out.println("flag: " + flag);
		return flag;	
	}
	
	@Override
	public List<Request> showLatestTransaction(Integer sellerFrom, Integer sellerTo)
	{
		List<Request> requests = PlatformDaoImp.showLatestTransaction(sellerFrom, sellerTo);		
		return requests;
	}
}
