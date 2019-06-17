/**
 * 显示我的心愿单
 * @param res
 * @returns
 */
function wishList(data) {
	var cellHtml = "";
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].index = data[i].wish_id;
		cellHtml += template('template-wish', data[i]);
	}
	$('#wish-list').append(cellHtml);
	waterFall("wish-list", "wish-box");
}

function auto_wish() {
	var index = event.target.dataset.index;
	sessionStorage.setItem("wish_id", index);
	
	var title = $("#wish-title-"+index).find("p").html();
	var description = $("#wish-description-"+index).find("p").html();
	
	$("#wish-modal-title").val(title);
	$("#wish-modal-description").val(description);
}

function wish_edit() {
	var wish_id = sessionStorage.getItem("wish_id");
	var title = $("#wish-modal-title").val();
	var description = $("#wish-modal-description").val();
	
	$.ajax({
		url: "/tshare/market/wish/api/edit",
		type: "GET",
		data: {
			wish_id: wish_id,
			title: title,
			description: description
		},
		success: res => {
			if(res.code==1) {
				$("#wish_edit .success").css("display", "block");
				setTimeout("$(\"#wish_edit\").modal(\"hide\"); $(\"#wish_edit .success\").css(\"display\", \"none\");", 1500);
			} else {
				alert(res.msg);
			}
		},
		dataType: "json"
	});
}

function delete_wish() {
	var wish_id = event.target.dataset.index;

	$.ajax({
		url: "/tshare/market/wish/api/edit",
		type: "GET",
		data: {
			wish_id: wish_id,
			is_delete: 1
		},
		success: res => {
			$("#wish-"+wish_id).remove();
			waterFall("wish-list", "wish-box");
		},
		dataType: "json"
	});
}