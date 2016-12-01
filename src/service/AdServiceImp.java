package service;

import java.util.List;

import javax.annotation.Resource;

import model.ad_present;
import model.advertisement;

import org.springframework.stereotype.Service;

import dao.AdDao;

@Service("adServiceImp")
public class AdServiceImp implements AdService {
	
	@Resource(name = "adDaoImp")
	private AdDao adDaoImp;
	
	@Override
	public List<ad_present> getAd(){
		return adDaoImp.getAd();
	}
	@Override
	public List<advertisement> getAdBySellerID(String id){
		return adDaoImp.getAdBySellerID(id);
	}
	@Override
	public int getNumberOfAdBySellerID(String id){
		return adDaoImp.getNumberOfAdBySellerID(id);
	}
	@Override
	public boolean addAd(advertisement newad){
		return adDaoImp.addAd(newad);
	}
	@Override
	public advertisement getAdByAdID(String id){
		return adDaoImp.getAdByAdID(id);
	}
	@Override
	public boolean editAdd(advertisement ad){
		return adDaoImp.editAdd(ad);
	}
	@Override
	public boolean deleteAdd(String id){
		return adDaoImp.deleteAdd(id);
	}
}
