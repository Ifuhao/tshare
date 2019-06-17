/**
 * 显示我的收购
 * @param res
 * @returns
 */
function seekList(data) {
	var cellHtml = "";
	for (var i = 0; i < data.length; i++) {
		// 添加序号
		data[i].index = data[i].seek_id;
		cellHtml += template('template-seek', data[i]);
	}
	$('#seek-list').append(cellHtml);
}

function auto_seek() {
	var index = event.target.dataset.index;
	sessionStorage.setItem("seek_id", index);
	
	var title = $("#seek-title-"+index).find("p").html();
	var description = $("#seek-description-"+index).find("p").html();
	
	$("#seek-modal-title").val(title);
	$("#seek-modal-description").val(description);
}

function seek_edit() {
	var seek_id = sessionStorage.getItem("seek_id");
	var title = $("#seek-modal-title").val();
	var description = $("#seek-modal-description").val();
	
	$.ajax({
		url: "/tshare/market/seek/api/edit",
		type: "GET",
		data: {
			seek_id: seek_id,
			title: title,
			description: description
		},
		success: res => {
			if(res.code==1) {
				$("#seek_edit .success").css("display", "block");
				setTimeout("$(\"#seek_edit\").modal(\"hide\"); $(\"#seek_edit .success\").css(\"display\", \"none\");", 1500);
			} else {
				alert(res.msg);
			}
		},
		dataType: "json"
	});
}

function delete_seek() {
	var seek_id = event.target.dataset.index;

	$.ajax({
		url: "/tshare/market/seek/api/edit",
		type: "GET",
		data: {
			seek_id: seek_id,
			is_delete: 1
		},
		success: res => {
			$("#seek-"+seek_id).remove();
		},
		dataType: "json"
	});
}
