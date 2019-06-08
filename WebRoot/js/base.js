/**
 * 输入框获取焦点时去除错误提示 
 */
$(document).ready(function() {
	$('input').on('focus', function() {
		$(this).removeClass('input-error');
	});
	$('.custom-file-input').on('focus', function() {
		$(this).next().removeClass('input-error');
	})
});

/**
 * 全站搜索
 */
function globalSearch(e) {
	var key = $('#global-search').val()
	alert(key)
	if (key == "")
		e.preventDefault()
}

/**
 * 退出
 */
function logout() {
	$.ajax({
		url: '/api/logout.php',
		type: 'GET',
		success: res => {
			if (res.code == 1)
				location.pathname = "index.html"
			else
				alert(res.msg)
		},
		error: (xhr, status, error) => console.log('[Status]', status, '\n[Error]', error),
		dataType: 'json',
		timeout: 5000
	})
}

function getExt(name) {
	var ext = "";
	var nameArray = name.split('.')
	if (nameArray.length > 1)
		switch (nameArray[nameArray.length - 1]) {
			case 'doc':
			case 'docx':
			case 'odt':
			case 'pages':
				ext = 'word'
				break
			case 'ppt':
			case 'pptx':
			case 'odp':
			case 'key':
				ext = 'ppt'
				break
			case 'xls':
			case 'xlsx':
			case 'csv':
			case 'ods':
			case 'numbers':
				ext = 'excel'
				break
			case 'pdf':
				ext = 'pdf'
				break
			case 'jpg':
			case 'png':
			case 'bmp':
			case 'gif':
			case 'svg':
				ext = 'picture'
				break
			case 'c':
			case 'h':
			case 'cpp':
			case 'hpp':
			case 'py':
			case 'java':
			case 'html':
			case 'htm':
			case 'js':
			case 'json':
			case 'css':
			case 'scss':
			case 'php':
			case 'm':
			case 'matlab':
			case 'v':
			case 'md':
			case 'ipynb':
				ext = 'code'
				break
			case 'mp3':
				ext = 'audio'
				break
			case 'avi':
				ext = 'video'
				break
			case 'txt':
			case 'rtf':
			case 'rtfd':
				ext = 'text'
				break
			default:
				ext = 'other'
		}
	else
		ext = 'other'
			
	return ext;
}