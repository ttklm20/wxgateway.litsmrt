/**
 * Created by wen on 15/4/13.
 */
(function(){

    /* ----------socketio通讯方式调试方法----------------*/
alert("qqqq");

    function write(msg){
        var div = document.getElementById('msgArea'),
            divDom = document.createElement('p');
        divDom.innerHTML = JSON.stringify(msg);
        div.appendChild(divDom);
    }



    XJSObject.setConfig({
        _debug : true
    });
    alert("wwww");
    XJSObject.invoke('loadXJSAPILib', {
        type : 'remote',
//        host : 'http://127.0.0.1:6001',
        host : 'http://42.121.122.23:23777',
        appid : 2
    }, function(r){
        alert("加载成功");
        write('loadXJSAPILib');
        write(r)
        XJSObject.invoke('connectXDevice', {
            deviceid: 77777,
            appid : 2,
            token : 'ttt'
        }, function(r){
            write('connectXDevice')
            write(r)
            alert("连接成功");
            XJSObject.invoke('sendXDeviceData', {
                deviceid : 77777,
                appid : 2,
                token : 'ttt',
                data : XJSObject.b64.encode('test send')
            }, function(r){
                write('sendXDeviceData')
                write(r)
            });

//            XJSObject.invoke('disconnectXDevice', function(r){
//                write('disconnectXDevice')
//                write(r)
//                XJSObject.invoke('sendXDeviceData', {
//                    deviceid : 22222,
//                    data : XJSObject.b64.encode('2323fasd')
//                }, function(r){
//                    write('sendXDeviceData')
//                    write(r)
//                })
//            })

//                XJSObject.invoke('freeXJSAPILib', {
//                    deviceid: 111111
//                }, function(r){
//                    write('freeXJSAPILib');
//                    write(r)
//                    setTimeout(function(){
//                        XJSObject.invoke('connectXDevice', {
//                            deviceid: 111111
//                        }, function(r){
//                            write('connectXDevice')
//                            write(r);
//                        });
//                    }, 2000)
//                })


        });


        XJSObject.on('onXDeviceStateChange', function(r){
            write('onXDeviceStateChange')
            write(r);
        }).on('onRecvXDeviceData', function(r){
                write('onRecvXDeviceData')
                write(r);
            })
    });



    /*---------------------- 调试结束 -----------------------*/

})();