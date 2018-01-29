define(function (require, exports, module) {
  "use strict";
  var paging=require('common-paging');
  module.exports.binding = [{
    name: 'queryCtrl', requires: function ($scope, ajax, $routeParams, $rootScope) {

      var ui = require('common-ui');
      ui.init(document);

      $scope.pagingBar = paging.init();

      $rootScope.listCodeCate = ajax.query('list/code-cate');
      $rootScope.listCodeCateChangeFn = function () {
        $rootScope.listCodeEntry = ajax.query('list/code-entry', $scope.queryData.codeCate);
      }

      $scope.flushGrid = function () {
        $scope.data = ajax.query('list', $scope.queryData, function (data) {
          if (data.success) {
            $scope.pagingBar.build($scope.queryData, $scope.flushGrid, data.content.totalPages);
          }
        });
      };

      $scope.del = function (id) {
        ajax.remove({ id: id }, function (ret) { bootbox.alert(ret.errMsg); });
        $scope.flushGrid();
      };
      $scope.ngClickd = function () {
        console.log( $scope.checked);
      }
    }
  },

  {
    name: 'editCtrl', requires: function ($scope, ajax, $routeParams, $rootScope) {
      $scope.product = ajax.get({ id: $routeParams.id });

      $scope.save = function () {
        if ($scope.product.content.id) {
          $scope.retValue = ajax.patch($scope.product.content);
        } else {
          $scope.retValue = ajax.save($scope.product.content)
        }
      }
    }
  },

  {
    name: 'detailCtrl', requires: function ($scope, ajax, $routeParams) {
      $scope.product = ajax.get({ id: $routeParams.id });
    }
  }];

  module.exports.route = [{
    url: '/query',
    templateUrl: 'query.html',
    controller: 'queryCtrl'
  }, {
    url: '/detail/:id',
    templateUrl: 'detail.html',
    controller: 'detailCtrl'
  }, {
    url: '/edit/:id',
    templateUrl: 'edit.html',
    controller: 'editCtrl'
  }];
});