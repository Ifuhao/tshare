$(document).ready(function() {
	// 向servlet请求用户信息
	
	$.ajax({
		url: "api/person",
		type: "GET",
		success: res => {
			show(res);
		},
		dataType: "json"
	});
});

function show() {
	
}