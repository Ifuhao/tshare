$(document).ready(function() {
	var sale_id = sessionStorage.getItem('index');
	
	// 根据sale_id发送请求
	$.ajax({
		url: "api/details",
		type: "GET",
		data: {sale_id: sale_id},
		success: res => {
			show(res, sale_id);
		},
		error: (xhr, status, error) => {
			console.log('[Status]', status, '\n[Error]', error);
		},
		dataType: "json",
		timeout: 5000
	});
});

function show(res, sale_id) {
	var saleHtml = "";
	
	// 填写商品简介模板
	var sale_new = "";
	switch(res.sale.sale_new) {
	case 0:
		sale_new = "全新";
		break;
	case 1:
		sale_new = "用过一两次";
		break;
	case 2:
		sale_new = "半成新";
		break;
	case 3:
		sale_new = "用过很久";
		break;
	}
	
	var bargain = "";
	if(res.sale.bargain == "0") {
		bargain = "不可议价";
	} else {
		bargain = "可议价";
	}
	
	var delivery = "";
	if(res.sale.delivery == "0") {
		delivery = "不配送";
	} else {
		delivery = "支持配送";
	}
	
	saleHtml += template("template-sale", {
		sale_id: sale_id,
		main_pic: res.sale.picture[0],
		title: res.sale.title,
		price: res.sale.price,
		buy_price: res.sale.buy_price,
		sale_new: sale_new,
		bargain: bargain,
		delivery: delivery,
		num: res.sale.num
	});
	
	// 填写商品出售者信息模板
	saleHtml += template("template-seller", {
		username: res.user.username,
		sell_id: res.user.uid,
		head_image: res.user.head_image
	});
	
	$("#sale").empty().append(saleHtml);
	
	var detailHtml = "";
	// 填写商品详情模板
	detailHtml += template("template-detail", {
		description: res.sale.description
	});
	$("#detail").empty().append(detailHtml);
	
	// 在商品详情中插入参考图片
	var imgHtml = "";
	$("#pics").empty();
	for(var i=0;i<res.sale.picture.length;i++) {
		imgHtml = template("template-picture", {
			index: i,
			sale_id: sale_id,
			pic: res.sale.picture[i]
		});
		$("#pics").append(imgHtml);
	}
	
	for(var i=0;i<res.sale.picture.length;i++) {
		auto_size(i);
	}
	
	if(res.sale.is_follow == 1) {
		// 没有关注此商品
		$("#mark").val("yes");
		$("#mark").html("已关注");
	}
	
	// 绑定关注商品按钮的点击事件
	$("#mark").click(function() {
		var is_follow = 1;
		if($("#mark").val() == "yes") {
			// 已经关注了，再点一下取消关注
			is_follow = 0;
		}
		
		$.ajax({
			url: "api/follow",
			type: "GET",
			data: {
				sale_id: sale_id,
				is_follow: is_follow
			},
			success: res => {
				// 修改关注按钮的样式
				if(is_follow==0) {
					$("#mark").val("no");
					$("#mark").html("关注商品");
				} else {
					$("#mark").val("yes");
					$("#mark").html("已关注");
				}
			},
			dataType: "json"
		});
	});
}

function auto_size(id) {
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $("#detail").width()*1.0/3.5;
	
	img.onload = function() {
		var img_width = img.width;
		var img_height = img.height;
		
		var rate = main_width*1.0/img_width;
		var img_height = img_height*rate;
		
		$("#pics #"+id).css({"width":main_width, "height":img_height});
	}
}

function add() {
	var cur_num = parseInt($("#cur-num").html());
	var rest_num = parseInt($("#rest-num").html());
	
	if(cur_num + 1 <= rest_num) {
		$("#cur-num").html(cur_num + 1);
	}
}

function sub() {
	var cur_num = parseInt($("#cur-num").html());
	
	if(cur_num - 1 >= 1) {
		$("#cur-num").html(cur_num - 1);
	}
}

function contact() {
	var sell_id = event.target.dataset.index;
	var user_id = sessionStorage.getItem("user_id");
	var title = $(".title").html();
	if(sell_id != "" && sell_id != undefined
		&& user_id != "" && user_id != null 
		&& user_id != undefined && user_id != "null") {
		socket.send(createMessage(user_id, sell_id, "null", "我看上了你的 【"+title+"】"+" 有兴趣聊一聊吗", "trade"));
	}
}