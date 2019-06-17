$(document).ready(function() {
	// 连接websocket
	var user_id = sessionStorage.getItem("user_id");
	sessionStorage.clear();
	sessionStorage.setItem("user_id", user_id);
});

function search() {
	location = "sale/search.html";
}

function publish_sale() {
	location = "sale/upload.html";
}

function publish_seek() {
	location = "seek/upload.html";
}

function publish_wish() {
	location = "wish/upload.html";
}