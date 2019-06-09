$(document).ready(function() {
	var index=sessionStorage.getItem('index');
	var data = JSON.parse(sessionStorage.getItem('res')).data[index];
	
	// 根据sale_id发送请求
	$.ajax({
		url: "api/details",
		type: "GET",
		data: {sale_id: data.sale_id},
		success: res => {
			show(res, data.sale_id);
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
		username: res.user.username
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
		imgHtml += template("template-picture", {
			index: i,
			sale_id: sale_id,
			pic: res.sale.picture[i]
		});
		$("#pics").append(imgHtml);
	}
	
	for(var i=0;i<res.sale.picture.length;i++) {
		auto_size(i);
	}
}

function auto_size(id) {
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $("#detail").width()*1.0/2;
	
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