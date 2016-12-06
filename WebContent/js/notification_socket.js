function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
	return unescape(arr[2]);
	else
	return null;
}

//WEBSOCKET PREPARATION

var Chat = {};
Chat.socket = null;
var UserId = 2;//getCookie("u_id");
// connect()
Chat.connect = (function(host) {
	if ('WebSocket' in window) {
		Chat.socket = new WebSocket(host);
	} else if ('MozWebSocket' in window) {
		Chat.socket = new MozWebSocket(host);
	} else {
		console.log('Error: WebSocket is not supported by this browser.');
		return;
	}

	Chat.socket.onopen = function () {
		console.log('Info: WebSocket connection opened. your GID:'+UserId);
	};
	Chat.socket.onclose = function () {
		document.getElementById('chat').onkeydown = null;
		console.log('Info: WebSocket closed.');
	};
	Chat.socket.onmessage = function (message) {
		var split = message.data.split("|");
		var status = '';
		if(split[2] == 0){
			status = "YOUR EXCHANGE OFFER WAITING FOR RESPOND";
		}else if(split[2] == 1 || split[2] == 2){
			status = "YOUR EXCHANGE SUCCESFULLY DONE";
		}else if(split[2] == 3){
			status = "YOUR EXCHANGE OFFER HAS BEEN REMOVED";
		}else if(split[2] == 4){
			status = "YOUR EXCHANGE REQUEST HAS BEEN REMOVED";
		}else if(split[2] == 5){
			status = "YOUR EXCHANGE REQUEST HAS BEEN DECLINED";
		}else if(split[2] == 6){
			status = "YOUR EXCHANGE REQUEST IS PENDING";
		}else{
			status = "YOUR DECLINED EXCHANGE REQUEST";
		}
	
		if(split[0] == UserId){
			console.log(message.data);
			makeNotification("Trans.No: "+split[1]+" "+status); 
		}else if(split[1] == UserId){
			console.log("Your Message Send");
		}
		console.log(split[0]+" -> "+split[1]+" -> "+split[2]);
	};
});
// connect()
Chat.initialize = function() {
	if (window.location.protocol == 'http:') {
		Chat.connect('ws://' + window.location.host + '/ccpx/websocket/notification');
	} else {
		Chat.connect('wss://' + window.location.host + '/ccpx/websocket/notification');
	}
};

function makeNotification(message) {
	if(Notification.permission !== 'granted'){
		Notification.requestPermission();
	}
	var notif_success = document.createElement('audio');
	notif_success.setAttribute('src', 'js/success.mp3');
	console.log(window.location.host+'/SpringWebSocket/js/notif_icon.png');
	n = new Notification( "a new notification", {
		body: message, 
		icon : 'js/notif_icon.png'
	});
	notif_success.play();
	n.close();	
}

Chat.initialize();

