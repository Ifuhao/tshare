var curPage = 1;

$(document).ready(function() {
	search();
});

function search() {
	$.ajax({
		url: "api/list_sale",
		type: "GET",
		data: {page: curPage},
		success: res => {
			saleList("", JSON.parse(res));
		},
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
		
		for (var i = 0; i < data.length; i++) {
			// 添加序号
			data[i].index = i;
			cellHtml += template('template-sale-show', data[i])
		}
		// 清空列表，重新添加cell
		$('#sale-list').empty().append(cellHtml);

		//填分页器模板
		var pageHtml =
			'<li id="page-prev" class="page-item disabled"><a class="page-link" href="#" onclick="prevPage()">上一页</a></li>'
		if (res.amount > 20) {
			totalpages = Math.ceil(res.amount / 20)
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
	search();
}

/**
 * 下一页
 */
function nextPage() {
	curPage = parseInt(curPage) + 1;
	search();
}

/**
 * 跳转到某一页
 */
function toPage(page) {
	curPage = page;
	search();
}

/**
 * 查看文件详情
 */
function getDetails() {
	// 通过session把文件数据传到details页面
	var index = event.target.dataset.index;
	sessionStorage.setItem('index', index);
	location = "details.html";
}

