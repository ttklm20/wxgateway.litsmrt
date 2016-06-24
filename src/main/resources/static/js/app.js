(function e(t, n, r) {
    function s(o, u) {
        if (!n[o]) {
            if (!t[o]) {
                var a = typeof require == "function" && require;
                if (!u && a)return a(o, !0);
                if (i)return i(o, !0);
                var f = new Error("Cannot find module '" + o + "'");
                throw f.code = "MODULE_NOT_FOUND", f
            }
            var l = n[o] = {exports: {}};
            t[o][0].call(l.exports, function (e) {
                var n = t[o][1][e];
                return s(n ? n : e)
            }, l, l.exports, e, t, n, r)
        }
        return n[o].exports
    }

    var i = typeof require == "function" && require;
    for (var o = 0; o < r.length; o++)s(r[o]);
    return s
})({
    1: [function (require, module, exports) {
        var App, Util;

        Util = require('./util').Util;

        App = (function () {
            function App() {
                this.homePage = Util.getById('homePage');
                this.debugPage = Util.getById('debugPage');
                this.addDevicePage = Util.getById('addDevicePage');
                this.connectPage = Util.getById('connectPage');
                this.errorPage = Util.getById('errorPage');
                this.mcDebugPage = new Hammer(this.debugPage);
                this.mcAddDevicePage = new Hammer(this.addDevicePage);
                this.mcFunctionList = new Hammer(Util.getById('functionList'));
                this.mcDeviceList = new Hammer(Util.getById('deviceList'));
                this.mcLinkDebug = new Hammer(Util.getById('linkDebug'));
                this.mcLinkAddDevice = new Hammer(Util.getById('linkAddDevice'));
                this.mcCancelConnect = new Hammer(Util.getById('cancelConnect'));
                this.mcReconnect = new Hammer(Util.getById('reconnect'));
                this.mcRescan = new Hammer(Util.getById('rescan'));
                this.mcLinkError = new Hammer(Util.getById('linkError'));
            }

            App.prototype.init = function () {
                var _that, movePages;
                _that = this;
                movePages = function (pages, direction) {
                    var reveseDirection;
                    reveseDirection = direction === 'left' ? 'right' : 'left';
                    pages[0].classList.add('page-move-to-' + direction);
                    pages[1].classList.add('page-current', 'page-move-from-' + reveseDirection);
                    return window.setTimeout(function () {
                        pages[0].classList.remove('page-current', 'page-move-to-' + direction);
                        return pages[1].classList.remove('page-move-from-' + reveseDirection);
                    }, 650);
                };
                this.mcFunctionList.on('tap', function (event) {
                    if (event.target && event.target.tagName === 'A') {
                        event.preventDefault();
                        event.target.parentNode.classList.add('active');
                        return window.setTimeout(function () {
                            return location.href = event.target.href;
                        }, 500);
                    }
                });
                this.mcDeviceList.on('tap', function (event) {
                    if (event.target && event.target.tagName === 'A') {
                        event.preventDefault();
                        return movePages([_that.addDevicePage, _that.connectPage], 'left');
                    }
                });
                this.mcLinkDebug.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.homePage, _that.debugPage], 'left');
                });
                this.mcLinkAddDevice.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.homePage, _that.addDevicePage], 'right');
                });
                this.mcCancelConnect.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.connectPage, _that.addDevicePage], 'right');
                });
                this.mcReconnect.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.errorPage, _that.connectPage], 'right');
                });
                this.mcRescan.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.errorPage, _that.addDevicePage], 'right');
                });
                this.mcLinkError.on('tap', function (event) {
                    event.preventDefault();
                    return movePages([_that.connectPage, _that.errorPage], 'left');
                });
                this.mcDebugPage.on('swiperight', function (event) {
                    return movePages([_that.debugPage, _that.homePage], 'right');
                });
                return this.mcAddDevicePage.on('swipeleft', function (event) {
                    return movePages([_that.addDevicePage, _that.homePage], 'left');
                });
            };

            return App;

        })();

        window.onload = function () {
            var app;
            app = new App;
            return app.init();
        };


    }, {"./util": 2}], 2: [function (require, module, exports) {
        var Util;

        Util = {
            getById: function (id) {
                return document.getElementById(id);
            }
        };

        exports.Util = Util;


    }, {}]
}, {}, [1]);
