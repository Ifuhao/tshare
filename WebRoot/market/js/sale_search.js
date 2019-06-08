function inputSearch(key) {
	if (event.keyCode == 13) {
		// 阻止搜索空字符串
		if (key == '')
			event.preventDefault();
		else {
			// 回车搜索
			go_search(key);
		}
	}
}

function on_search() {
	var key = $("#search").val();
	if(key == '') {
		event.preventDefault();
	} else {
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