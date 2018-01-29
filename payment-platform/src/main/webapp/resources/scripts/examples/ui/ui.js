define(function (require, exports, module) {
  "use strict";

  module.exports.binding = [{
    name: 'noop', requires: function ($scope) {
    
      /** begin 初始化demo用的数据**/
      $scope.listData = [
	    { id: 1, policyNo: 'HAN051EP0100004', type: '主险', name: "金瑞人生终身寿险分红型 B款",sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]},
	    { id: 2, policyNo: 'HAN051EP0100004', type: '附险', name: "金瑞人生重大疾病保险" ,sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]},
	    { id: 3, policyNo: 'TTN051EP0100111', type: '主险', name: "银福年年两全保险（分红型）" ,sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]},
	    { id: 4, policyNo: 'TTN051EP0100111', type: '附险', name: "银福年年加倍关爱重大疾病保险" ,sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]},
	    { id: 5, policyNo: 'XXV051EP0108880', type: '主险', name: "综合意外伤害保障计划（B款）" ,sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]},
	    { id: 6, policyNo: 'XXV051EP0108880', type: '附险', name: "安心住院费用医疗保险（A款）" ,sendType:[{type:"1",name:"邮寄"},{type:"2",name:"自取"}]}
      ];
      

      /** end 初始化demo用的数据**/
      
      $scope.selectArrayValue = [];
      $scope.selectSimpleValue = $scope.listData[4];
      
      /**事件函数**/
      $scope.clickEvent = function (item, event) {
        console.log(item)
        console.log(event)
        
        /** begin 用于级联选择的一个例子**/
        if (item.type == '主险') {
          if ($(event.target).hasClass("checked"))
            $("div[policy-no='" + item.policyNo + "']").addClass("checked");
          else
            $("div[policy-no='" + item.policyNo + "']").removeClass("checked");
        }
        /** end 用于级联选择的一个例子**/
        
        console.log("err:", $scope.checked);
      }
      
      /**变量监听**/
      $scope.$watch('selectArrayValue', function (newVal, oldVal) { console.log("watch $scope.selectArrayValue:", newVal); });
      $scope.$watch('selectSimpleValue', function (newVal, oldVal) { console.log("watch $scope.selectSimpleValue:", newVal); });
    }
  }];

  module.exports.route = [{
    url: '/dialog',
    templateUrl: 'dialog.html',
    controller: 'noop'
  }, {
    url: '/tree',
    templateUrl: 'tree.html',
    controller: 'noop'
  }, {
    url: '/select',
    templateUrl: 'select.html',
    controller: 'noop'
  }, {
    url: '/checkbox',
    templateUrl: 'checkbox.html',
    controller: 'noop'
  }, {
    url: '/radio',
    templateUrl: 'radio.html',
    controller: 'noop'
  }];
});