define(function (require, exports, module) {
  "use strict";

  module.exports.binding = [{
    name: 'noop', requires: function ($scope) {
      $scope.parentVar='this is parentVar';
      $scope.innerTemplate='page-inner.html';
    }
  },{
    name:'inner',requires: function ($scope) {
      $scope.innerVar='this is innerVar';
    }
  }];

  module.exports.route = [{
    url: '/inner',
    templateUrl: 'page.html',
    controller: 'noop'
  }];
});