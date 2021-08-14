function toLocation(geo) {
	return {
		"longitude": geo.position.lng,
		"latitude": geo.position.lat
	}
}

function getWebSocketRootPath(){
	console.log(location.protocol);
	if(location.protocol == "http:"){
		return "ws://" + window.location.host;
	}else{
		return "wss://" + window.location.host;
	}
}