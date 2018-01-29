define(function (require, exports, module) {
    "use strict";
   

    module.exports.binding = function ($scope, ajax) {
            $scope.pagingBar = require('common-paging');
            $scope.queryData = { pageIndex: 1, pageSize: 2 };
            $scope.listCodeCate = ajax.listCodeCate();
            $scope.listCodeEntry = ajax.listCodeEntry('XL32');


            $scope.flushGrid = function () {
                $scope.data = ajax.page($scope.queryData, function (data) {
                    if (data.success) {
                        $scope.pagingBar.build($scope.queryData, $scope.flushGrid, data.content.totalPages);
                    }
                });
            };

        }

    module.exports.ajax = {
        page: {method: 'POST', url: 'list'},
        listCodeCate: {method: 'POST', url: 'list/code-cate'},
        listCodeEntry: { method: 'POST', url: 'list/code-entry' }
    };

});