$(document).ready(function() {
	// 向servlet请求用户信息
	
	$.ajax({
		url: "api/person",
		type: "GET",
		success: res => {
			sessionStorage.setItem("user", res);
			
			res = JSON.parse(res);
			// 设置头像
			$("#user-img img").attr("src", res.user.head_image);
			// 设置用户名
			$("#username").html(res.user.username);
			// 设置上传文件数量
			$("#upload-time h5").html(res.user.upload_time);
			// 设置交易成功数量
			$("#transaction h5").html(res.user.transaction);
			// 设置用户积分
			$("#money h5").html(res.user.money);
			// 设置我的关注
			$("#follow h5").html(res.user.follow_num);
			// 设置我的粉丝
			$("#fans h5").html(res.user.fans_num);
			
			mySale();
		},
	});
});

var pages = new Array();
pages["sale_page"] = 1;
pages["seek_page"] = 1;
pages["wish_page"] = 1;
pages["upload_page"] = 1;

var curPage = "sale_page";

function mySale() {
	curPage = "sale_page";
	
	$.ajax({
		url: "../market/sale/api/list_my_sale",
		type: "GET",
		data: {page: pages["sale_page"]},
		success: res => {
			// 调用我的上架模板显示上架信息
			if(res.code == 0) {
				var cellHtml = template('template-nofound', {});
				$('#result').addClass('d-none');
				$("#list").empty().append(cellHtml);
			} else {
				show_my_sale(res);
			}
		},
		dataType: "json"
	});
}

function show_my_sale(res) {
	$('#result').removeClass('d-none');
	var cellHtml = "";
	data = res.sale;
	
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].index = i;
		cellHtml += template('template-sale', data[i]);
	}
	// 清空列表，重新添加cell
	$('#list').empty().append(cellHtml);
	$("#list").addClass("row");
	$("#list").addClass("cmdt-row");

	pagination(res.amount);
}

function myUpload() {
	curPage = "upload_page";
	
}

/**
 * 添加分页器
 * @param amount
 * @returns
 */
function pagination(amount) {
	//填分页器模板
	var pageHtml =
		'<li id="page-prev" class="page-item disabled"><a class="page-link" href="#" onclick="prevPage()">上一页</a></li>'
	if (amount > 12) {
		totalpages = Math.ceil(amount / 12)
		// 如果页数太多，只显示当前页前后9页
		if (totalpages > 9) {
			var start = pages[curPage] - 4 > 1 ? pages[curPage] - 4 : 1
			var end = pages[curPage] + 4 < totalpages ? pages[curPage] + 4 : totalpages
			for (var i = start; i <= end; i++)
				pageHtml += template('template-page', {
					num: i
				})
		} else
			for (var i = 1; i <= totalpages; i++)
				pageHtml += template('template-page', {
					num: i
				})
	} else {
		totalpages = 1
		pageHtml += '<li id="page-1" class="page-item"><a class="page-link" href="#" onclick="toPage(this.text)">1</a></li>'
	}
	pageHtml +=
		'<li id="page-next" class="page-item disabled"><a class="page-link" href="#" onclick="nextPage()">下一页</a></li>'
	// 清空分页器，重新添加页项
	$('#pagination').empty().append(pageHtml)
	// 设置按钮状态
	if (pages[curPage] != 1)
		$('#page-prev').removeClass('disabled')
	if (pages[curPage] < totalpages)
		$('#page-next').removeClass('disabled')
	$('#page-' + pages[curPage]).addClass('active')
}

/**
 * 上一页
 */
function prevPage() {
	pages[curPage] = pages[curPage] - 1;
	if(curPage == "sale_page") {
		mySale();
	} else if(curPage == "seek_page") {
		mySeek();
	} else if(curPage == "wish_page") {
		myWish();
	} else if(curPage == "upload_page") {
		myUpload();
	}
}

/**
 * 下一页
 */
function nextPage() {
	pages[curPage] = pages[curPage] + 1;
	if(curPage == "sale_page") {
		mySale();
	} else if(curPage == "seek_page") {
		mySeek();
	} else if(curPage == "wish_page") {
		myWish();
	} else if(curPage == "upload_page") {
		myUpload();
	}
}

/**
 * 跳转到某一页
 */
function toPage(page) {
	pages[curPage] = page;
	if(curPage == "sale_page") {
		mySale();
	} else if(curPage == "seek_page") {
		mySeek();
	} else if(curPage == "wish_page") {
		myWish();
	} else if(curPage == "upload_page") {
		myUpload();
	}
}