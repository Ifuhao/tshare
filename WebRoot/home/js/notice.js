// 全局变量保存当前选项的页码
var pages = new Array();
pages['system'] = 1;
pages['person'] = 1;
pages['trade'] = 1;
pages['other'] = 1;
var curPage = "system";		// 定位当前选项

var detail_page = 1;		// 用户组显示的页码
var cur_group = 0;

$(document).ready(function() {
	new_msg();
	
	// 根据url来跳转到指定的消息选项
	var url = document.URL;
	var end = url.indexOf("#");
	end = (end==-1)?url.length:end;
	var type = url.substring(url.indexOf("?")+1, end);
	
	var obj = $(".main").find(".side-bar").find(".item").find("a");
	$(obj).removeClass("active");
	$(".tab-content").find(".tab-pane").removeClass("active");
	
	$(obj).each(function(i, e) {
		if($(e).attr("href").substring(1) == type) {
			$(e).addClass("active");
			$($(e).attr("href")).addClass("active");
			curPage = type;
		}
	});
	
	if(type == "system") {
		getSystem();
	} else if(type == "person") {
		getPerson();
	} else if(type == "trade") {
		getTrade();
	} else if(type == "other") {
		getOther();
	}
});

function new_msg() {
	// 将所有小红点消除
	$("#system-notice").find("img").attr("src", "img/new_msg.png");
	$("#system-notice").find("img").addClass("alpha-0");
	
	$("#person-notice").find("img").attr("src", "img/new_msg.png");
	$("#person-notice").find("img").addClass("alpha-0");
	
	$("#trade-notice").find("img").attr("src", "img/new_msg.png");
	$("#trade-notice").find("img").addClass("alpha-0");
	
	$("#other-notice").find("img").attr("src", "img/new_msg.png");
	$("#other-notice").find("img").addClass("alpha-0");
	
	var user_id = sessionStorage.getItem("user_id");
	var socket = websocket(user_id);
	sessionStorage.clear();
	
	sessionStorage.setItem("new-person-notice", "null");
	sessionStorage.setItem("new-trade-notice", "null");
	sessionStorage.setItem("user_id", user_id);
	
	socket.onmessage = function(msg) {
		var data = JSON.parse(msg.data);
		if(data.system == 1) {
			$("#system-notice").find("img").removeClass("alpha-0");
			// 如果当前正在展示的页面是系统通知页面，则立即获得刷新后的数据
		}
		if(data.person != undefined) {
			// 有新的私信
			$("#person-notice").find("img").removeClass("alpha-0");
			
			// 获取原来已经保存的新的私信，将现在的新的私信合并到一起
			var cur = sessionStorage.getItem("new-person-notice");
			if(cur == "null" || cur == null) {
				cur = data.person;
				for(var i=0;i<data.person.length;i++) {
					// 对于有新消息的用户组，立刻使用红点进行提示
					$("a[href='#group-"+data.person[i]+"']").find("img").removeClass("alpha-0");
				}
			} else {
				cur = JSON.parse(cur);
				for(var i=0;i<data.person.length;i++) {
					// 对于有新消息的用户组，立刻使用红点进行提示
					$("a[href='#group-"+data.person[i]+"']").find("img").removeClass("alpha-0");
					
					if(cur.indexOf(data.person[i]) == -1) {
						// 表示该用户组是新的用户组
						cur[cur.length] = data.person[i];
					}
				}
			}
			
			if(curPage == "person") {
				// 当前正在私信页面
				for(var i=0;i<data.person.length;i++) {
					if(data.person[i] == cur_group) {
						refresh_person_detail(cur_group);
						cur.splice(cur.indexOf(cur_group), 1);
						break;
					}
				}
			}
			
			sessionStorage.setItem("new-person-notice", JSON.stringify(cur));
		}
		
		if(data.trade != undefined) {
			// 有新的私信
			$("#trade-notice").find("img").removeClass("alpha-0");
			
			// 获取原来已经保存的新的私信，将现在的新的私信合并到一起
			var cur = sessionStorage.getItem("new-trade-notice");
			if(cur == "null" || cur == null) {
				cur = data.trade;
				for(var i=0;i<data.trade.length;i++) {
					// 对于有新消息的用户组，立刻使用红点进行提示
					$("a[href='#group-"+data.trade[i]+"']").find("img").removeClass("alpha-0");
				}
			} else {
				cur = JSON.parse(cur);
				for(var i=0;i<data.trade.length;i++) {
					// 对于有新消息的用户组，立刻使用红点进行提示
					$("a[href='#group-"+data.trade[i]+"']").find("img").removeClass("alpha-0");
					
					if(cur.indexOf(data.trade[i]) == -1) {
						// 表示该用户组是新的用户组
						cur[cur.length] = data.trade[i];
					}
				}
			}
			
			if(curPage == "trade") {
				// 当前正在私信页面
				for(var i=0;i<data.trade.length;i++) {
					if(data.trade[i] == cur_group) {
						refresh_trade_detail(cur_group);
						cur.splice(cur.indexOf(cur_group), 1);
						break;
					}
				}
			}
			
			sessionStorage.setItem("new-trade-notice", JSON.stringify(cur));
		}
	};
}

/**
 * 点击系统通知选项，获取系统通知
 * @returns
 */
function getSystem() {
	$("#system-list").empty();		// 首先清空之前显示的东西
	curPage = "system";				// 将当前显示选项设置为系统通知
	pages["system"] = 1;
	search_system();				// 获取系统通知并显示
}

/**
 * 点击私信选项，获取私信
 * @returns
 */
function getPerson() {
	$("#person-list").empty();			// 首先清空左边的简介列表
	$("#person-detail-list").empty();	// 清空所有的详情显示
	curPage = "person";					// 将当前显示选项设置为私信
	pages["person"] = 1;
	detail_page = 1;					// 初次默认显示第一页

	var new_person_notice = sessionStorage.getItem("new-person-notice");
	var user_id = sessionStorage.getItem("user_id");
	sessionStorage.clear();
	sessionStorage.setItem("new-person-notice", new_person_notice);
	sessionStorage.setItem("user_id", user_id);
	
	search_person_group();
}

function getTrade() {
	$("#trade-list").empty();			// 首先清空左边的简介列表
	$("#trade-detail-list").empty();	// 清空所有的详情显示
	curPage = "trade";					// 将当前显示选项设置为私信
	pages["trade"] = 1;
	detail_page = 1;					// 初次默认显示第一页

	var new_person_notice = sessionStorage.getItem("new-person-notice");
	var user_id = sessionStorage.getItem("user_id");
	sessionStorage.clear();
	sessionStorage.setItem("new-person-notice", new_person_notice);
	sessionStorage.setItem("user_id", user_id);
	
	search_trade_group();
}

function getOther() {
	console.log("其他消息");
}

/**
 * 请求系统通知
 * @returns
 */
function search_system() {
	$.ajax({
		url: "api/system_notice",
		type: "GET",
		data: {
			page: pages['system']
		},
		success: res => {
			if(res.code == 1) {
				show_system(res.data);
				pages['system'] += 1;
			}
		},
		dataType: "json"
	});
}

/**
 * 显示系统通知
 * @param data
 * @returns
 */
function show_system(data) {
	var cellHtml = "";
	for(var i=0;i<data.length;i++) {
		var content = data[i].content;
		
		sessionStorage.setItem("system-info-"+data[i].sys_id, content);
		
		// 简介仅显示消息内容的前30个字符
		data[i].simple_content = content.length<30?content:content.substring(0, 30)+"...";
		cellHtml = template("template-system-notice", data[i]);
		$("#system-list").append(cellHtml);
		
		if(data[i].is_read == 1) {
			// 未读的通知显示红点提示
			// is_read = 1表示已读，is_read = 0表示未读
			$("#system-"+data[i].sys_id).find("img").addClass("alpha-0");
		}
	}
}

function show_system_info(id) {
	id = id.substring(id.indexOf("-")+1);
	var content = sessionStorage.getItem("system-info-"+id);
	$("#system-info").find(".modal-body").find(".row").html(content);
	
	// 将该系统消息设置为已读
	$.ajax({
		url: "api/system_read",
		type: "GET",
		data: {
			sys_id: id
		},
		success: res => {
			if(res.code == 1) {
				// 取消小红点
				$("#system-"+id).find("img").addClass("alpha-0");
				
				// 如果所有的系统通知都阅读了，那么取消系统通知选项上的小红点提示
				var item = $(".system-item").children(".top").length;
				var alpha = $(".system-item").find(".top").children(".alpha-0").length;
				if(item.length == alpha.length) {
					$("#system-notice").find("img").addClass("alpha-0");
				}
			}
		},
		dataType: "json"
	});
}

/**
 * 请求私信的用户组信息
 * @returns
 */
function search_person_group() {
	$.ajax({
		url: "api/message_group",
		type: "GET",
		data: {
			page: pages['person'],
			type: "person"
		},
		success: res => {
			if(res.code == 1) {
				pages['person'] += 1;
				show_person_group(res.data);		// 将用户组信息显示在左边的简介列表上
				search_person_detail(res.data[0].group.group_id);		// 初次默认显示第一个用户组的详细信息
			}
		},
		dataType: "json"
	});
}

function show_person_group(data) {
	var leftHtml = "";
	var rightHtml = "";
	
	for(var i=0;i<data.length;i++) {
		// 左边的简介列表和右边的详情列表都是与用户组有关的
		var group = data[i].group;
		var sender = data[i].sender;
		var address = data[i].address;
		
		// 根据用户组编号，保存所有的组信息
		sessionStorage.setItem("group-"+group.group_id, JSON.stringify(data[i]));
		
		leftHtml = template("template-message-notice", {
			group_id: group.group_id,
			last_msg: group.last_msg,
			head_image: sender.head_image,
			sender_name: sender.name
		});
		$("#person-list").append(leftHtml);
		
		rightHtml = template("template-msg-in", {
			group_id: group.group_id,
			sender_name: sender.name,
			type: "person"
		});
		$("#person-detail-list").append(rightHtml);
		
		if(i==0) {
			// 第一个用户组进行显示
			$("#group-"+group.group_id).addClass("active");
			$("#group-"+group.group_id).css("display", "flex");
		}
		
		if(group.is_read == 0) {
			// 如果没有新的消息，就隐藏提示红点
			// is_read = 1表示有新消息，is_read = 0表示没有新消息
			$("a[href='#group-"+group.group_id+"']").find("img").addClass("alpha-0");
		}
	}
	
	clickGroup();
}

/**
 * 获取指定的用户组的详细信息
 * @param group_id
 * @returns
 */
function search_person_detail(group_id) {
	cur_group = group_id;
	$.ajax({
		url: "api/message_detail",
		type: "GET",
		data: {
			group_id: group_id,
			page: detail_page
		},
		success: res => {
			if(res.code == 1) {
				// 根据用户组编号保存其本次请求到的详细信息
				sessionStorage.setItem("detail-"+group_id, JSON.stringify(res.data));
				detail_page += 1;
				show_person_detail(res.data, group_id);
				
				var cur = sessionStorage.getItem("new-person-notice");
				if(cur != "null" && cur != null) {
					cur = JSON.parse(cur);
					group_id = parseInt(group_id);
					if(cur.indexOf(group_id) != -1) {
						cur.splice(cur.indexOf(group_id), 1);
						sessionStorage.setItem("new-person-notice", JSON.stringify(cur));
					}
				}
			}
		},
		dataType: "json"
	});
}

/**
 * 显示用户组的聊天消息，从服务器端发送过来的消息是按时间顺序由后往前的
 * 所以在填写模板的时候，也要从后往前填写
 * @param data
 * @param group_id
 * @returns
 */
function show_person_detail(data, group_id) {
	var msgHtml = "";
	var group = JSON.parse(sessionStorage.getItem("group-"+group_id));
	
	// 然后添加每一条具体的消息
	for(var j=0;j<data.length;j++) {
		if(data[j].is_me == 0) {
			// 这不是我发的消息
			msgHtml = template("template-msg-notme", {
				head_image: group.sender.head_image,
				content: data[j].content
			});
		} else if(data[j].is_me == 1) {
			// 这是我发的消息
			msgHtml = template("template-msg-isme", {
				head_image: group.address.head_image,
				content: data[j].content
			});
		}
		$("#message-content-list-"+group_id).find(".msg-time").after(msgHtml);
	}
	
	// 显示时间
	$("#message-content-list-"+group_id).find(".msg-time").find(".time").html(data[0].time);
}

/**
 * 刷新指定用户组的消息
 * @param group_id
 * @returns
 */
function refresh_person_detail(group_id) {
	detail_page = 1;
	// 通过append新增加的消息达到目的
	$.ajax({
		url: "api/message_detail",
		type: "GET",
		data: {
			page: detail_page,
			group_id: group_id
		},
		success: res => {
			if(res.code == 1) {
				// 将新增加的消息显示在消息框中
				var data = res.data;
				var detail = JSON.parse(sessionStorage.getItem("detail-"+group_id));
				var newData = [];
				for(var i=0;i<data.length;i++) {
					var dataTime = new Date(Date.parse(data[i].time.replace(/-/g,"/")));
					var detailTime = new Date(Date.parse(detail[0].time.replace(/-/g,"/")));
					
					if(dataTime.getTime() > detailTime.getTime()) {
						newData[newData.length] = data[i];
					}
				}
				
				sessionStorage.setItem("detail-"+group_id, JSON.stringify(res.data));
				// 将暂时显示的消息删除
				var temp = sessionStorage.getItem("temp");
				if(temp != null && temp != undefined && temp != "null") {
					temp = JSON.parse(temp);
					for(var i=0;i<temp.length;i++) {
						$("#temp-"+(i+1)).remove();
					}
				}
				sessionStorage.setItem("temp", "null");
				
				var group = JSON.parse(sessionStorage.getItem("group-"+group_id));
				for(var i=newData.length-1;i>=0;i--) {
					var msgHtml = "";
					if(data[i].is_me == 0) {
						// 这不是我发的消息
						msgHtml = template("template-msg-notme", {
							head_image: group.sender.head_image,
							content: data[i].content
						});
					} else if(data[i].is_me == 1) {
						// 这是我发的消息
						msgHtml = template("template-msg-isme", {
							head_image: group.address.head_image,
							content: data[i].content
						});
					}
					$("#message-content-list-"+group_id).append(msgHtml);
				}
			}
		},
		dataType: "json"
	});
}

/**
 * 请求交易的用户组信息
 * @returns
 */
function search_trade_group() {
	$.ajax({
		url: "api/message_group",
		type: "GET",
		data: {
			page: pages['trade'],
			type: "trade"
		},
		success: res => {
			if(res.code == 1) {
				pages['trade'] += 1;
				show_trade_group(res.data);		// 将用户组信息显示在左边的简介列表上
				search_trade_detail(res.data[0].group.group_id);		// 初次默认显示第一个用户组的详细信息
			}
		},
		dataType: "json"
	});
}

/**
 * 显示交易消息简介列表
 * @param data
 * @returns
 */
function show_trade_group(data) {
	var leftHtml = "";
	var rightHtml = "";
	
	for(var i=0;i<data.length;i++) {
		// 左边的简介列表和右边的详情列表都是与用户组有关的
		var group = data[i].group;
		var sender = data[i].sender;
		var address = data[i].address;
		
		// 根据用户组编号，保存所有的组信息
		sessionStorage.setItem("group-"+group.group_id, JSON.stringify(data[i]));
		
		leftHtml = template("template-message-notice", {
			group_id: group.group_id,
			last_msg: group.last_msg,
			head_image: sender.head_image,
			sender_name: sender.name
		});
		$("#trade-list").append(leftHtml);
		
		rightHtml = template("template-msg-in", {
			group_id: group.group_id,
			sender_name: sender.name,
			type: "trade"
		});
		$("#trade-detail-list").append(rightHtml);
		
		if(i==0) {
			// 第一个用户组进行显示
			$("#group-"+group.group_id).addClass("active");
			$("#group-"+group.group_id).css("display", "flex");
		}
		
		if(group.is_read == 0) {
			// 如果没有新的消息，就隐藏提示红点
			$("a[href='#group-"+group.group_id+"']").find("img").addClass("alpha-0");
		}
	}
	
	clickGroup();
}

/**
 * 获取指定的用户组的详细信息
 * @param group_id
 * @returns
 */
function search_trade_detail(group_id) {
	cur_group = group_id;
	$.ajax({
		url: "api/message_detail",
		type: "GET",
		data: {
			group_id: group_id,
			page: detail_page
		},
		success: res => {
			if(res.code == 1) {
				// 根据用户组编号保存其本次请求到的详细信息
				sessionStorage.setItem("detail-"+group_id, JSON.stringify(res.data));
				detail_page += 1;
				show_person_detail(res.data, group_id);
				
				var cur = sessionStorage.getItem("new-trade-notice");
				if(cur != "null" && cur != null) {
					cur = JSON.parse(cur);
					group_id = parseInt(group_id);
					if(cur.indexOf(group_id) != -1) {
						cur.splice(cur.indexOf(group_id), 1);
						sessionStorage.setItem("new-trade-notice", JSON.stringify(cur));
					}
				}
			}
		},
		dataType: "json"
	});
}

/**
 * 显示用户组的聊天消息，从服务器端发送过来的消息是按时间顺序由后往前的
 * 所以在填写模板的时候，也要从后往前填写
 * @param data
 * @param group_id
 * @returns
 */
function show_trade_detail(data, group_id) {
	var msgHtml = "";
	var group = JSON.parse(sessionStorage.getItem("group-"+group_id));
	
	// 然后添加每一条具体的消息
	for(var j=0;j<data.length;j++) {
		if(data[j].is_me == 0) {
			// 这不是我发的消息
			msgHtml = template("template-msg-notme", {
				head_image: group.sender.head_image,
				content: data[j].content
			});
		} else if(data[j].is_me == 1) {
			// 这是我发的消息
			msgHtml = template("template-msg-isme", {
				head_image: group.address.head_image,
				content: data[j].content
			});
		}
		$("#message-content-list-"+group_id).find(".msg-time").after(msgHtml);
	}
	
	// 显示时间
	$("#message-content-list-"+group_id).find(".msg-time").find(".time").html(data[0].time);
}

/**
 * 刷新指定用户组的消息
 * @param group_id
 * @returns
 */
function refresh_trade_detail(group_id) {
	detail_page = 1;
	// 通过append新增加的消息达到目的
	$.ajax({
		url: "api/message_detail",
		type: "GET",
		data: {
			page: detail_page,
			group_id: group_id
		},
		success: res => {
			if(res.code == 1) {
				// 将新增加的消息显示在消息框中
				var data = res.data;
				var detail = JSON.parse(sessionStorage.getItem("detail-"+group_id));
				var newData = [];
				for(var i=0;i<data.length;i++) {
					var dataTime = new Date(Date.parse(data[i].time.replace(/-/g,"/")));
					var detailTime = new Date(Date.parse(detail[0].time.replace(/-/g,"/")));
					
					if(dataTime.getTime() > detailTime.getTime()) {
						newData[newData.length] = data[i];
					}
				}
				
				sessionStorage.setItem("detail-"+group_id, JSON.stringify(res.data));
				// 将暂时显示的消息删除
				var temp = sessionStorage.getItem("temp");
				if(temp != null && temp != undefined && temp != "null") {
					temp = JSON.parse(temp);
					for(var i=0;i<temp.length;i++) {
						$("#temp-"+(i+1)).remove();
					}
				}
				sessionStorage.setItem("temp", "null");
				
				var group = JSON.parse(sessionStorage.getItem("group-"+group_id));
				for(var i=newData.length-1;i>=0;i--) {
					var msgHtml = "";
					if(data[i].is_me == 0) {
						// 这不是我发的消息
						msgHtml = template("template-msg-notme", {
							head_image: group.sender.head_image,
							content: data[i].content
						});
					} else if(data[i].is_me == 1) {
						// 这是我发的消息
						msgHtml = template("template-msg-isme", {
							head_image: group.address.head_image,
							content: data[i].content
						});
					}
					$("#message-content-list-"+group_id).append(msgHtml);
				}
			}
		},
		dataType: "json"
	});
}

/**
 * 为每个用户组绑定点击事件
 * @returns
 */
function clickGroup() {
	var listitem = $(".space-right-main .list-item");

	listitem.mouseenter(function() {
		x= $(this).find('.close');
		x.css("transform","translateX(0)"); //css改style   attr改标签
		
		$(this).addClass("active");
	});
	
	listitem.mouseleave(function() {
		x= $(this).find('.close');
		x.css("transform","translateX(-100px)"); //css改style   attr改标签
		
		users = $(this).attr("href");
		if($($(" "+users+" ")).hasClass("active")){
			
		}else{
			$(this).removeClass("active");
		}
	});
	
	/**
	 * 用户组的点击事件代表，需要将原来的详细信息隐藏，然后显示一个新的用户组的详细信息.
	 * 当点击这个用户组的时候，判断一下这个用户组是否有新的消息，如果有，再去请求数据库
	 */
	listitem.click(function() {
		$(this).addClass("active").siblings().removeClass("active");
		users = $(this).attr("href");
		$($(" "+users+" ")).addClass("active").siblings().removeClass("active");
		$($(" "+users+" ")).css("display","flex").siblings().css("display","none");
		
		var group_id = users.substring(users.indexOf("-")+1);
		if(sessionStorage.getItem("detail-"+group_id) == null) {
			// 还没有请求过此用户组的详细信息，将页码设为1请求新的用户组的详细信息
			detail_page = 1;
			search_person_detail(group_id);
		} else {
			// 已经请求过此用户组，判断一下此用户组是否有新的消息
			var cur = sessionStorage.getItem("new-person-notice");
			if(cur != "null") {
				cur = JSON.parse(cur);
				group_id = parseInt(group_id);
				if(cur.indexOf(group_id) != -1) {
					refresh_person_detail(group_id);
				}
			}
		}
		
		// 点击用户组后，向服务器请求，将该用户在用户组中的新消息状态设置为无新消息
		$.ajax({
			url: "api/message_read",
			type: "GET",
			data: {
				group_id: group_id,
			},
			success: res => {
				if(res.code==1) {
					// 标记成功，消除小红点
					$("a[href='#group-"+group_id+"']").find("img").addClass("alpha-0");
					
					// 当私信中所有的小红点都被删除时，则外部红点也应该被删除
					var all_person = $("#person-list").children(".list-item");
					var all_read = $("#person-list").find(".list-item").children(".alpha-0");
					if(all_person.length == all_read.length) {
						$("#person-notice").find("img").addClass("alpha-0");
					}
					
					// 当交易消息中的小红点都被删除时，外部红点也应该删除
					var all_trade = $("#trade-list").children(".list-item");
					all_read = $("#trade-list").find(".list-item").children(".alpha-0");
					if(all_trade.length == all_read.length) {
						$("#trade-notice").find("img").addClass("alpha-0");
					}
				}
			},
			dataType: "json"
		});
	});
}

/**
 * 发送消息
 * @param obj
 * @returns
 */
function send(obj) {
	// 待发送的消息内容
	var text = $(obj.parentNode.parentNode).find("textarea")
	var content = text.val();
	
	// 获取对应的用户组编号
	var id = $(obj.parentNode.parentNode.parentNode).attr("id");
	var group_id = id.substring(id.indexOf("-")+1);
	
	// 根据用户组编号获取接收方邮箱账号
	var res = JSON.parse(sessionStorage.getItem("group-"+group_id));
	var address = res.sender.id;
	
	// 获取消息类型
	var type = event.target.dataset.type;
	// 获取自己的邮箱账号
	var sender = sessionStorage.getItem("user_id");
	// 发送消息
	socket.send(createMessage(sender, address, "null", content, type));
	
	var temp = sessionStorage.getItem("temp");
	if(temp == null || temp == undefined || temp == "null") {
		temp = [];
		temp[temp.length] = 0;
	} else {
		temp = JSON.parse(temp);
		temp[temp.length] = 0;
	}
	sessionStorage.setItem("temp", JSON.stringify(temp));
	
	// 将消息内容写到展示框中
	var html = template("template-msg-isme", {
		head_image: res.sender.head_image,
		content: content,
		index: temp.length
	});
	
	$("#message-content-list-"+group_id).append(html);
	text.val("");
}