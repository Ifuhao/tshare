function select_head() {
	document.getElementById("head_image").click();
}

function person_edit() {
	var head = document.getElementById("head_image").files[0];
//	var text = document.getElementById("person-text").value;
	
	var reader = new FileReader();
	
	reader.readAsDataURL(head);
	reader.onloadend = function(element) {
		var data64 = element.target.result;
		
		var formdata = new FormData();
		formdata.append("head_image", data64);
		
		$.ajax({
			url: "api/edit_person",
			type: "POST",
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
}