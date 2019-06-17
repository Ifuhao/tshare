$(document).ready(function() {
	var user_id = sessionStorage.getItem("user_id");
	sessionStorage.clear();
	sessionStorage.setItem("user_id", user_id);
});
