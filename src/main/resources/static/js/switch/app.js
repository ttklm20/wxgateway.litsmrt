(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
var PageTransitions;

PageTransitions = (function() {
  function PageTransitions(container) {
    var animEndEventNames;
    this.container = container;
    this.pages = this.container.children('div.pt-page');
    animEndEventNames = {
      'WebkitAnimation': 'webkitAnimationEnd',
      'OAnimation': 'oAnimationEnd',
      'msAnimation': 'MSAnimationEnd',
      'animation': 'animationend'
    };
    this.animEndEventName = animEndEventNames[Modernizr.prefixed('animation')];
    this.support = Modernizr.cssanimations;
    this.isAnimating = false;
    this.endCurrPage = false;
    this.endNextPage = false;
  }

  PageTransitions.prototype.init = function() {
    var _that, addDevicePage, connectPage, debugPage, errorPage, homePage;
    _that = this;
    homePage = $('#homePage');
    addDevicePage = $('#addDevicePage');
    connectPage = $('#connectPage');
    errorPage = $('#errorPage');
    debugPage = $('#debugPage');
    this.pages.each(function() {
      var page;
      page = $(this);
      return page.data('orginalClassList', page.attr('class'));
    });
    homePage.addClass('pt-page-current');
    touch.on('#functionList', 'tap', 'a', function(e) {
      e.preventDefault();
      $(this).parent().addClass('active');
      return window.setTimeout(function() {
        return location.href = event.target.href;
      }, 500);
    });
    touch.on('#deviceList', 'tap', 'a', function(e) {
      e.preventDefault();
      return _that.move(addDevicePage, connectPage, 'left');
    });
    touch.on('#linkDebug', 'tap', function(e) {
      e.preventDefault();
      return _that.move(homePage, debugPage, 'left');
    });
    touch.on('#linkAddDevice', 'tap', function(e) {
      e.preventDefault();
      return _that.move(homePage, addDevicePage, 'right');
    });
    touch.on('#cancelConnect', 'tap', function(e) {
      e.preventDefault();
      return _that.move(connectPage, addDevicePage, 'right');
    });
    touch.on('#reconnect', 'tap', function(e) {
      e.preventDefault();
      return _that.move(errorPage, connectPage, 'right');
    });
    touch.on('#rescan', 'tap', function(e) {
      e.preventDefault();
      return _that.move(errorPage, addDevicePage, 'right');
    });
    touch.on('#linkError', 'tap', function(e) {
      e.preventDefault();
      return _that.move(connectPage, errorPage, 'left');
    });
    touch.on('#debugPage', 'swiperight', function(e) {
      e.preventDefault();
      return _that.move(debugPage, homePage, 'right');
    });
    return touch.on('#addDevicePage', 'swipeleft', function(e) {
      e.preventDefault();
      return _that.move(addDevicePage, homePage, 'left');
    });
  };

  PageTransitions.prototype.onEndAnimation = function(outPage, inPage, cb) {
    this.endCurrPage = false;
    this.endNextPage = false;
    this.resetPage(outPage, inPage);
    this.isAnimating = false;
    if (cb && typeof cb === 'function') {
      return cb.apply(this);
    }
  };

  PageTransitions.prototype.resetPage = function(outPage, inPage) {
    outPage.attr('class', outPage.data('orginalClassList'));
    return inPage.attr('class', inPage.data('orginalClassList') + ' pt-page-current');
  };

  PageTransitions.prototype.move = function(currPage, nextPage, direction, callback) {
    var _that, inClass, outClass;
    _that = this;
    if (this.isAnimating) {
      return false;
    }
    this.isAnimating = true;
    nextPage.addClass('pt-page-current');
    outClass = '';
    inClass = '';
    switch (direction) {
      case 'left':
        outClass = 'pt-page-moveToLeft';
        inClass = 'pt-page-moveFromRight';
        break;
      case 'right':
        outClass = 'pt-page-moveToRight';
        inClass = 'pt-page-moveFromLeft';
    }
    currPage.addClass(outClass).on(this.animEndEventName, function() {
      currPage.off(_that.animEndEventName);
      _that.endCurrPage = true;
      if (_that.endNextPage) {
        return _that.onEndAnimation(currPage, nextPage, callback);
      }
    });
    return nextPage.addClass(inClass).on(this.animEndEventName, function() {
      nextPage.off(_that.animEndEventName);
      _that.endNextPage = true;
      if (_that.endCurrPage) {
        return _that.onEndAnimation(currPage, nextPage, callback);
      }
    });
  };

  return PageTransitions;

})();

$(function() {
  var pt;
  pt = new PageTransitions($("#ptMain"));
  return pt.init();
});



},{}]},{},[1]);
