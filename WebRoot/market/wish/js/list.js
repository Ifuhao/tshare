var curPage = 1;
var back_colors = new Array();
back_colors[0] = "#fff2e2";
back_colors[1] = "#fde6e0";
back_colors[2] = "#dce2f1";
back_colors[3] = "#e9ebfe";
back_colors[4] = "#eaeaef";

$(document).ready(function() {
	load_pic("top");
	load_station("man-up");
	load_station("man-down");
	load_station("achieve-up");
	load_station("achieve-down");
	load_station("num-up");
	load_station("num-down");
	
	// 获取心愿统计信息
	$.ajax({
		url: "api/station",
		type: "GET",
		success: res => {
			if(res.code == 0) {
				// 出错
			} else {
				// 显示统计信息
				$("#man-num").html(res.man_num);
				$("#achieve-num").html(res.achieve_num);
				$("#num-num").html(res.num_num);
			}
		},
		dataType: "json"
	});
	search();
});

$(window).resize(function() {
	load_pic("top");
	load_station("man-up");
	load_station("man-down");
	load_station("achieve-up");
	load_station("achieve-down");
	load_station("num-up");
	load_station("num-down");
	
	waterFall();
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
		if(curPage > 1) {
			search();
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
		url: "api/list_wish",
		type: "GET",
		data: {page: curPage},
		success: res => {
			if(res.code == 0) {
				// 划到底了
			} else {
				wishList(res);
				curPage + 1;
			}
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: "json"
	});
}

function getWidth() {
	// 计算出每个心愿单的宽度
	var main_width = $("#wish-list").width();
	itemWidth = parseInt((main_width-100)/3);

	// 设置每个wish-box的宽度
	$(".wish-box").css("width", itemWidth);
	
	return itemWidth;
}

/**
 * 显示我的心愿单
 * @param res
 * @returns
 */
function wishList(res) {
	var cellHtml = "";
	data = res.data;
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].index = i;
		cellHtml += template('template-wish', data[i]);
	}
	$('#wish-list').append(cellHtml);
	waterFall();
}

function waterFall() {
	var itemWidth = getWidth();
	var gap = 30;
	var top = 10;
	
	var mainbox = document.getElementById('wish-list');
	var items = mainbox.getElementsByClassName('wish-box');
	
    // 1、 确定列数  = 页面的宽度 / 图片的宽度
    var pageWidth = $("#wish-list").width();
    var columns = parseInt(pageWidth / (itemWidth + gap));//显示的列数 = 页面宽度/(图片盒子宽度+间隙);
    var arr = [];
    for (var i = 0; i < items.length; i++) {
    	var colorIndex = Math.ceil(Math.random()*5);
    	items[i].style.background = back_colors[colorIndex];
    	
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
    
    var  parentHeight =  items[items.length-1].offsetHeight + items[items.length-1].offsetTop - 72;
    $("#wish-list").height(parentHeight);
}

// 下面的函数用于控制显示统计信息
function load_pic(id) {
	// 自动调整pic图片的大小
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $(".main").width();
	var img_width = img.width;
	var img_height = img.height;
	
	var rate = 1.0*main_width/img_width;
	img_height = img_height*rate;
	
	$("#"+id).css({"width":main_width, "height":img_height});
}

function load_station(id) {
	var img = new Image();
	img.src = $("#"+id).attr("src");
	
	var main_width = $(".main").width()*1.0/3.5;
	var img_width = img.width;
	var img_height = img.height;
	
	var rate = 1.0*main_width/img_width;
	img_height = img_height*rate;
	
	$("#"+id).css({"width":main_width, "height":img_height});
}