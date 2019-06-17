$(document).ready(function() {
	var user_id = sessionStorage.getItem("user_id");
	sessionStorage.clear();
	sessionStorage.setItem("user_id", user_id);
	
	load_pic("bird");
	load_station("upload-up");
	load_station("upload-down");
	load_station("download-up");
	load_station("download-down");
	load_station("file-up");
	load_station("file-down");
	
	$.ajax({
		url: "api/station",
		type: "GET",
		success: res => {
			setStation(res);
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: "json"
	});
});

function setStation(res) {
	$("#upload-num").html(res.upload_time);
	$("#download-num").html(res.download_time);
	$("#file-num").html(res.file_num);
}

/**
 * 导航栏搜索框和中心搜索框回车事件
 */
function inputSearch(key) {
	if (event.keyCode == 13) {
		// 阻止搜索空字符串
		if (key == '')
			event.preventDefault();
		else {
			// 跳转到list.html并传值
			sessionStorage.setItem("key", key);
			location = "list.html";
		}
	}
}

function load_pic(id) {
	// 自动调整pic图片的大小
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $(".main").width();
	var img_width = img.width;
	var img_height = img.height;
	
	var rate = 1.0*main_width/img_width;
	img_height = img_height*rate;
	
	$("#"+id).css({"width":main_width, "height":img_height});
}

function load_station(id) {
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $(".main").width()*1.0/3.5;
	var img_width = img.width;
	var img_height = img.height;
	
	var rate = 1.0*main_width/img_width;
	img_height = img_height*rate;
	
	$("#"+id).css({"width":main_width, "height":img_height});
}