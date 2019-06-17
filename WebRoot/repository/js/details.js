$(document).ready(function() {
	var index=sessionStorage.getItem('index')
	var data = JSON.parse(sessionStorage.getItem('res')).data[index]
	
	sessionStorage.setItem("base", data.url);
	sessionStorage.setItem("filename", data.name);
	
	// 判断一下文件类型，看是否需要生成预览图片
	if(data.contents == "") {
		// 文件类型
		var pre = ["word", "ppt", "pdf", "excel"];
		data.ext = getExt(data.name);
		
		if(pre.indexOf(data.ext) != -1) {
			// word类型文件需要先生成预览文件
			preview(data);
		} else if(data.ext == "picture") {
			// 图片文件可以直接显示
			show_image(data);
		}
	} else {
		// 文件夹类型，需要生成内部目录
		show_folder(data);
	}
});

/**
 * 显示文件夹的内部目录
 * @param data
 */
function show_folder(data) {
	var cellHtml = template("template-folder", data);
	$("#result").empty().append(cellHtml);
	
	// 最开始只显示顶层文件夹
	var top = "";
	var content = "";
	for(let key in data.contents) {
		var html = template("template-content-folder", {
			name: name,
			path: key
		});
		$("#folder-content").append(html);
		top = key;
		$("#"+top).empty();
		content = data.contents[key];
	}
	
	setContent(data.name, top+"-", content);
	$("#"+top).css("display", "block");
	$("#"+top).children("div").css("display", "block");
	$("#download").attr("title", "点击下载"+sessionStorage.getItem("filename"));
}

/**
 * 根据文件夹内容和父目录生成模板
 * @param parent
 * @param content
 * @returns
 */
function setContent(name, parent, content) {
	for(let key in content) {
		if(content[key] == 0) {
			// 这是一个文件
			var html = template("template-content-file", {
				ext: getExt(key),
				name: key,
				path: parent+key
			});
			$("#"+parent.substring(0, parent.length-1)).append(html);
		} else {
			var html = template("template-content-folder", {
				name: key,
				path: parent+key
			});
			$("#"+parent.substring(0, parent.length-1)).append(html);
			setContent(name, parent+key+"-", content[key]);
		}
	}
}

function open_dir() {
	var path = event.target.dataset.path;
	var is_open = document.getElementById(path).getAttribute("value");
	if(is_open == "no") {
		// 这一级文件夹没有打开，此次点击将会打开文件夹内部目录，并将打开标志设置为yes
		document.getElementById(path).setAttribute("value", "yes");
		// 打开这一级目录下的所有文件和文件夹
		$("#"+path).children("div").css("display", "block");
		// 将箭头变为向下的箭头
		$("#"+path).children("img").attr("src", "img/open.png");
	} else if(is_open == "yes") {
		// 这一级文件夹已经打开了，此次点击将会关闭文件夹内部目录，并将打开标志设置为no
		document.getElementById(path).setAttribute("value", "no");
		$("#"+path).children("div").css("display", "none");
		$("#"+path).children("img").attr("src", "img/notopen.png");
	}
}

function preview(data) {
	// 先生成预览图片
	$.ajax({
		url: "api/preview",
		type: "GET",
		data: {
			url: data.url
		},
		complete: () => {
			show_word(data);
		},
		timeout: 15000
	});
	
	// 等待动画
	$('.main').waitMe({
		effect: 'rotateplane',
		text:'正在加载...',
		bg: 'rgba(255,255,255,0.8)',
		color: ['#aaa', '#666'], //前者是字体颜色，后者是动画颜色
		fontSize: '16px'
	});
}

/**
 * 显示word文件的预览
 * @param data
 */
function show_word(data) {
	$(".main").waitMe("hide");
	show_file(data, "/upload/temp/"+data.url+".png");
}

/**
 * 显示图片文件预览
 * @param data
 */
function show_image(data) {
	show_file(data, "/upload/file/" + data.url);
}

/**
 * 显示单个文件
 * @param data 文件数据
 * @param img 预览图片路径
 */
function show_file(data, img) {
	var cellHtml = "";
	data.img = img;
	cellHtml = template("template-file", data);
	$("#result").empty().append(cellHtml);
	
	if(data.category == 1) {
		// 课外资料，需要显示描述框
		$("#file .file-text").css("display", "block");
	}
	
	setImageSize();
	$("#download").attr("title", "点击下载"+sessionStorage.getItem("filename"));
}

function setImageSize() {
	// 设置预览图片的大小
	var main_width = $(".main").width();
	var img_width = 0;
	var img_height = 0;
	
	var img = new Image();
	img.src = $("#preview img").attr("src");
	
	img.onload = function() {
		img_width = img.width;
		img_height = img.height;
		
		var rate = main_width*1.0/img_width;
		var img_height = img_height*rate;
		
		$("#preview img").css({"width":main_width, "height":img_height});
	}
}

function download_file() {
	var base = sessionStorage.getItem("base");
	if(base == null) {
		var index=sessionStorage.getItem('index');
		var data = JSON.parse(sessionStorage.getItem('res')).data[index];
		
		com_download(data.url, data.name);
	} else {
		var filename = sessionStorage.getItem("filename");
		com_download(base, filename);
	}
}

function download_folder(obj) {
	var path = event.target.dataset.path;
	if(path==undefined) {
		path = event.target.parentNode.dataset.path;
	}
	
	path = path.replace(/-/g, "/");
	path = path.substring(path.indexOf("/"));
	
	var index=sessionStorage.getItem('index');
	var data = JSON.parse(sessionStorage.getItem('res')).data[index];
	var base = data.url+path;
	
	var filename = base.substring(base.lastIndexOf("/")+1);
	sessionStorage.setItem("base", base);
	sessionStorage.setItem("filename", filename);
	
	// 被选中的这一行将会以其他颜色显示，其他的行将会显示绿色
	if(obj.getAttribute("value") == "no") {
		// 之前没有被选中
		$(obj).css("background", "rgba(219, 226, 236, 1)");
		
		// 将其他选中的文件或文件夹取消选中
		$("#folder-content span").each(function(i, e) {
			if(e != obj) {
				e.setAttribute("value", "no");
				$(e).css("background", "#fff");
			}
		});
		
		obj.setAttribute("value", "yes");
	} else if(obj.getAttribute("value") == "yes") {
		obj.setAttribute("value", "no");
		$(obj).css("background", "#fff");
	}
	
	// 如果被选中的是文件，考虑能否显示预览
	var ext = getExt(filename);
	var pre = ["word", "ppt", "pdf", "excel"];
	if(pre.indexOf(ext) != -1) {
		// 可预览的文件类型
		$.ajax({
			url: "api/preview",
			type: "GET",
			data: {url: base},
			complete: () => {
				$(".main").waitMe("hide");
				$("#preview img").attr("src", "/upload/temp/"+base+".png");
				$("#preview").css("display", "block");
				setImageSize();
			},
			timeout: 15000
		});
		// 等待动画
		$('.main').waitMe({
			effect: 'rotateplane',
			text:'正在转码...',
			bg: 'rgba(255,255,255,0.8)',
			color: ['#aaa', '#666'], //前者是字体颜色，后者是动画颜色
			fontSize: '16px'
		});
	} else if(ext == "picture") {
		// 图片，则直接显示
		$("#preview img").attr("src", "/upload/file/"+base);
		$("#preview").css("display", "block");
		setImageSize();
	}
	
	// 选中文件后在下载按钮上提示将下载该文件
	$("#download").attr("title", "点击下载"+filename);
}

function com_download(url, name) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "api/download?url="+url+"&filename="+name, true);
	xhr.responseType = "blob";
	xhr.onreadystatechange = ()=>{
		if (xhr.readyState==4) {
			if(xhr.status == 200) {
				download(xhr.response, name);
			} else if(xhr.status = 205) {
				alert("您需要重新登录");
			}
		}
	}
	xhr.send();
}
