var curPage = 1;

$(document).ready(function() {
	search();
});

/**
 * 滚动条滚动事件响应函数
 */
window.onscroll = function() {
	var scrollTop = getScrollTop();
	var windowHeight = getWindowHeight();
	var scrollHeight = getScrollHeight();
	if(scrollTop + windowHeight == scrollHeight) {
		// 滚动条滚动到屏幕底部了，需要重新申请数据
		if(pages[curPage] > 1) {
			getData();
		}
	}
};

function getScrollTop(){
	var scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
	if(document.body){
		bodyScrollTop = document.body.scrollTop;
	}
	if(document.documentElement){
		documentScrollTop = document.documentElement.scrollTop;
	}
	scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
	return scrollTop;
}

function getScrollHeight(){
	var scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
	if(document.body){
		bodyScrollHeight = document.body.scrollHeight;
	}
	if(document.documentElement){
		documentScrollHeight = document.documentElement.scrollHeight;
	}
	scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
	return scrollHeight;
}

function getWindowHeight(){
	var windowHeight = 0;
	if(document.compatMode == "CSS1Compat"){
		windowHeight = document.documentElement.clientHeight;
	} else {
		windowHeight = document.body.clientHeight;
	}
	return windowHeight;
}

function search() {
	$.ajax({
		url: "api/list_sale",
		type: "GET",
		data: {page: curPage},
		success: res => {
			curPage = curPage+1;
			if(res.code == 1) {
				saleList(res.data);
			} else {
				alert(res.msg);
			}
		},
		dataType: "json"
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

function saleList(data) {
	// 隐藏等待动画，清除找不到的提示
	$('.main').waitMe('hide');
	
	var cellHtml = "";
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].main_pic = data[i].main_pic;
		cellHtml = template('template-sale-show', data[i]);
		$('#sale-list').append(cellHtml);
		if(data[i].is_follow == 1) {
			$("#concern-btn-"+data[i].sale_id).val("yes");
			$("#concern-btn-"+data[i].sale_id).html("已关注");
		}
	}
}

function concern(obj) {
	var is_follow = 1;
	if($(obj).val() == "yes") {
		// 已经关注了，再点一下取消关注
		is_follow = 0;
	}
	
	var sale_id = obj.id.substring(obj.id.lastIndexOf("-")+1,);
	
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
				$(obj).val("no");
				$(obj).html("立即关注");
			} else {
				$(obj).val("yes");
				$(obj).html("已关注");
			}
		},
		dataType: "json"
	});
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

function contact() {
	var sell_id = event.target.dataset.index;
	var user_id = sessionStorage.getItem("user_id");
	var title = $(event.target.parentNode).find(".sale-title").html();
	socket.send(createMessage(user_id, sell_id, "null", "我看上了你的 【"+title+"】"+" 有兴趣聊一聊吗", "trade"));
}

