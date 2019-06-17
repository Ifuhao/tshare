function followList(data) {
	var cellHtml = "";
	for(var i=0;i<data.length;i++) {
		data[i].index = data[i].sale_id;
		cellHtml += template("template-follow", data[i]);
	}
	
	$("#follow-list").append(cellHtml);
	waterFall("follow-list", "follow-box");
	cover();
}

function cover() {
	var mainbox = document.getElementById('news-follow');
	var followBox = mainbox.getElementsByClassName('follow-box');
	var cover = mainbox.getElementsByClassName("cover");
	
	for(var i=0;i<followBox.length;i++){
		//“删除”按钮的鼠标移入移出动作响应
		followBox[i].onmouseover = function(){
			this.childNodes[7].style.display = "block";
		};
		
		followBox[i].onmouseout = function(){
			this.childNodes[7].style.display = "none";
		};
		//蒙版的鼠标移入移出动作响应
		cover.onmouseover = function(){
			this.childNodes[3].style.display = "block";
		};
		
		cover.onmouseout = function(){
			this.childNodes[3].style.display = "none";
		};
	}
}

function cancel_follow() {
	var index = event.target.dataset.index;
	
	$.ajax({
		url: "/tshare/market/sale/api/follow",
		type: "GET",
		data: {
			sale_id: index,
			is_follow: 0
		},
		success: res => {
			$("#box-"+index).remove();
		},
		dataType: "json"
	});
}