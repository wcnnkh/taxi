function toLocation(geo) {
	return {
		"longitude": geo.position.lng,
		"latitude": geo.position.lat
	}
}

function getWebSocketRootPath() {
	console.log(location.protocol);
	if (location.protocol == "http:") {
		return "ws://" + window.location.host;
	} else {
		return "wss://" + window.location.host;
	}
}

function toTaxiMarker(taxi) {
	return new AMap.Marker({
		offset: new AMap.Pixel(-24, -24),
		icon: 'img/taxi48.png', // 添加 Icon 图标 URL
		position: new AMap.LngLat(taxi.location.longitude, taxi.location.latitude),
		title: '司机[' + taxi.id + ']',
		taxiId: taxi.id
	});
}

function toMyselfMarker(passengerId){
	return new AMap.Marker({
		offset: new AMap.Pixel(-27, -62),
		icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png', // 添加 Icon 图标 URL
		title: 'myself[' + passengerId + ']'
	});
}