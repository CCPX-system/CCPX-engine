<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head><H1>CCPX Project</H1>
</head>

<body>
<form method="post" action="showRecommendationList" id="showRecommendationList">
						<h2>showRecommendation</h2>
						<ul class="form-list">
							<li>
								<label class="required" for="seller_from">SellerFrom<em>*</em></label>
								<div class="input-box">
									
								<input type="text" name="seller_from" id="seller_from" tabindex="1" autocomplete="off" class="input-text required-entry validate-sellerFrom" />
								</div>
								<div class="clear"></div>
							</li>
							<li>
								<label class="required" for="seller_to">SellerTo<em>*</em></label>
								<div class="input-box">
									<input type="text" name="seller_to"
					maxlength="16" id="seller_to" tabindex="2" class="input-text required-entry validate-sellerTo"/>
							
								</div>
								<div class="clear"></div>
							</li>	
							
							<li>
								<label class="required" for="points_from">pointsFrom<em>*</em></label>
								<div class="input-box">
									<input type="text" name="points_from"
					maxlength="16" id="points_from" tabindex="2" class="input-text required-entry validate-pointsFromm"/>
							
								</div>
								<div class="clear"></div>
							</li>	
							
							<li>
								<label class="required" for="points_to_min">pointsToMin<em>*</em></label>
								<div class="input-box">
									<input type="text" name="points_to_min"
					maxlength="16" id="points_to_min" tabindex="2" class="input-text required-entry validate-pointsToMin"/>
							
								</div>
								<div class="clear"></div>
							</li>						
						</ul>						
					</div>
					<div class="buttons-set">				
					<input type="submit" tabindex="5" class="colors-btn" class="colors-btn"  title="submit" value="提交" />

						
						<div class="clear"></div>
					</div>
            	</div>
            		</form>
   
            	
            	
            	
				<div class="clear"></div>
			</div>
		</div>
		<div class="clearfix"></div>

	</div>
</section>
</div>
</body>
</html>



