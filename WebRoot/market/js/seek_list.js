$(document).ready(function() {
	$.ajax({
		url: "api/seek",
		type: "GET",
		success: res => {
			setSeek(res);
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: "json"
	});
});

function setSeek(res) {
	var cellHtml = "";
	
	for(var i=0;i<res.length;i++) {
		
	}
}