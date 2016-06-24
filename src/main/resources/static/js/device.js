/**
 * Created by wen on 15/4/13.
 */
(function() {
	var deviceId;
	var tokenId;
	deviceId = $('#did').val();
	tokenId = $('#tid').val();
	/* ----------socketio通讯方式调试方法---------------- */
	// alert("qqqq");
	var conn_stateStr = false;
	// 写日志
	function write(msg) {
		var div = document.getElementById('debug'), divDom = document
				.createElement('p');
		divDom.innerHTML = JSON.stringify(msg);
		div.appendChild(divDom);
	}

	XJSObject.setConfig({
				_debug : true
			});
	// alert("wwww");
	XJSObject.invoke('loadXJSAPILib', {
				type : 'remote',
				// host : 'http://127.0.0.1:6001',
				host : 'http://cm.xlink.cn:23777',
				appid : 2
			}, function(r) {
				// alert("加载成功");
				write('loadXJSAPILib');
				write(r);

				write("connect  id " + deviceId + " token:" + tokenId);
				XJSObject.invoke('connectXDevice', {
							deviceid : deviceId,
							appid : 2,
							token : tokenId
						}, function(r) {
							// alert("连接成功");
							write('connectXDevice ');
							write(r);
							conn_stateStr = true;

						});

				XJSObject.on('onXDeviceStateChange', function(r) {
							write('onXDeviceStateChange');
							write(r);
							if (r.deviceid == deviceId && r.state == 0) {
								alert("设备掉线..");
								conn_stateStr = false;
							}
						}).on('onRecvXDeviceData', function(r) {
							write('onRecvXDeviceData');
							write(r);
						})
			});

	// 发送数据
	function sendData(data) {
		if (!conn_stateStr) {
			alert("设备未连接上!");
			return;
		}
		XJSObject.invoke('sendXDeviceData', {
					deviceid : deviceId,
					appid : 2,
					token : tokenId,
					data : XJSObject.b64.encode(data)
				}, function(r) {
					alert("发送数据成功" + JSON.stringify(r));
					write('sendXDeviceData :' + data);
					write(r);
				});

	}

	// 这里写点击事件
	document.querySelector(".fun-1").addEventListener('touchstart',
			function(e) {
				sendData('1');
			});
	document.querySelector(".fun-2").addEventListener('touchstart',
			function(e) {
				sendData('2');
			});
	document.querySelector(".fun-3").addEventListener('touchstart',
			function(e) {
				sendData('3');
			});
	document.querySelector(".fun-4").addEventListener('touchstart',
			function(e) {
				sendData('4');
			});
	document.querySelector(".fun-5").addEventListener('touchstart',
			function(e) {
				sendData('5');
			});
	document.querySelector(".fun-6").addEventListener('touchstart',
			function(e) {
				sendData('6');
			});
	document.querySelector(".fun-7").addEventListener('touchstart',
			function(e) {
				sendData('7');
			});
	document.querySelector(".fun-8").addEventListener('touchstart',
			function(e) {
				sendData('8');
			});
	document.querySelector(".fun-9").addEventListener('touchstart',
			function(e) {
				sendData('9');
			});
	/*---------------------- 调试结束 -----------------------*/

})();