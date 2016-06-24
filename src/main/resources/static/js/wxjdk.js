/*------------- 使用说明 ----------------------
 Created by wen on 15/4/12.

 页面引入socket.io.js 及 wxjdk.js

 XJSObject为暴露对象，公开方法：

 1.invoke 给外部调用入口方法

 2.on 给外部监听方法

 3.b64 base64操作对象（提供加密encode、解密deconde方法）

 ---------------------------------------------------*/

'use strict';
(function (win) {


    var __extends = function (base, ext) {

        if (base instanceof Object && ext instanceof Object) {
            var newObj = {};
            for (var k in base) {
                newObj[k] = base[k];
            }

            for (var e in ext) {
                newObj[e] = ext[e];
            }
            return newObj;
        }
        return ext;
    };

    //base64
    function Base64() {
        var r = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        this.encode = function (t) {
            var a, e, n, h, C, c, d, f = "", i = 0;
            for (t = o(t); i < t.length;)a = t.charCodeAt(i++), e = t.charCodeAt(i++), n = t.charCodeAt(i++), h = a >> 2, C = (3 & a) << 4 | e >> 4, c = (15 & e) << 2 | n >> 6, d = 63 & n, isNaN(e) ? c = d = 64 : isNaN(n) && (d = 64), f = f + r.charAt(h) + r.charAt(C) + r.charAt(c) + r.charAt(d);
            return f
        }, this.decode = function (o) {
            var a, e, n, h, C, c, d, f = "", i = 0;
            for (o = o.replace(/[^A-Za-z0-9\+\/\=]/g, ""); i < o.length;)h = r.indexOf(o.charAt(i++)), C = r.indexOf(o.charAt(i++)), c = r.indexOf(o.charAt(i++)), d = r.indexOf(o.charAt(i++)), a = h << 2 | C >> 4, e = (15 & C) << 4 | c >> 2, n = (3 & c) << 6 | d, f += String.fromCharCode(a), 64 != c && (f += String.fromCharCode(e)), 64 != d && (f += String.fromCharCode(n));
            return f = t(f)
        };
        var o = function (r) {
            r = r.replace(/\r\n/g, "\n");
            for (var o = "", t = 0; t < r.length; t++) {
                var a = r.charCodeAt(t);
                128 > a ? o += String.fromCharCode(a) : a > 127 && 2048 > a ? (o += String.fromCharCode(a >> 6 | 192), o += String.fromCharCode(63 & a | 128)) : (o += String.fromCharCode(a >> 12 | 224), o += String.fromCharCode(a >> 6 & 63 | 128), o += String.fromCharCode(63 & a | 128))
            }
            return o
        }, t = function (r) {
            for (var o, t, a = "", e = 0, n = o = t = 0; e < r.length;)n = r.charCodeAt(e), 128 > n ? (a += String.fromCharCode(n), e++) : n > 191 && 224 > n ? (t = r.charCodeAt(e + 1), a += String.fromCharCode((31 & n) << 6 | 63 & t), e += 2) : (t = r.charCodeAt(e + 1), c3 = r.charCodeAt(e + 2), a += String.fromCharCode((15 & n) << 12 | (63 & t) << 6 | 63 & c3), e += 3);
            return a
        }
    };
    //公用方法
    var Base = {
        _debug: true,//开发测试模式
        deviceList: [], //设置列表
        device: {
            deviceid: '123456789',
            token: 'token',
            appid: '111'
        },
        isConnectDeviced: false,
//        isPc : false,//pc ？
        isAlert: false,//弹出提示
        type: 'remote',//remote采用socketio通讯方式，local采用app webview调用方式
        isWeixin: true,//是否是微信
        base64: new Base64,//base64
        eventType: 'touchend',//点击触发事件，暂时监听此事件
        appid: null,

        log: function (msg, type) {
            if (this._debug) {
                if (msg instanceof Object)
                    msg = JSON.stringify(msg);
                if (this.isAlert) {
                    //if(msg instanceof Object)
                    msg = JSON.stringify(msg);
                    alert(msg);
                } else {
                    if (typeof toastr !== 'undefined')
                        toastr[type || 'success'](msg || '未知错误！');
                    else
                        console.log(msg);
                }
            }
        },
        getDom: function (id) {
            return document.getElementById(id);
        },
        getValueByTagName: function (name) {
            try {
                return document.getElementsByName(name)[0].value;
            } catch (e) {

            }
            return '';

        },

        actions: {
            evts: {},
            get: function (ns) {
                return this.evts[ns];
            },

            reg: function (ns, fn) {
                if (!this.evts[ns])
                    this.evts[ns] = fn;
                else {
                    var orfn = this.evts[ns];
                    if (orfn instanceof Array) {
                        orfn.push(fn);
                    } else {
                        var newfn = [orfn];
                        newfn.push(fn);
                        this.evts[ns] = newfn;
                    }

                }
                return this;
            }
        },
        listen: function (name, fn) {
            this.actions.reg('self_' + name, fn);
            return this;
        },

        fire: function (name, params) {
            var fns = this.actions.get('self_' + name);
            if (fns) {
                if (fns instanceof Array) {
                    for (var l = fns.length, i = 0; i < l; i++)
                        fns[i](params);
                } else {
                    fns(params);
                }
            } else {
                self.log('no event');
            }
            return this;
        },
        isPc: function () {
            var userAgentInfo = navigator.userAgent;
            var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
            var flag = true;
            for (var v = 0; v < Agents.length; v++) {
                if (userAgentInfo.indexOf(Agents[v]) > 0) {
                    flag = false;
                    break;
                }
            }
            this.isPc = flag;
            return flag;
        },
        _fn: function () {
            this.log('null function')
        },
        touchEvent: function (fn) {
            document.addEventListener(this.eventType, function (e) {
                var target = e.target, rel = target.getAttribute('rel');
                if (rel === null) {
                    rel = target.parentNode.getAttribute('rel');
                    target = target.parentNode;
                }
                if (rel) {
                    e.preventDefault();
                    self.fire('event-tc', {
                        rel: rel,
                        target: target
                    });
                }
            }, false);
        },
        setMsg: function (status, msg) {
            return {'status': status, 'msg': msg};
        },
        writeSend: function (msg) {
            this.write(1, msg);
        },
        writeAnswer: function (msg) {
            this.write(2, msg);
        },
        //输出回复数据
        write: function (type, msg) {
            var cls = 'send', text = '';
            if (type === 1) {
                text = 'send : ' + msg + '<span class="time">' + (+new Date) + '</span>';
            } else {
                cls = 'answer';
                text = 'answer : ' + msg + '<span class="time">' + (+new Date) + '</span>';
            }
            var div = document.createElement("div");
            div.className = cls;
            div.innerHTML = text;
            this.getDom('cl').appendChild(div);
        },
        loadXJSAPILibCallback: self._fn,//初始化回调
        onXDeviceStateChange: self._fn,//设备状态发⽣生变化
        onRecvXDeviceData: self._fn,//接收到设备发送过来的数据
        //根据deviceid获取设备
        getDevice: function (deviceid) {
            for (var l = this.devices.length, i = 0; i < l; i++) {
                if (this.deviceList[i].deviceid === deviceid) return this.deviceList[i];
            }
            return false;
        },
        //{ “deviceid” : 1234567890 , “token”:”xxxxxxxx” , “appid” : “xxxxxx” }
        addDevice: function (dev) {
            this.deviceList.push(dev);
            return this;
        },
        setDevice: function (device) {
            this.device = device;
            return this;
        },
        setAppid: function (id) {
            this.appid = id;
        },
        getAppid: function () {
            return this.appid;
        }

    };

    window['XJSObject'] = (function () {
        var self = __extends(Base, {

            //当前host
            host: 'http://127.0.0.1:6001',

            //设置当前host
            setHost: function (host) {
                this.host = host;
            },

            //连接socket，如果此host已经创建socket就返回
            conSocket: function (host) {
                var sock = this.getSocketObj();
                if (!sock)
                    this.socketInit(host);
                else
                    self.log('已初始化');
            },

            //连接多个服务地址
            socketList: {
                'http://123.com:23': {
                    uri: '',
                    socket: {},
                    isConnecting: true
                }
            },
            //删除socket连接
//        delSocket : function(obj){
//            if(self.socketList[obj.uri]){
//
//            }
//        },

            getSocketObj: function (_host) {
                _host = _host || this.host;
                return this.socketList[_host];
            },

            getSocket: function (_host) {
                var obj = this.getSocketObj(_host);
                if (obj) return obj;
                this.log('getSocket:未连接socket');
                return false;
            },

            setSocketList: function (obj) {
                var uri = obj.uri, _sock = this.getSocketObj(uri);
                if (_sock) {
                    for (var k in obj) {
                        if ('socket' !== k) {
                            _sock[k] = obj[k];
                        }
                    }
                } else {
                    this.socketList[uri] = obj;
                }
            },


            //连接socket
            socketInit: function (host) {
                if (typeof io === 'undefined') return false;
                if (this.getSocketObj(host)) return this.getSocketObj(host);
                if (host) {
                    /*------------- 内部广播事件 ------------------*/
                    self.listen('connect-socket', function (obj) {
                        self.setSocketList(obj);
                        self.loadXJSAPILibCallback && self.loadXJSAPILibCallback(self.setMsg(200, "connect socket success!"));
                    }).listen('disconnect-socket', function (obj) {
                        self.setSocketList(obj);
                        //self.disconnectCallBack()
                    }).listen('reconnect-socket', function (obj) {
                        self.setSocketList(obj);
                    }).listen('connect-failed', function (obj) {
//                        self.stopSocket(obj.uri);
                        self.loadXJSAPILibCallback && self.loadXJSAPILibCallback(self.setMsg(0, "connect socket fail!"));
                    });
                    /*------------- 内部广播事件结束 -----------------*/


                    var _socketio = new io.connect(host, {'reconnect': false, 'auto connect': false});
                    _socketio.on('disconnect', function (e) {//断开连接
                        self.fire('disconnect-socket', {uri: this.io.uri, isConnecting: false});
                    }).on('connect', function (e) {//socket连接
//                            self.log('socket init success!')
                        self.fire('connect-socket', {uri: this.io.uri, socket: this, isConnecting: true});
                    }).on('reconnect', function () {//恢复连接socket
                        self.fire('reconnect-socket', {uri: this.io.uri});
                    }).on('connect_failed', function (r) {
                        self.fire('connect-failed', {uri: this.io.uri});
                    }).on('device.onState', function (r) {//设备状态发⽣生变化

                        self.onXDeviceStateChange(r);
//                        self.socketDevState({
//                            host : this.io.uri,
//                            data : r
//                        });
                    }).on('device.onRecvData', function (r) {//接收到设备发送过来的数据
                        self.onRecvXDeviceData(r);
//                        self.socketDevReSend({
//                            host : this.io.uri,
//                            data : r
//                        });
                    })
                }
            },
            //设备状态回调
            socketDevState: function (d) {
                self.log(d);
            },
            //设备数据回调
            socketDevReSend: function (d) {
                self.log(d);
            },

            //断开socket
            stopSocket: function (host) {
                var sock = this.getSocket(host);
                if (sock && sock.isConnecting)
                    sock.socket.io.disconnect();
                else
                    this.log('stopSocket:没有连接socket!');
            },

            //重新连接socket
            reconnectSocket: function (host) {
                var sock = this.getSocket(host);
                if (sock && !sock.isConnecting)
                    sock.socket.io.reconnect();
                else {
                    if (sock) {
                        this.log('reSocket:已连接');
                    } else {
                        this.log('reSocket:请先初始化此socket!');
                    }
                }

            },

            socketEmit: function (command, params, fn) {
                var sock = this.getSocketObj();
                if (sock && sock.isConnecting)
                    sock.socket.emit(command, params, fn);
                else {
                    fn && fn(self.setMsg(0, '设先连接socket!'));
                    this.log('设先连接socket!');
                }
            },
            socketOn: function (command, fn) {
                var sock = this.getSocketObj();
                if (sock && sock.isConnecting)
                    sock.socket.on(command, fn);
                else
                    this.log('设先连接socket!');
            },
            //验证设备
            connectDevice: function (opt, fn) {
                this.socketEmit('device.connect', opt, function (r) {
                    if (r.status === 200) {
                        self.isConnectDeviced = true;
                    }
                    fn && fn(r);
                });
            },
            //向设备发送数据{ “data” : “数据base64编码“ , “appid” : “xxxxx” }
            sendDate: function (data, fn) {
                this.socketEmit('device.senddata', data, fn);
            },
            //断开连接（socket.io与设备的连接断开）
            closeConnect: function (data, fn) {
                this.socketEmit('device.disconnect', data, fn);
            }
        });
        return {

            /*
             * 初始化配置
             * @param {Object} params 参数
             */
            setConfig: function (params) {
                try {
                    self._debug = params._debug;
                } catch (e) {
                }
            },


            /*
             * invoke 给外部调用入口方法
             * @param {String} name  方法名称
             * @param {Object} params 参数
             * @param {Function} fn 回调方法
             */

            invoke: function (name, params, fn) {
                var len = arguments.length;
//                if(len === 1) fn = self._fn;
                if (len === 2) {
                    if (arguments[1] instanceof Function)
                        fn = params;
                }
                if (!fn) self._fn;
                switch (name) {
                    case 'loadXJSAPILib' ://加载所有资源
                        self.type = params.type || 'remote';
                        if (params.type === 'remote') {//⺴⽹网络通讯,通过socket.io与服务器通讯
                            self.setHost(params.host);
                            self.setAppid(params.appid);
                            self.loadXJSAPILibCallback = fn;
                            self.conSocket(self.host);
                            
                            // goto to app ....
                            href="ios://xxxxxx?callbackasdfasdf"
                            
                            //fn && fn({"status":200, "msg":"ok"});
                        } else if (params.type === 'local') {//本地APP调⽤用模式

                        }
                        break;
                    case 'freeXJSAPILib': //断开socket及设备
                        self.stopSocket();
                        self.isConnectDeviced = false;
                        fn && fn(self.setMsg(200, "ok"));
                        break;
                    case 'connectXDevice' : //连接设备
                        self.connectDevice(params, fn);
                        break;
                    case 'sendXDeviceData' : //发送数据到设备
                        if (self.isConnectDeviced) {
                            self.sendDate(params, fn || function (e) {
                                self.log(arguments);
                                self.writeAnswer(JSON.stringify(e));
                            });
                        } else {//未连接设备
                            fn && fn(self.setMsg(0, "unconnect device!"));
                        }
                        break;
                    case 'disconnectXDevice' : //断开与设备的连接
                        self.isConnectDeviced = false;
                        fn && fn(self.setMsg(0, "device: " + self.appid + " disconnecte"));
                        break;

                    default :
                        self.log('no function!');
                        break;

                }
                ;
                return this;
            },

            /*
             * on 给外部监听方法
             * @param {String} name  方法名称
             * @param {Function} fn 回调方法
             */
            on: function (name, fn) {
                switch (name) {
                    case 'onXDeviceStateChange' : //设备状态发⽣生变化
                        self.onXDeviceStateChange = fn;
                        break;

                    case 'onRecvXDeviceData' : //接收到设备发送过来的数据
                        self.onRecvXDeviceData = fn;
                        break;
                    default :
                        self.log('no function!');
                        break;
                }
                return this;
            },

            b64: self.base64
        }
    })();

})(window);