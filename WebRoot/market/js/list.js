var curPage = 1;

function go_search(key) {
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
	
	var type = $("#type").find(".selected").html();
	
	// 是否全新(0不是/1是)
	var sale_new = 0;
	// 是否配送(0不是/1是)
	var delivery = 0;
	// 是否可议价(0不可/1可)
	var bargain = 0;
	
	var btns = $("#other").find("button");
	if(btns[0].classList.contains("selected")) {
		sale_new = 1;
	}
	
	if(btns[1].classList.contains("selected")) {
		delivery = 1;
	}
	
	if(btns[2].classList.contains("selected")) {
		bargain = 1;
	}
	
	$.ajax({
		url: "api/search",
		type: "GET",
		data: {
			key: key,
			date: date,
			type: type,
			page: curPage,
			sale_new: sale_new,
			delivery: delivery,
			bargain: bargain
		},
		success: res => {
//			sessionStorage.setItem('res', res) //sessionStorage只能存字符串
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
	// 填模版
	var cellHtml = ''
	if (res.code == 0) {
		cellHtml = template('template-nofound', {
			keyword: key
		});
		$('#result').addClass('d-none');
		$("#sale-list").empty();
		$('.main').append(cellHtml);
		if(document.getElementById("list-top")) {
			$("#list-top").remove();
		}
	} else {
		$('#result').removeClass('d-none');
		data = res.data;
		
		if(document.getElementById("list-top")) {
			$("#list-top").remove();
		}
		
		var top = template("template-top", {amount: res.amount});
		$("#result").before(top);
		
		for (var i = 0; i < data.length; i++) {
			// 添加序号
			data[i].index = i;
			cellHtml += template('template-sale-show', data[i])
		}
		console.log(cellHtml);
		// 清空列表，重新添加cell
		$('#sale-list').empty().append(cellHtml);

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
 * 	切换排序方式
 */
function sortSwitch(sort) {
	globalSort = sort
	curPage = 1
	searchFile($('#search').val())
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
	// 通过session把文件数据传到details页面
	var index = event.target.dataset.index
	sessionStorage.setItem('index', index)
	location = "market/details.html"
}

