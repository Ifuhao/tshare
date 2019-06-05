var fileList = new Array();		// 全局数组保存上传的文件
/**
 * 图片选择发生变化时处理函数
 */
function selectPicture(file) {
	$(file.files).each(function(i, e){
        fileList.push(e);
    });
	
	 //清空input file里的信息
    $(file).val('');

    if(fileList.length > 3) {
		alert("最多选择三张图片");
		return;
	}
    showPicture();
}

// 展示所有选择的图片
function showPicture() {
	var com = "pic";
	$(fileList).each(function(i ,e) {
		var pic = document.getElementById(com+(i+1));
		pic.src = window.URL.createObjectURL(e);
		pic.alt = e.name;
		pic.style.display = "block";
	});
}

//点击删除文件
function delFile(index){
    fileList.splice(index,1);
    var pic = document.getElementById("pic"+(index+1));
    pic.src = "";
    pic.alt = "";
    pic.style.display = "none";
}

/**
 * new下拉列表选择元素被修改时的处理函数
 */
function new_change() {
	
}


var selects = ["cosmetics", "digital"];		// 附下拉列表数组

/**
 * category下拉列表选择元素被修改时的处理函数
 */
function category_change() {
	var category = $("#category").find("option:selected").text();
	if(category === "其他") {
		// 选中了"其他"项，需要将隐藏的文本框显示出来
		document.getElementById("other-category").style.display = "block";
		// 并且关闭其他所有的附下拉列表和附文本框
		show_select("");
		document.getElementById("other-select").style.display = "none";
	} else {
		// 没有选中"其他"项，需要将文本框进行隐藏
		document.getElementById("other-category").style.display = "none";
		// 并显示对应的附下拉列表，隐藏其他列表
		if(category === "美妆护肤") {
			show_select("cosmetics");
		} else if(category === "数码电子") {
			show_select("digital");
		} else if(category === "休闲零食") {
			// 没有附下拉列表
			show_select("");
		} else if(category === "宿舍神器") {
			// 没有附下拉列表
			show_select("");
		} else if(category === "教材文具") {
			// 没有附下拉列表
			show_select("");
		}
	}
}

/**
 * 显示附下拉列表中的一个，隐藏其他所有，如果传递进来的参数不是附下拉列表则会隐藏所有的附下拉列表
 */
function show_select(select_name) {
	console.log(select_name);
	for(var i=0;i<selects.length;i++) {
		if(selects[i] !== select_name) {
			// 隐藏
			console.log("隐藏" + selects[i]);
			document.getElementById(selects[i]).style.display = "none";
		} else {
			// 显示
			console.log("显示" + selects[i]);
			document.getElementById(select_name).style.display = "block";
			change(document.getElementById(select_name));
		}
	}
}

/**
 * category的附下拉列表选择元素被修改时的处理函数
 */
function change(select) {
	var option = select.options[select.selectedIndex].value;		// 根据索引获取被选中的列表
	
	if(option === "其他") {
		// 选中了"其他"项，需要将隐藏的文本框显示出来
		document.getElementById("other-select").style.display = "block";
	} else {
		// 没有选中"其他"项，需要将文本框进行隐藏
		document.getElementById("other-select").style.display = "none";
	}
}