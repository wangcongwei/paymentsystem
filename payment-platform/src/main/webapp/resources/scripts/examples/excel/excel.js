define(function (require, exports, module) {
    "use strict";
   

    module.exports.binding = function ($scope, ajax) {
            $scope.pagingBar = require('common-paging');
            $scope.queryData = { pageIndex: 1, pageSize: 4, hello: 1433163217916 ,hello$$type:'timestamp'};
           
            $scope.flushGrid = function () {
                $scope.data = ajax.page($scope.queryData, function (data) {
                    if (data.success) {
                        $scope.pagingBar.build($scope.queryData, $scope.flushGrid, data.content.page.totalPages);
                    }
                });
            };

        }

    module.exports.ajax = {
        page: {method: 'POST', url: 'list'}
    };


});