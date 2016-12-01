<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>
<head>
<script src="http://localhost:8080/ccpx/js/notification/jquery-1.8.2.min.js"></script>
<script src="http://localhost:8080/ccpx/js/notification/underscore.js"></script>
<script src="http://localhost:8080/ccpx/js/notification/common.min.js"></script>
<script src="http://localhost:8080/ccpx/js/notification/uikit.min.js"></script>
<script src="http://localhost:8080/ccpx/js/notification/uikit.js"></script>
<script src="http://localhost:8080/ccpx/js/notification/sweetalert.min.js"></script>

<link rel="stylesheet" href="http://localhost:8080/ccpx/css/notification/altair_main.min.css"/>
<link rel="stylesheet" href="http://localhost:8080/ccpx/css/notification/uikit.min.css"/>
<link rel="stylesheet" href="http://localhost:8080/ccpx/css/notification/uikit.css"/>
<link rel="stylesheet" href="http://localhost:8080/ccpx/css/notification/sweetalert.css"/>

<c:url var="home" value="/" scope="request" />

</head>
<body>
<div id="page_content">
	<div id="page_content_inner" style="padding: 10px 24px 100px;">
	<div class="md-card"><div class="md-card-content">
	
	<div class="uk-width-medium-1">
		<H1 class="uk-text-large"><center><strong>NOTIFICATION SERVICES<strong></center></H1>
		
		<div class="uk-width-medium-1" style="margin-bottom:+50px">
			<div class="uk-grid" data-uk-grid-margin>
				<div class="uk-width-medium-1-2">
					<div class="md-input-wrapper">
						<input id="uid" class="md-input" placeholder="Type User Id..." style="min-height:40px;background: url(<?php echo base_url()?>/assets/img/md-images/search_icon.png) no-repeat 10px center;border: solid 1px #ccc;padding: 9px 10px 9px 10px; -webkit-transition: all .5s;-moz-transition: all .5s;transition: all .5s;"></input>
						<span class="md-input-bar"></span>
					</div>
				</div>
				<div class="uk-width-medium-1-2">
					<div class="md-input-wrapper">
						<input id="er_id" class="md-input" placeholder="Type Offer / Requests Id..." style="min-height:40px;background: url(<?php echo base_url()?>/assets/img/md-images/search_icon.png) no-repeat 10px center;border: solid 1px #ccc;padding: 9px 10px 9px 10px; -webkit-transition: all .5s;-moz-transition: all .5s;transition: all .5s;"></input>
						<span class="md-input-bar"></span>
					</div>
				</div>
				<div class="uk-width-medium-1-2">
					<div class="md-input-wrapper">
						<input id="stid" class="md-input" placeholder="Type Status..." style="min-height:40px;background: url(<?php echo base_url()?>/assets/img/md-images/search_icon.png) no-repeat 10px center;border: solid 1px #ccc;padding: 9px 10px 9px 10px; -webkit-transition: all .5s;-moz-transition: all .5s;transition: all .5s;"></input>
						<span class="md-input-bar"></span>
					</div>
				</div>
				<div class="uk-width-medium-1-2"> 
					<button id="setNotif" title="Create Notificaiton" class="uk-width-medium-1 md-btn md-btn-success">Create Notification</button>
				</div>
		</div>
		<br/><hr/><br/>
		<div class="uk-width-medium-1">
			<div class="uk-grid" data-uk-grid-margin>
				<div class="uk-width-medium-1-2">
					<div class="md-input-wrapper">
						<input id="uid_text" class="md-input" placeholder="Enter User Id..." style="min-height:40px;background: url(<?php echo base_url()?>/assets/img/md-images/search_icon.png) no-repeat 10px center;border: solid 1px #ccc;padding: 9px 10px 9px 10px; -webkit-transition: all .5s;-moz-transition: all .5s;transition: all .5s;"></input>
						<span class="md-input-bar"></span>
					</div>
				</div>
				<div class="uk-width-medium-1-2">
					<button id="getNotification" title="get notificaiton" class="uk-width-medium-1 md-btn md-btn-primary">Generate Notification</button>
				</div>
			</div>
		</div>
		
		<div id="ctc_notif" style="display:none">
			<span class="uk-text">You have <span id="notif_count" class="uk-badge uk-badge-warning"></span> new notifications</span>
		</div>
		
		<div class="uk-width-medium-1" style="padding-top:+40px">
			<ul id="notification_list_content" class="md-list md-list-outside" data-uk-scrollspy-nav="{smoothscroll:true}"></ul>
		</div>
	</div>
	
	</div></div>
	</div>
</div>

<script type="text/javascript">
	var getUrl = window.location;
	var baseUrl = getUrl .protocol + "//" + getUrl.host  + "/" + getUrl.pathname.split('/')[1]+"/"+getUrl.pathname.split('/')[2];
	
	var UserId = null;
	
    jQuery(document).ready(function($) {
		
		$("#uid_text").focus();
			
		
		$("#getNotification").on('click',function (){
			UserId = $("#uid_text").val();
			notifReload();
			setInterval(notifReload, 10000); //Runs every 10 second
		});
		
		$("#uid_text").keydown(function(event){
	    	$("#uid_text").focus();
			var text=$("#uid_text").val().trim();
	        if(event.keyCode == 13){//ENTER
	        	UserId = $("#uid_text").val();
	        	notifReload();
	        }
	    });
	    
	    $("#setNotif").on('click',function (){
	    	var uid = $("#uid").val(); 
	    	var erid = $("#er_id").val();
	    	var stid = $("#stid").val();
	    	$.ajax({
		        url: 'create_notification/',
		        data: {"UserId" : uid, "Status":stid, "ER_ID":erid},
		        success: function(data) {
		        	console.log("Create: "+data);
		        }
		    });
	    });
			
	});
	
	
	    
    function generate_notificaion_list(userId){
    	
    	$.ajax({
	        url: 'notif_list_by_user_id/',
	        data: ({userId : userId}),
	        success: function(data) {      
	        	$("#notification_list_content").empty();    	
	          	if (_.isEmpty(data)==false){
                    $.each(data, function( key, val ){
                    	//console.log(data);
                    	var bgcolor = null;
                    	var btnclr  = null;
                    	var badge_clr = null;
                    	var status_msg = null;
                    	
                    	if(val.seen == 0){
                    		bgcolor = 'md-list-item-active';
                    	}
						
                   	
                    	if(val.status == 0){
                    		badge_clr = "uk-badge-primary"; btnclr = "md-btn-flat-primary"; status_msg= "OFFER_OPEN";
                    	}else if(val.status == 1){
                    		badge_clr = "uk-badge-success"; btnclr = "md-btn-flat-success"; status_msg= "CLOSE_OFFER";
                    	}else if(val.status == 2){
                    		badge_clr = "uk-badge-success"; btnclr = "md-btn-flat-success"; status_msg= "CLOSE_REQUEST";
                    	}else if(val.status == 3){
                    		badge_clr = "uk-badge-warning"; btnclr = "md-btn-flat-warning"; status_msg= "REMOVE_OFFER";
                    	}else if(val.status == 4){
                    		badge_clr = "uk-badge-warning"; btnclr = "md-btn-flat-warning"; status_msg= "REMOVE_REQUEST";
                    	}else if(val.status == 5){
                    		badge_clr = "uk-badge-danger"; btnclr = "md-btn-flat-danger"; status_msg= "DECLINE_REQUEST";
                    	}else if(val.status == 6){
                    		badge_clr = "uk-badge-warning"; btnclr = "md-btn-flat-warning"; status_msg= "REQUEST_PENDING";
                    	}else{
                    		badge_clr = null;
                    	}
                    	
                    	$("#notification_list_content").append(
                    		'<li id="'+val.notifiId+'" content="'+status_msg+'" class="'+bgcolor+'">'+
                    			'<div class="uk-grid" data-uk-grid-margin>'+
									'<div class="uk-width-medium-1-2">'+
			                    		'<b>'+val.userId+'</b> | Trans.No: '+val.exchId+
			                    		'. <i><span class="uk-badge '+badge_clr+'">'+status_msg+'</span></i><br/> '+
			                    		'<i>'+val.content+'</i> on <i>'+val.notiDate+'</i>'+
	                    		    '</div>'+
	                    		    '<div class="uk-width-medium-1-2" align="right">'+
	                    		    	'<button title="'+status_msg+'" class="md-btn '+btnclr+' md-btn-flat">'+status_msg+'</button>'+
	                    		    '</div>'+
	                    		 '</div>'+  		
                    		'</li>'
                    	);
                    });
                }else{
                	//console.log("data empty");
                	swal("EROR DATA NOT FOUND ");
                }
	        },
	        error:function(data){
	        	swal("EROR DATA NOT FOUND ");
	        }
     	});
    }
    
    
    
    function notifReload() {
    	generate_notificaion_list(UserId);
    	getNotifUnRead(UserId);
    }
    
	
	function getNotifUnRead(userId){
		$.ajax({
	        url: 'notif_unread/',
	        data: ({userId : userId}),
	        success: function(data) {
	        	$("#ctc_notif").show();
	        	$("#notif_count").text(data);
	        },error:function(data){
	        	//alert("EROR DATA NOT FOUND");
	        	swal("EROR DATA NOT FOUND ");
	        	$("#ctc_notif").hide();
	        }
	  	});
	}
	
	
	$("#notification_list_content").on('click','li',function (){
		console.log($(this).attr("id")+" "+$(this).attr("content"));
		return false;
	});

	
    
</script>

</body>
</html>