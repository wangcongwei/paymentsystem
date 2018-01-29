/**
* 总体angular框架模块，加载angular，jquery，bootstrap，以及插件等
* 该模块按需加载对应js、按需加载css等
*/
define(function (require, exports, module) {
  "use strict";
  var registerModule = function (m, defs, flag) {
    if (m && defs && flag) {
      if (!angular.isArray(defs)) {
        defs = [defs];
      }
      for (var index = 0; index < defs.length; index++) {
        var name = defs[index].name;
        var requires = defs[index].requires;
        if (flag == 'controller') {
          m.register.controller(name, requires);
        } else if (flag == 'directive') {
          m.register.directive(name, requires);
        }
      }
    }
  }

  module.exports = function (pageDefine) {
    var bindingDef = pageDefine.binding;
    var directiveDef = pageDefine.directive;
    var ajaxDef = pageDefine.ajax || {};
    var routeDef = pageDefine.route;

    if (angular.isFunction(bindingDef)) {
      bindingDef = { name: 'controller', requires: bindingDef };
    }

    require('angular-file-upload');
    require('common-resource');
    require('angular-route');
    require('angular-lazyload');


    angular.module('ie7support', []).config(function ($sceProvider) {
      // Completely disable SCE to support IE7.
      $sceProvider.enabled(false);
    });

    var mainModule = angular.module('MainModule', ['angular-lazyload', 'ngResource', 'ngRoute', 'angularFileUpload']);

    // require('app-interceptor')(mainModule);//注册拦截器


    mainModule.run(['$lazyload', function ($lazyload) {
      $lazyload.init(mainModule);
      mainModule.register = $lazyload.register;



      mainModule.register.factory('ajax', ['$resource', function ($resource) {
        ajaxDef['getValidationRules'] = { method: 'GET', url: './vali.rule', isArray: true };
        var service = $resource('.', null, ajaxDef);
        service.validationRules = service.getValidationRules();
        var valiUtils = require('common-vali');
        service.valiUtils = valiUtils;
        return service;
      }]);

      //rigister controller
      registerModule(mainModule, bindingDef, 'controller');
      //register directive
      registerModule(mainModule, directiveDef, 'directive');

      //注册ui指令
      //这里避免preload并发的问题，采用require加载
      require('bootstrap');
      require('bootbox');
      var ui = require('common-ui');
      ui.directive(mainModule);
    }]);

    if (routeDef && routeDef.length > 0) {
      mainModule.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        for (var index = 0; index < routeDef.length; index++) {
          var route = routeDef[index];
          if (!route.controller) {
            route.controller = 'controller';
          }
          $routeProvider.when(route.url, {
            templateUrl: route.templateUrl,
            controller: route.controller
          });
        }
        $routeProvider.otherwise({
          redirectTo: routeDef[0].url
        });
        // $locationProvider.html5Mode(true);
      }]);
    }

    angular.bootstrap(document, ['MainModule']);
  };
});

