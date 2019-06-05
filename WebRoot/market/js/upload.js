var fileList = new Array();		// 全局数组保存上传的文件
/**
 * 图片选择发生变化时处理函数
 */
function selectPicture(file) {
	if(fileList.length + file.files.length > 3) {
		alert("最多选择三张图片");
	} else {
		$(file.files).each(function(i, e){
			// 过滤不是图片的文件
			if(e.type.indexOf("image/") === -1) {
				// 不是图片文件
				alert(e.name + "不是图片");
			} else {
				fileList.push(e);
			}
	    });
	    showPicture();
	}
	
	//清空input file里的信息
    $(file).val('');
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

var selects = {};		// 附下拉列表数组
selects['美妆护肤'] = "cosmetics";
selects['数码电子'] = "digital";

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
		show_select(category);
	}
}

/**
 * 显示附下拉列表中的一个，隐藏其他所有，如果传递进来的参数不是附下拉列表则会隐藏所有的附下拉列表
 */
function show_select(select_name) {
	for(var key in selects) {
		if(key !== select_name) {
			// 隐藏
			document.getElementById(selects[key]).style.display = "none";
		} else {
			// 显示
			document.getElementById(selects[key]).style.display = "block";
			change(document.getElementById(selects[key]));
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

/**
 * 获取附下拉列表的值
 */
function getSelect(select_name) {
	if(selects.hasOwnProperty(select_name)) {
		return $("#"+selects[select_name]).find("option:selected").text();
	} else {
		return null;
	}
}

/**
 * 提交上架请求
 * upload.html无效等价类测试
 * 1. 输入框为空
 * 2. 商品数量不为整数
 * 3. 买入价格不为浮点数
 * 4. 预期价格不为浮点数
 * 5. 图片数量超过三张
 */
function upload() {
	// 获取请求参数
	/**
	 * title：商品标题
	 * category：分类
	 * description：商品描述
	 * num：商品数量
	 * buy_price：买入价格
	 * buy_way：商品来源
	 * new：是否全新(0为全新/1为用过一两次/2为半成新/3为用过很久)
	 * price：预期价格
	 * bargain：能否议价
	 * delivery：能否配送
	 * picture：参考图片
	 */
	
	// 判断一下字段是否为空
	var isEmpty = false;
	if(fileList.length === 0) {
		isEmpty = true;
		console.log("没有上传图片");
		$('#picture').next().addClass('input-error');
	}
	
	$('#upload-body').find('input').add('#upload-body textarea').not(':hidden').not("#picture").each(function() {
		if ($(this).val() == "") {
			isEmpty = true;
			console.log($(this));
			$(this).addClass('input-error');
		}
	});
	
	if(isEmpty)
		return;
	
	/****** 获取商品标题 ******/
	var title = $("#name").val();
	
	/******* 获取商品分类，需要进行一些判断 ******/
	var category = $("#category").find("option:selected").text();
	if(category === "其他") {
		// 获取other-category的值
		category = $("#other-category").val();
	} else {
		// 获取附下拉列表的值
		var category_addition = getSelect(category);
		if(category_addition !== null) {
			if(category_addition === "其他") {
				category_addition = $("#other-select").val();
			}
			// 组合主下拉列表和附下拉列表
			category += category_addition;
		}
	}
	
	/********** 获取商品描述 ***********/
	var description = $("#description").val();
	
	/********** 获取商品数量并使用正则表达式判断是否是整数 ***********/
	var num = $("#num").val();
	if(!(/^\d+$/.test(num))) {
		// num不是数字，需要提醒
		$("#num").val("");
		$("#num").attr("placeholder", "商品数量必须是整数");
		$("#num").addClass("input-error");
	}

	/********** 获取商品买入价格并使用正则表达式判断是否为浮点数 ***********/
	var buy_price = $("#buy_price").val();
	if(!(/^\d{1,6}(.\d{1,2})?$/).test(buy_price)) {
		// buy_price不是浮点数，需要提醒
		$("#buy_price").val("");
		$("#buy_price").attr("placeholder", "买入价格必须是小数");
		$("#buy_price").addClass("input-error");
	}
	
	/********** 获取商品来源 ***********/
	var buy_way = $("#buy_way").val();
	
	/********** 获取商品是否全新并转换为数字 ***********/
	var form_new = $("#new").find("option:selected").text();
	var sale_new = 0;
	switch(form_new) {
	case "全新":
		sale_new = 0;
		break;
	case "用过一两次":
		sale_new = 1;
		break;
	case "半成新":
		sale_new = 2;
		break;
	case "用过很久":
		sale_new = 3;
		break;
	}
	
	/************ 获取商品预期价格并使用正则表达式判断是否为浮点数 ************/
	var price = $("#price").val();
	if(!(/^\d{1,6}(.\d{1,2})?$/).test(price)) {
		// price不是浮点数，需要提醒
		$("#price").val("");
		$("#price").attr("placeholder", "预期价格必须是小数");
		$("#price").addClass("input-error");
	}
	
	/*********** 获取能否议价并转化为数字 ************/
	var form_bargain = $("#bargain").find("option:selected").text();
	var bargain = 0;
	if(form_bargain === "不能") {
		bargain = 1;
	}
	
	/************ 获取能否配送并转化为数字 ***************/
	var form_delivery = $("#delivery").find("option:selected").text();
	var delivery = 1;
	if(form_delivery === "能") {
		delivery = 0;
	}
	
	/************ 获取参考图片 *****************/
	// fileList全局数组中保存着所有图片信息
	
	// 全部数据都获取完毕，向后台提交ajax请求
	var formdata = new FormData();
	formdata.append("title", title);
	formdata.append("category", category);
	formdata.append("description", description);
	formdata.append("num", num);
	formdata.append("buy_price", buy_price);
	formdata.append("buy_way", buy_way);
	formdata.append("sale_new", sale_new);
	formdata.append("price", price);
	formdata.append("bargain", bargain);
	formdata.append("delivery", delivery);
	
	var picture = new Array();
	$(fileList).each(function(i, e) {
		picture[i] = e;
	});
	formdata.append("picture", picture);
	
	$.ajax({
		url: 'api/publish_sale',
		type: 'POST',
		data: formdata,
		success: res => {
			if (res.code == 1) {
				alert('上传成功')
			} else {
				alert(res.msg);
			}
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		processData: false, // 不处理数据
		contentType: false, // 不设置内容类型
		dataType: 'json',
		timeout: 5000
	});
}















