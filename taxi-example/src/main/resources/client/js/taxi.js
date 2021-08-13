function initMap(taxiId, websocket) {
	var map = new AMap.Map('container', {
		zoom: 17,
		resizeEnable: true
	});

	// 同时引入工具条插件，比例尺插件和鹰眼插件
	AMap.plugin([
		'AMap.ToolBar',
		'AMap.Geolocation',
	], function() {
		// 在图面添加工具条控件，工具条控件集成了缩放、平移、定位等功能按钮在内的组合控件
		map.addControl(new AMap.ToolBar());

		// 在图面添加定位控件，用来获取和展示用户主机所在的经纬度位置
		map.addControl(new AMap.Geolocation());
	});

	AMap.plugin('AMap.Geolocation', function() {
		var geolocation = new AMap.Geolocation({
			// 是否使用高精度定位，默认：true
			enableHighAccuracy: true,
			// 设置定位超时时间，默认：无穷大
			timeout: 10000,
			// 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
			buttonOffset: new AMap.Pixel(10, 20),
			//  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
			zoomToAccuracy: true,
			//  定位按钮的排放位置,  RB表示右下
			buttonPosition: 'RB'
		})

		geolocation.getCurrentPosition()
		setInterval(function() {
			geolocation.getCurrentPosition()
		}, 5000)
		AMap.event.addListener(geolocation, 'complete', onComplete)
		AMap.event.addListener(geolocation, 'error', onError)

		var marker = new AMap.Marker({
			offset: new AMap.Pixel(-10, -10),
			icon: '//vdata.amap.com/icons/b18/1/2.png', // 添加 Icon 图标 URL
		});
		map.add(marker);

		function onComplete(data) {
			// data是具体的定位信息
			console.log(data)
			var position = [data.position.lng, data.position.lat];
			map.setCenter(position);
			map.setZoom(17);
			marker.setTitle(data.formattedAddress);
			marker.setPosition(position);
			
			var trace = {
				"id":taxiId,
				"location": toLocation(data)
			}
			//位置上报
			websocket.send(JSON.stringify(trace));
		}

		function onError(data) {
			console.log(data);
		}
	})
}

/**
 * 抢单
 * @param {Object} taxiId
 * @param {Object} orderId
 */
function grabOrder(taxiId, orderId){
	$.ajax({
		method: "POST",
		url: "../taxi/grab_order",
		data:JSON.stringify({
			"taxiId": taxiId,
			"orderId": orderId
		}),
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		success:function(data){
			console.log(data);
		}
	})
}

function confirmOrder(taxiId, orderId){
	$.ajax({
		method: "POST",
		url: "../taxi/confirm_order",
		data:JSON.stringify({
			"taxiId": taxiId,
			"orderId": orderId
		}),
		contentType: "application/json;charset=utf-8",
		dataType: "json",
		success:function(data){
			console.log(data);
		}
	})
}
