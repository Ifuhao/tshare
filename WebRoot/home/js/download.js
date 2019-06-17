function downloadList(data) {
	cellHtml = "";
	for(var i=0;i<data.length;i++) {
		data[i].index = data[i].did;
		data[i].ext = getExt(data[i].name);
		cellHtml += template("template-download", data[i]);
	}
	
	$("#download-list").append(cellHtml);
	
	for(var i=0;i<data.length;i++) {
		starScore("mark-"+data[i].index);
		
		if(data[i].is_mark == 1) {
			// 替换类选择器解除hover伪类效果
			$("#mark-"+data[i].index).find(".star").removeClass("star").addClass("newstar");
			document.getElementById("btn-"+data[i].index).style.display = "none";
			$("#mark-"+data[i].index).find(".newstar ul li a").unbind();
			
			// 根据评分修改星星
			$("#star-"+data[i].index).css("width", (10*data[i].score)+"%");
		}
	}
}

function mark() {
	var index = event.target.dataset.index;
	var score = $("#score-"+index).html();
	
	$.ajax({
		url: "/tshare/repository/api/mark",
		type: "GET",
		data: {
			did: index,
			score: score
		},
		success: res => {
			if(res.code == 1) {
				// 删除提交按钮
				$("#mark-"+index).find(".star ul li a").unbind();
				document.getElementById("btn-"+index).style.display = "none";
				$("#mark-"+index).find(".star").removeClass("star").addClass("newstar");
			} else {
				alert(res.msg);
			}
		},
		dataType: "json"
	});
}

/**
 * 接收一个指定id的父标签，给内部的元素指定响应事件
 * @param id
 * @returns
 */
function starScore(id, className){
	var star = $("#"+id);
	var fivestar = $(star.find(".star ul li a"));
	
	/**
	 * 
	 * mouseleave和mouseout的区别
	 * mouseenter和mousemouseover的区别
	 * http://www.w3school.com.cn/tiy/t.asp?f=jquery_event_mouseleave_mouseout
	 */
	fivestar.mouseenter(function(){
		var txt = $(this).attr("data-name");//相应星星代表的评分
		var x = $(this).parent("li").index();//li在数组中的位置【0，1，2，3，4】
		star.find(".tips").html(txt).css("left",-6+x*24).show();//txt放入框中
	});
	
	fivestar.mouseleave(function(){
		star.find(".tips").html("").css("left",0).hide();//隐藏框
	});
	
	fivestar.click(function() {
		var x = $(this).parent("li").index();//li在数组中的位置【0，1，2，3，4】
		
		star.find('.ystar').css("width",(x+1)*10+'%');
		star.find('.score').text(x+1);
	});
}
