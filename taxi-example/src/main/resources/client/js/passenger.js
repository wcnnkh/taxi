function initMap(passengerId, websocket) {
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
			timeout: 5000,
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

		var selfMarker = new AMap.Marker({
			offset: new AMap.Pixel(-10, -10),
			icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png', // 添加 Icon 图标 URL
			title: '乘客[' + passengerId + ']'
		});
		map.add(selfMarker);

		var markers = new Array();
		//清除所有的点 
		function clearMarkers() {
			map.remove(markers);
			markers = new Array();
		}

		/**
		 * 刷新附近车辆
		 * @param {Object} data 当前位置信息
		 */
		function refreshNearbyTaxi(data) {
			$.ajax({
				method: "POST",
				url: "../passenger/nearby_taxi",
				data: JSON.stringify({
					"taxiStatus": {
						"working": true
					},
					"location": toLocation(data)
				}),
				contentType: "application/json;charset=utf-8",
				dataType: "json",
				success: function(data) {
					$("#nearby-taxi-log").html(new Date() + ":<br/>" + JSON.stringify(data));
					clearMarkers();
					data.data.forEach((taxi) => {
						var marker = toMarker(taxi);
						markers.push(markers);
						map.add(marker);
					})
				}
			})
		}

		function toMarker(taxi) {
			return new AMap.Marker({
				map: map,
				offset: new AMap.Pixel(-13, -30),
				icon: '//vdata.amap.com/icons/b18/1/2.png', // 添加 Icon 图标 URL
				position: new AMap.LngLat(taxi.location.longitude, taxi.location.latitude),
				title: '司机[' + taxi.id + ']'
			});
		}

		function onComplete(data) {
			$("#geo-log").html(new Date() + ":<br/>" + JSON.stringify(data));
			//这种写法极端情况下会出现标记点混乱的问题(如：上一个还未标记完就被下一次清空了)
			refreshNearbyTaxi(data);

			// data是具体的定位信息
			var position = [data.position.lng, data.position.lat];
			map.setCenter(position);
			map.setZoom(17);
			selfMarker.setTitle(data.formattedAddress);
			selfMarker.setPosition(position);

			try {
				var trace = {
					"id": passengerId,
					"location": toLocation(data)
				}
				//位置上报
				websocket.send(JSON.stringify(trace));
			} catch (e) {
				layer.open({
					title: '异常提示',
					content: "位置上报异常" + e.message
				});
			}

		}

		function onError(data) {
			console.log(data);
			$("#geo-log").html(new Date() + ":<br/>" + JSON.stringify(data));
		}

		$("button.post-order").click(function() {
			try{
				geolocation.getCurrentPosition(function(status, result) {
					if (status == "error") {
						layer.msg("下单失败，无法获取位置信息[" + result.message + "]");
						return;
					}
					$.ajax({
						method: "POST",
						url: "../passenger/post_order",
						data: JSON.stringify({
							"passengerId": passengerId,
							"startLocation": toLocation(result)
						}),
						contentType: "application/json;charset=utf-8",
						dataType: "json",
						success: function(data) {
							console.log(data);
							layer.open({
								title: '下单成功提示',
								content: "订单号：" + data.data.id
							});
						},
						error: function(error){
							layer.msg("下单失[" + error + "]");
						}
					})
				})
			}catch(e){
				layer.msg("代码错误：" + e);
			}
		})
	})
}
