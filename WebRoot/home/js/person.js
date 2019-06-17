var pages = new Array();
var curPage = "sale_page";
pages["sale_page"] = 1;
pages["seek_page"] = 1;
pages["wish_page"] = 1;
pages["file_page"] = 1;
pages['follow_page'] = 1;
pages['download_page'] = 1;

$(document).ready(function() {
	// 向servlet请求用户信息
	$.ajax({
		url: "api/person",
		type: "GET",
		success: res => {
			sessionStorage.setItem("user", res);
			
			res = JSON.parse(res);
			// 设置头像
			$("#user-img img").attr("src", "/upload/person/"+res.user.head_image);
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
			
			getData();
		},
	});
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

/**
 * 窗口放缩事件的响应函数
 */
$(window).resize(function() {
	if(curPage == "wish_page") {
		waterFall();
	}
});

function getData() {
	var url = "";
	if(curPage == "sale_page") {
		url = "/tshare/market/sale/api/list_my_sale";
	} else if(curPage == "seek_page") {
		url = "/tshare/market/seek/api/list_my_seek";
	} else if(curPage == "wish_page") {
		url = "/tshare/market/wish/api/list_my_wish";
	} else if(curPage == "file_page") {
		url = "/tshare/repository/api/list_my_upload";
	} else if(curPage == "follow_page") {
		url = "/tshare/market/sale/api/list_follow";
	} else if(curPage == "download_page") {
		url = "/tshare/repository/api/list_my_download";
	}
	
	// 使用ajax重新申请数据
	$.ajax({
		url: url,
		type: "GET",
		data: {page: pages[curPage]},
		success: res => {
			if(res.code == 0) {
//				alert(res.msg);
			} else {
				pages[curPage] += 1;
				if(curPage == "sale_page") {
					saleList(res.data);
				} else if(curPage == "seek_page") {
					seekList(res.data);
				} else if(curPage == "wish_page") {
					wishList(res.data);
				} else if(curPage == "file_page") {
					fileList(res.data);
				} else if(curPage == "follow_page") {
					followList(res.data);
				} else if(curPage == "download_page") {
					downloadList(res.data);
				}
			}
		},
		dataType: "json"
	});
}

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

function deletePage() {
	// 删除当前页面内容
	var index = curPage.indexOf("_page");
	var select = curPage.slice(0, index);
	$("#"+select+"-list").empty();
}

// 给所有的选择按钮绑定点击事件
$("#sale").click(function() {
	deletePage();
	curPage = "sale_page";
	pages[curPage] = 1;
	getData();
});

$("#seek").click(function() {
	deletePage();
	curPage = "seek_page";
	pages[curPage] = 1;
	getData();
});

$("#wish").click(function() {
	deletePage();
	curPage = "wish_page";
	pages[curPage] = 1;
	getData();
});

$("#file").click(function() {
	deletePage();
	curPage = "file_page";
	pages[curPage] = 1;
	getData();
});

$("#sale-follow").click(function() {
	deletePage();
	curPage = "follow_page";
	pages[curPage] = 1;
	getData();
});

$("#file-download").click(function() {
	deletePage();
	curPage = "download_page";
	pages[curPage] = 1;
	getData();
});

function getWidth(idName, className) {
	// 计算出每个心愿单的宽度
	var main_width = $("#"+idName).width();
	itemWidth = parseInt((main_width-100)/3);

	// 设置每个wish-box的宽度
	$("."+className).css("width", itemWidth);
	
	return itemWidth;
}

/**
 * 瀑布流
 * @param listName 瀑布流外部盒子的id
 * @param boxName 每个瀑布流的class
 * @returns
 */
function waterFall(listName, boxName) {
	var itemWidth = getWidth(listName, boxName);
	var gap = 30;
	var top = 10;
	
	var mainbox = document.getElementById(listName);
	var items = mainbox.getElementsByClassName(boxName);
	
    // 1、 确定列数  = 页面的宽度 / 图片的宽度
    var pageWidth = $("#"+listName).width();
    var columns = parseInt(pageWidth / (itemWidth + gap));//显示的列数 = 页面宽度/(图片盒子宽度+间隙);
    var arr = [];
    for (var i = 0; i < items.length; i++) {
        if (i < columns) {
            // 2、 确定第一行
            items[i].style.top = top + "px";
            items[i].style.left = ((itemWidth + gap) * i) + 'px';
            arr.push(items[i].offsetHeight);
        } else {
            // 其他行
            // 3- 找到数组中最小高度  和 它的索引
            var minHeight = arr[0];
            var index = 0;
            for (var j = 0; j < arr.length; j++) {
                if (minHeight > arr[j]) {
                    minHeight = arr[j];
                    index = j;
                }
            }
            // 4- 设置下一行的第一个盒子位置
            // top值就是最小列的高度 + gap
            items[i].style.top = 12 + arr[index] + gap + 'px';
            // left值就是最小列距离左边的距离
            items[i].style.left = items[index].offsetLeft + 'px';

            // 5- 修改最小列的高度 
            // 最小列的高度 = 当前自己的高度 + 拼接过来的高度 + 间隙的高度
            arr[index] = arr[index] + items[i].offsetHeight + gap;
        }
    }
    
    var parentHeight =  items[items.length-1].offsetHeight + items[items.length-1].offsetTop - 72;
    $("#"+listName).height(parentHeight);
}
