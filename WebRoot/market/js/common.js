function goto_search(key) {
	if(event.keyCode == 13) {
		if(key == "") {
			return;
		} else {
			sessionStorage.setItem("key", key);
			location = "/tshare/market/sale/search.html";
		}
	}
}