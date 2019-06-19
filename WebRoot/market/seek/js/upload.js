$(document).ready(function() {
});

function upload() {
	var isEmpty = false;
	$('#upload-body').find('input').add('#upload-body textarea').not(':hidden').not("#check-wish").each(function() {
		if ($(this).val() == "") {
			isEmpty = true;
			$(this).addClass('input-error');
		}
	});
	
	if(isEmpty) {
		return;
	}
	
	var title = $("#name").val();
	
	var description = $("#description").val();
	var check_wish = $("#check-wish").is(':checked');
	var wish = 0;
	if(check_wish) {
		wish = 1;
	}
	
	$.ajax({
		url: "api/upload",
		type: "GET",
		data: {
			title: title,
			description: description,
			wish: wish
		},
		success: res => {
			if(res.code == 1) {
				alert("发布成功");
			} else {
				alert(res.msg);
			}
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: "json"
	});
}