/**
 * grid
 * auther:zhangdongfeng
 */

define(function (require, exports, module) {
    "use strict";
    module.exports.init = function () {
      return {
        inited: false,
        numbers:new Array(),
        data: {
          pageIndex: 0,
          previous: 0,
          next: 0,
          totalPages: 0,
          windowSize: 10,
          q: {},
          cb: null
        },
        build: function (queryData, callback, total, window_size) {
          this.data.q = queryData;
          this.data.cb = callback;
          this.data.pageIndex = queryData.pageIndex;
          this.data.pageIndex > total ? ((this.data.pageIndex = total) && (queryData.pageIndex = total)) : "";
          this.data.pageIndex < 1 ? ((this.data.pageIndex = 1) && (queryData.pageIndex = 1)) : "";

          this.data.totalPages = total;
          this.data.previous = this.data.pageIndex - 1 < 1 ? 1 : this.data.pageIndex - 1;
          this.data.next = this.data.pageIndex + 1 > this.data.totalPages ? this.data.totalPages : this.data.pageIndex + 1;
          //计算需要显示的页码列表
          var i = this.data.pageIndex - parseInt(this.data.windowSize / 2) < 1 ? 1 : this.data.pageIndex - parseInt(this.data.windowSize / 2);
          this.data.windowSize = window_size ? window_size + 1 : this.data.windowSize;
          this.numbers.length = 0;//clear
          for (var count = 1; i <= this.data.totalPages && count <= this.data.windowSize - 1; i++, count++) {
            this.numbers.push(i);
          }
          this.inited = true;
        },
        update: function (toPageIndex) {
          this.data.q.pageIndex = toPageIndex;
          this.build(this.data.q, this.data.cb, this.data.totalPages);
          this.data.cb();
        },
        //导出分页函数
        next: function (q) {
          this.update(this.data.pageIndex + 1);
        },
        prev: function () {
          this.update(this.data.pageIndex - 1);
        },
        nextWindow: function (q) {
          this.update(this.data.pageIndex + this.data.windowSize);
        },
        prevWindow: function () {
          this.update(this.data.pageIndex - this.data.windowSize);
        },
        first: function () {
          this.update(1);
        },
        last: function () {
          this.update(this.data.totalPages);
        },
        go: function (pageIndex) {
          this.update(pageIndex);
        }
      };
    };

});