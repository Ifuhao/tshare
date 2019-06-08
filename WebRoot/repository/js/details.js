$(document).ready(function() {
	var index=sessionStorage.getItem('index')
	var data = JSON.parse(sessionStorage.getItem('res')).data[index]

	// 先生成预览图片
	$.ajax({
		url: "api/preview",
		type: "GET",
		data: {
			url: data.url
		},
		complete: () => {
			show(data);
		},
		timeout: 15000
	});
	
	// 等待动画
	$('.main').waitMe({
		effect: 'rotateplane',
		text:'正在加载...',
		bg: 'rgba(255,255,255,0.8)',
		color: ['#aaa', '#666'], //前者是字体颜色，后者是动画颜色
		fontSize: '16px'
	});
});

function show(data) {
	$(".main").waitMe("hide");
	
	if(data.category == "0" && data.contents=="") {
		// 课内单个文件
		// 先调用单个文件显示模板
		
		// 如果图片没有加载出来，循环直到图片加载完成
		var cellHtml = "";
		data.ext = getExt(data.name);
		cellHtml = template("template-in-class-file", data);
		$(".main").empty().append(cellHtml);
		
		// 设置预览图片的大小
		var main_width = $(".main").width();
		var img_width = 0;
		var img_height = 0;
		
		var img = new Image();
		img.src = $("#preview img").attr("src");
		
		img.onload = function() {
			img_width = img.width;
			img_height = img.height;
			
			var rate = main_width*1.0/img_width;
			var img_height = img_height*rate;
			
			$("#preview img").css({"width":main_width, "height":img_height});
		}
	}
}

function download_file() {
	var index=sessionStorage.getItem('index');
	var data = JSON.parse(sessionStorage.getItem('res')).data[index];
	
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "api/download?url="+data.url+"&filename="+data.name, true);
	xhr.responseType = "blob";
	xhr.onreadystatechange = ()=>{
		if (xhr.readyState==4) {
			if(xhr.status == 200) {
				download(xhr.response, data.name);
			} else if(xhr.status = 205) {
				alert("您需要重新登录");
			}
		}
	}
	xhr.send();
}