var oldFileList = new Array();		// 以前的参考图片（字符串数组表示图片路径）
var newFileList = new Array();			// 新上传的图片
var base64 = new Array();			// 新上传图片的base64编码字符串

/**
 * 显示我的上架
 * @param res
 * @returns
 */
function saleList(data) {
	var cellHtml = "";
	
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].index = data[i].sale_id;
		data[i].main_pic = data[i].picture[0];
		cellHtml += template('template-sale', data[i]);
	}
	$('#sale-list').append(cellHtml);
	
	salecover();
}

function auto_sale() {
	var index = event.target.dataset.index;
	sessionStorage.setItem("sale_id", index);
	
	$.ajax({
		url: "/tshare/market/sale/api/details",
		type: "GET",
		data: {sale_id: index},
		success: res => {
			if(res.code==1) {
				// 图片最后处理
				data = res.sale;
				$("#sale-modal-title").val(data.title);
				$("#sale-modal-price").val(data.price);
				$("#sale-modal-buy_price").val(data.buy_price);
				$("#sale-modal-num").val(data.num);
				$("#sale-modal-buy_way").val(data.buy_way);
				$("#sale-modal-description").val(data.description);
				// sale_new
				setSale_new(data.sale_new);
				// category
				setCategory(data.category);
				// bargain
				setBargain(data.bargain);
				// delivery
				setDelivery(data.delivery);
				// 处理图片
				for(var i=0;i<data.picture.length;i++) {
					// 为0表示该图片没有被删除
					oldFileList[data.picture[i]] = 0;
				}
				// 展示图片
				showPicture();
			}
		},
		dataType: "json"
	});
}

function sale_edit() {
	// 提交修改请求
	var com = "sale-modal-";
	
	// 判断一下字段是否为空
	var isEmpty = false;
	if(fileList.length === 0) {
		isEmpty = true;
		console.log("没有上传图片");
		$("#" + com + "picture").next().addClass('input-error');
	}
	
	$("#" + com + "upload-body").find('input').add("#" + com + "upload-body textarea").not(':hidden').not("#" + com + "picture").each(function() {
		if ($(this).val() == "") {
			isEmpty = true;
			console.log($(this));
			$(this).addClass('input-error');
		}
	});
	
	if(isEmpty)
		return;
	
	/****** 获取商品标题 ******/
	var title = $("#" + com + "title").val();
	
	/******* 获取商品分类，需要进行一些判断 ******/
	var category = $("#" + com + "category").find("option:selected").text();
	if(category === "其他") {
		// 获取other-category的值
		category = $("#" + com + "other-category").val();
	} else {
		// 获取附下拉列表的值
		var category_addition = getSelect(category);
		if(category_addition !== null) {
			if(category_addition === "其他") {
				category_addition = $("#" + com + "other-select").val();
			}
			// 组合主下拉列表和附下拉列表
			category += (";"+category_addition);
		}
	}
	
	/********** 获取商品描述 ***********/
	var description = $("#" + com + "description").val();
	
	/********** 获取商品数量并使用正则表达式判断是否是整数 ***********/
	var num = $("#" + com + "num").val();
	if(!(/^\d+$/.test(num))) {
		// num不是数字，需要提醒
		$("#" + com + "num").val("");
		$("#" + com + "num").attr("placeholder", "商品数量必须是整数");
		$("#" + com + "num").addClass("input-error");
		return;
	}

	/********** 获取商品买入价格并使用正则表达式判断是否为浮点数 ***********/
	var buy_price = $("#" + com + "buy_price").val();
	if(!(/^\d{1,6}(.\d{1,2})?$/).test(buy_price)) {
		// buy_price不是浮点数，需要提醒
		$("#" + com + "buy_price").val("");
		$("#" + com + "buy_price").attr("placeholder", "买入价格必须是整数或小数且最多两位小数，例如：1000.00");
		$("#" + com + "buy_price").addClass("input-error");
		return;
	}
	
	/********** 获取商品来源 ***********/
	var buy_way = $("#" + com + "buy_way").val();
	
	/********** 获取商品是否全新并转换为数字 ***********/
	var form_new = $("#" + com + "new").find("option:selected").text();
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
	var price = $("#" + com + "price").val();
	if(!(/^\d{1,6}(.\d{1,2})?$/).test(price)) {
		// price不是浮点数，需要提醒
		$("#" + com + "price").val("");
		$("#" + com + "price").attr("placeholder", "预期价格必须是小数");
		$("#" + com + "price").addClass("input-error");
		return;
	}
	
	/*********** 获取能否议价并转化为数字 ************/
	var form_bargain = $("#" + com + "bargain").find("option:selected").text();
	var bargain = 1;
	if(form_bargain === "不能") {
		bargain = 0;
	}
	
	/************ 获取能否配送并转化为数字 ***************/
	var form_delivery = $("#" + com + "delivery").find("option:selected").text();
	var delivery = 0;
	if(form_delivery === "能") {
		delivery = 1;
	}
	
	/************ 获取参考图片 *****************/
	// fileList全局数组中保存着所有图片信息
	
	// 全部数据都获取完毕，向后台提交ajax请求
	var formdata = new FormData();
	formdata.append("sale_id", sessionStorage.getItem("sale_id"));
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
	
	var delIndex = 0;
	for(let key in oldFileList) {
		if(oldFileList[key] == 1) {
			formdata.append("delpic"+delIndex++, key);
		}
	}
	
	$(base64).each(function(i, e) {
		formdata.append("newpic"+i, e);
	});
	
	$.ajax({
		url: '/tshare/market/sale/api/edit',
		type: 'POST',
		data: formdata,
		success: res => {
			if (res.code == 1) {
				$("#sale_edit .success").css("display", "block");
				setTimeout("$(\"#sale_edit\").modal(\"hide\"); $(\"#sale_edit .success\").css(\"display\", \"none\");", 1500);
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

function setSale_new(sale_new) {
	var sale_newx = "";
	if(sale_new == 0) {
		// 全新
		sale_newx = "全新";
	} else if(sale_new == 1) {
		// 用过一两次
		sale_newx = "用过一两次";
	} else if(sale_new == 2) {
		// 半成新
		sale_newx = "半成新";
	} else if(sale_new == 3) {
		// 用了很久
		sale_newx = "用了很久";
	}
	$($("#sale-modal-new").find("option")).each(function(i,e ) {
		if(e.text == sale_newx) {
			e.selected = true;
		}
	});
}

function setCategory(category) {
	var main_flag = true;
	var attach_flag = true;
	
	var arr = category.split(";");
	$($("#sale-modal-category").find("option")).each(function(i, e) {
		if(e.text == arr[0]) {
			// 主分类为已知值，在此处设置附分类
			e.selected = true;
			category_change();
			
			if($("#sale-modal-digital").css("display") == "block") {
				$($("#sale-modal-digital").find("option")).each(function(i1, e1) {
					if(e1.text == arr[1]){
						e1.selected = true;
						change($("#sale-modal-digital"));
						attach_flag = false;
						// 附分类也设置完了，可以直接结束函数执行了
					} else if(attach_flag && i1==$("#sale-modal-digital option").length-1) {
						e1.selected = true;
						change($("#sale-modal-digital"));
						// 附分类为其他
						$("#sale-modal-other-select").val(arr[1]);
					}
				});
			} else if($("#sale-modal-cosmetics").css("display") == "block") {
				$($("#sale-modal-cosmetics").find("option")).each(function(i1, e1) {
					if(e1.text == arr[1]){
						e1.selected = true;
						change($("#sale-modal-cosmetics"));
						attach_flag = false;
						// 附分类也设置完了，可以直接结束函数执行了
					} else if(attach_flag && i1==$("#sale-modal-digital option").length-1) {
						e1.selected = true;
						change($("#sale-modal-cosmetics"));
						// 附分类为其他
						$("#sale-modal-other-select").val(arr[1]);
					}
				});
			}
			
			main_flag = false;
		} else if(main_flag && i==$("#sale-modal-category option").length-1) {
			// 主分类选择了其他
			e.selected = true;
			category_change();
			// 设置其他主分类的值
			$("#sale-modal-other-category").val(arr[0]);
		}
	});
}

function setBargain(bargain) {
	var bargainx = "";
	if(bargain == 0) {
		bargainx = "不能";
	} else if(bargain == 1) {
		bargainx = "能";
	}
	
	$($("#sale-modal-bargain").find("option")).each(function(i, e) {
		if(e.text == bargainx) {
			e.selected = true;
		}
	});
}

function setDelivery(delivery) {
	var deliveryx = "";
	if(delivery == 0) {
		deliveryx = "不能";
	} else if(delivery == 1) {
		deliveryx = "能";
	}
	
	$($("#sale-modal-delivery").find("option")).each(function(i, e) {
		if(e.text == deliveryx) {
			e.selected = true;
		}
	});
}

function salecover(){
	var mainbox = document.getElementById('news-sale');
	var mysale = mainbox.getElementsByClassName('cmdt');
	var pic = mainbox.getElementsByClassName("cmdt-img");
	
	for(var i=0;i<mysale.length;i++){
		//“删除”按钮的鼠标移入移出动作响应
		mysale[i].onmouseover = function(){
			this.childNodes[9].style.display = "block";
		};
		
		mysale[i].onmouseout = function(){
			this.childNodes[9].style.display = "none";
		};
		
		//蒙版的鼠标移入移出动作响应
		pic[i].onmouseover = function(){
			this.childNodes[3].style.display = "block";
		};
		
		pic[i].onmouseout = function(){
			this.childNodes[3].style.display = "none";
		};
	}
}

/**
 * 获取以前的参考图片的有效数量
 * @returns
 */
function getOldListSize() {
	var oldLength = 0;
	for(let key in oldFileList) {
		if(oldFileList[key] == 0) oldLength++;
	}
	
	return oldLength;
}

/**
 * 图片选择发生变化时处理函数
 */
function selectPicture(file) {
	if(newFileList.length + file.files.length + getOldListSize() > 3) {
		alert("最多选择三张图片");
	} else {
		$(file.files).each(function(i, e){
			// 过滤不是图片的文件
			if(e.type.indexOf("image/") === -1) {
				// 不是图片文件
				alert(e.name + "不是图片");
			} else {
				newFileList.push(e);
				
				// 将文件转换为base64类型的数据
				var reader = new FileReader();
				
				reader.readAsDataURL(e);
				reader.onloadend = function(element) {
					var data64 = element.target.result;
					base64.push(data64);
				}
			}
	    });
	    showPicture();
	}
	
	//清空input file里的信息
    $(file).val('');
}

// 展示所有选择的图片
function showPicture() {
	var com = "sale-modal-pic";
	var index = 1;

	// 以前的图片
	for(let key in oldFileList) {
		if(oldFileList[key]==0) {
			var pic = document.getElementById(com+(index));
			pic.src = "/upload/picture/"+key;
			pic.style.display = "block";
			index++;
		}
	}
	
	// 新的图片
	$(newFileList).each(function(i ,e) {
		var pic = document.getElementById(com+(index));
		pic.src = window.URL.createObjectURL(e);
		pic.alt = e.name;
		pic.style.display = "block";
		index++;
	});
}

//点击删除文件
function delFile(index){
	if(index < getOldListSize()) {
		var i=0;
		// 删除的是以前的图片
		for(let key in oldFileList) {
			if(oldFileList[key] == 0 && index == i) {
				// 删除这张图片
				delete(oldFileList[key]);
				var pic = document.getElementById("sale-modal-pic"+(index+1));
			    pic.src = "";
			    pic.alt = "";
			    pic.style.display = "none";
			    oldFileList[key] = 1;
			}
		}
	} else {
		newFileList.splice(getOldListSize() + index,1);
	    base64.splice(index, 1);
	    
	    var pic = document.getElementById("sale-modal-pic"+(index+1));
	    pic.src = "";
	    pic.alt = "";
	    pic.style.display = "none";
	}
}

var selects = {};		// 附下拉列表数组
selects['美妆护肤'] = "sale-modal-cosmetics";
selects['数码电子'] = "sale-modal-digital";

/**
 * category下拉列表选择元素被修改时的处理函数
 */
function category_change() {
	var category = $("#sale-modal-category").find("option:selected").text();
//	console.log("change:" + category);
	if(category === "其他") {
		// 选中了"其他"项，需要将隐藏的文本框显示出来
		document.getElementById("sale-modal-other-category").style.display = "block";
		// 并且关闭其他所有的附下拉列表和附文本框
		show_select("");
		document.getElementById("sale-modal-other-select").style.display = "none";
	} else {
		// 没有选中"其他"项，需要将文本框进行隐藏
		document.getElementById("sale-modal-other-category").style.display = "none";
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
	var option = $(select).find("option:selected").text();		// 根据索引获取被选中的列表
	
	if(option === "其他") {
		// 选中了"其他"项，需要将隐藏的文本框显示出来
		document.getElementById("sale-modal-other-select").style.display = "block";
	} else {
		// 没有选中"其他"项，需要将文本框进行隐藏
		document.getElementById("sale-modal-other-select").style.display = "none";
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
 * 删除上架的商品
 */
function delete_sale() {
	var sale_id = event.target.dataset.index;

	$.ajax({
		url: "/tshare/market/sale/api/edit",
		type: "POST",
		data: {
			sale_id: sale_id,
			is_delete: 1
		},
		success: res => {
			$("#sale-"+sale_id).remove();
		},
		dataType: "json"
	});
}
