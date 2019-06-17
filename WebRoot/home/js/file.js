function fileList(data) {
	var cellHtml = "";
	for(var i=0;i<data.length;i++) {
		// 获取文件后缀名
		data[i].ext = getExt(data[i].name);
		data[i].index = i;
		cellHtml += template("template-file", data[i]);
		
		if(data[i].name.indexOf(".zip") != -1) {
			data[i].name = data[i].name.substring(0, data[i].name.indexOf(".zip"));
		}
	}
	
	$("#file-list").append(cellHtml);
	
	var res = {};
	res["data"] = data;
	res = JSON.stringify(res);
	sessionStorage.setItem("res", res);
}

function getDetails() {
	var index = event.target.dataset.index;
	sessionStorage.setItem("index", index);
	location = "../repository/details.html";
}