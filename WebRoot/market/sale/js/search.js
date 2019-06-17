$(document).ready(function() {
	var key = sessionStorage.getItem("key");
	if(key == "" || key == null) {
		return;
	}
	$("#search").val(key);
	go_search(key);
});

function inputSearch(key) {
	if (event.keyCode == 13) {
		// 阻止搜索空字符串
		if (key == '')
			event.preventDefault();
		else {
			// 回车搜索
			sessionStorage.setItem("key", key);
			go_search(key);
		}
	}
}

function on_search() {
	var key = $("#search").val();
	if(key == '') {
		return;
	} else {
		sessionStorage.setItem("key", key);
		go_search(key);
	}
}

// 为选择按钮绑定点击事件
$("#date").find("button").click(function() {
	// 将该button设置为点击后的样式
	$(this).addClass("selected");
	var cur = this;
	
	// 该div标签下，其他的按钮都必须移除这个样式
	var btns = $("#date").find("button");
	$(btns).each(function(i, e) {
		if(e !== cur) {
			$(e).removeClass("selected");
		}
	});
});

$("#type").find("button").click(function() {
	// 将该button设置为点击后的样式
	$(this).addClass("selected");
	var cur = this;
	
	// 该div标签下，其他的按钮都必须移除这个样式
	var btns = $("#type").find("button");
	$(btns).each(function(i, e) {
		if(e !== cur) {
			$(e).removeClass("selected");
		}
	});
});

$("#other").find("button").click(function() {
	// 点击一次选中，再次点击则取消
	if(this.classList.contains("selected")) {
		$(this).removeClass("selected");
	} else {
		$(this).addClass("selected");
	}
});

var curPage = 1;
function go_search(key) {
	var url = "api/search?key="+key;
	sessionStorage.setItem("key", key);
	
	// 获取日期要求和类型要求并转化为对应的数字
	var sel_date = $("#date").find(".selected").html();
	var date = 0;
	switch(sel_date) {
	case "一天内":
		date = 1;
		break;
	case "一周内":
		date = 2;
		break;
	case "一月内":
		date = 3;
		break;
	case "一年内":
		date = 4;
		break;
	}
	url += "&date="+date;
	
	var type = $("#type").find(".selected").html();
	url += "&type="+type;
	
	var btns = $("#other").find("button");
	if(btns[0].classList.contains("selected")) {
		url += "&sale_new=1";
	}
	
	if(btns[1].classList.contains("selected")) {
		url += "&delivery=1";
	}
	
	if(btns[2].classList.contains("selected")) {
		url += "&bargain=1";
	}
	
	url += "&page="+curPage;
	
	$.ajax({
		url: url,
		type: "GET",
		success: res => {
			sessionStorage.setItem('res', res) //sessionStorage只能存字符串
			res = JSON.parse(res);
			saleList(key, res);
		},
		error: (xhr, status, error) => {
			console.log('[Status]', status, '\n[Error]', error)
			// 隐藏等待动画，清除找不到的提示
			$('.main').waitMe('hide');
			$('#nofound').remove();
			// 提示连接服务器超时
		},
		processData: false, // 不处理数据
		contentType: false, // 不设置内容类型
		timeout: 5000
	});
	
	// 等待动画
	$('.main').waitMe({
		effect: 'rotateplane',
		text:'正在搜索...',
		bg: 'rgba(255,255,255,0.8)',
		color: ['#aaa', '#666'], //前者是字体颜色，后者是动画颜色
		fontSize: '16px'
	});
}

/**
 * 显示搜索结果
 */
function saleList(key, res) {
	// 隐藏等待动画，清除找不到的提示
	$('.main').waitMe('hide');
	$('#nofound').remove();
	if(document.getElementById("list-top")) {
		$("#list-top").remove();
	}
	
	// 填模版
	var cellHtml = ''
	if (res.code == 0) {
		cellHtml = template('template-nofound', {
			keyword: key
		});
		$('#result').addClass('d-none');
		$("#sale-list").empty();
		$('.main').append(cellHtml);
	} else {
		$('#result').removeClass('d-none');
		data = res.data;
		
		var top = template("template-top", {amount: res.amount});
		$("#result").before(top);
		
		$('#sale-list').empty();
		for (var i = 0; i < data.length; i++) {
			// 添加序号			
			data[i].index = i;
			cellHtml += template('template-sale-show', data[i]);
			$('#sale-list').append(cellHtml);
			
			if(data[i].is_follow == 1) {
				$("#concern-"+i).attr("src", "img/concern.png");
				$("#concern-"+i).attr("title", "已关注");
				document.getElementById("concern-"+i).setAttribute("value", "yes");
			}
		}

		//填分页器模板
		var pageHtml =
			'<li id="page-prev" class="page-item disabled"><a class="page-link" href="#" onclick="prevPage()">上一页</a></li>'
		if (res.amount > 12) {
			totalpages = Math.ceil(res.amount / 12)
			// 如果页数太多，只显示当前页前后9页
			if (totalpages > 9) {
				var start = curPage - 4 > 1 ? curPage - 4 : 1
				var end = curPage + 4 < totalpages ? curPage + 4 : totalpages
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
		if (curPage != 1)
			$('#page-prev').removeClass('disabled')
		if (curPage < totalpages)
			$('#page-next').removeClass('disabled')
		$('#page-' + curPage).addClass('active')
	}
}

/**
 * 上一页
 */
function prevPage() {
	curPage = parseInt(curPage) - 1;
	go_search($('#search').val())
}

/**
 * 下一页
 */
function nextPage() {
	curPage = parseInt(curPage) + 1;
	go_search($('#search').val())
}

/**
 * 跳转到某一页
 */
function toPage(page) {
	curPage = page;
	go_search($('#search').val())
}

/**
 * 查看文件详情
 */
function getDetails() {
	var index = event.target.dataset.index;
	sessionStorage.setItem("index", index);
	location = "details.html";
}

/**
 * 关注商品
 */
function follow(obj) {
	var sale_id = event.target.dataset.index;
	var is_follow = 1;
	if(obj.getAttribute("value") == "yes") {
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
			if(is_follow == 0) {
				$(obj).attr("src", "img/not-concern.png");
				$(obj).attr("title", "关注商品");
				obj.setAttribute("value", "no");
			} else {
				$(obj).attr("src", "img/concern.png");
				$(obj).attr("title", "已关注");
				obj.setAttribute("value", "yes");
			}
		},
		dataType: "json"
	});
}
