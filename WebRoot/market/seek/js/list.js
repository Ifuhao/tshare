var curPage = 1;

$(document).ready(function() {
	search();
});

function search() {
	$.ajax({
		url: "api/list_seek",
		type: "GET",
		data: {page: curPage},
		success: res => {
			setSeek(res);
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: "json"
	});
}

function setSeek(res) {
	// 清除找不到提示
	if(document.getElementById("nofound")) {
		$("#nofound").remove();
	}
	
	if(document.getElementById("list-top")) {
		$("#list-top").remove();
	}
	
	var cellHtml = "";
	if(res.code == 0) {
		// 无新收购
		cellHtml = template('template-nofound', {});
		$('#result').addClass('d-none');
		$("#seek-list").empty();
		$('#result').before(cellHtml);
	} else {
		$('#result').removeClass('d-none')
		data = res.data;
		
		var top = template("template-top", {amount: res.amount});
		$("#result").before(top);
		
		for(var i=0;i<data.length;i++) {
			cellHtml += template("template-seek", data[i]);
		}
		
		$("#seek-list").empty().append(cellHtml);
		
		//填分页器模板
		var pageHtml =
			'<li id="page-prev" class="page-item disabled"><a class="page-link" href="#" onclick="prevPage()">上一页</a></li>'
		if (res.amount > 10) {
			totalpages = Math.ceil(res.amount / 10)
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