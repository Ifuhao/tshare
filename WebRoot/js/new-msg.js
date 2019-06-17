var socket;
$(document).ready(function() {
	$(".new-msg").attr("src", "/tshare/home/img/new_msg.png");
	$(".new-msg").addClass("alpha-0");
	
	$("#system-notice").find("img").attr("src", "/tshare/home/img/new_msg.png");
	$("#system-notice").find("img").addClass("alpha-0");
	
	$("#answer-notice").find("img").attr("src", "/tshare/home/img/new_msg.png");
	$("#answer-notice").find("img").addClass("alpha-0");
	
	$("#person-notice").find("img").attr("src", "/tshare/home/img/new_msg.png");
	$("#person-notice").find("img").addClass("alpha-0");
	
	$("#trade-notice").find("img").attr("src", "/tshare/home/img/new_msg.png");
	$("#trade-notice").find("img").addClass("alpha-0");
	
	$("#other-notice").find("img").attr("src", "/tshare/home/img/new_msg.png");
	$("#other-notice").find("img").addClass("alpha-0");
	
	var user_id = sessionStorage.getItem("user_id");
	socket = websocket(user_id);
	socket.onmessage = function(msg) {
		sessionStorage.setItem("new_msg", msg.data);
		new_msg();
	};
});

/**
 * websocket服务器发送了提醒消息
 */
function new_msg() {
	var msg = JSON.parse(sessionStorage.getItem("new_msg"));
	if(msg.system == 1 || msg.person != undefined || msg.trade != undefined || msg.other == 1) {
		$(".new-msg").removeClass("alpha-0");
	}
}

/**
 * 点击信封时，需要在四类消息上面分别显示是否有新消息
 */
function notice() {
	var msg = JSON.parse(sessionStorage.getItem("new_msg"));
	if(msg.system == 1) {
		$("#system-notice").find("img").removeClass("alpha-0");
	}
	if(msg.person != undefined) {
		$("#person-notice").find("img").removeClass("alpha-0");
	}
	if(msg.trade != undefined) {
		$("#trade-notice").find("img").removeClass("alpha-0");
	}
	if(msg.other == 1) {
		$("#other-notice").find("img").removeClass("alpha-0");
	}
}
