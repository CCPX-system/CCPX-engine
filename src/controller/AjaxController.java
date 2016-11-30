package controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import service.SellerManagementService;
//dash
@Controller()
public class AjaxController {
	@Resource(name = "sellerManagementServiceImp")
	private SellerManagementService sellerManagementServiceImp;

		
}
