(function (window, angular, undefined) {
  'use strict';

  var $resourceMinErr = angular.$$minErr('$resource');

  var MEMBER_NAME_REGEX = /^(\.[a-zA-Z_$][0-9a-zA-Z_$]*)+$/;

  function isValidDottedPath(path) {
    return (path != null && path !== '' && path !== 'hasOwnProperty' &&
        MEMBER_NAME_REGEX.test('.' + path));
  }

  function lookupDottedPath(obj, path) {
    if (!isValidDottedPath(path)) {
      throw $resourceMinErr('badmember', 'Dotted member path "@{0}" is invalid.', path);
    }
    var keys = path.split('.');
    for (var i = 0, ii = keys.length; i < ii && obj !== undefined; i++) {
      var key = keys[i];
      obj = (obj !== null) ? obj[key] : undefined;
    }
    return obj;
  }

  function shallowClearAndCopy(src, dst) {
    dst = dst || {};

    angular.forEach(dst, function (value, key) {
      delete dst[key];
    });

    for (var key in src) {
      if (src.hasOwnProperty(key) && !(key.charAt(0) === '$' && key.charAt(1) === '$')) {
        dst[key] = src[key];
      }
    }

    return dst;
  }

  angular.module('ngResource', ['ng']).
    factory('$resource', ['$http', '$q', function ($http, $q) {

      var DEFAULT_ACTIONS = {
        'get': { method: 'GET' ,url:'./:id'},
        'patch': { method: 'PATCH'},
        'save': { method: 'POST' },
        'query': { method: 'POST' },
        'remove': { method: 'DELETE', url: './:id' },
        'delete': { method: 'DELETE', url: './:id' }
      };
      var noop = angular.noop,
          forEach = angular.forEach,
          extend = angular.extend,
          copy = angular.copy,
          isObject = angular.isObject,
          isFunction = angular.isFunction;

      function encodeUriSegment(val) {
        return encodeUriQuery(val, true).
          replace(/%26/gi, '&').
          replace(/%3D/gi, '=').
          replace(/%2B/gi, '+');
      }


      function encodeUriQuery(val, pctEncodeSpaces) {
        return encodeURIComponent(val).
          replace(/%40/gi, '@').
          replace(/%3A/gi, ':').
          replace(/%24/g, '$').
          replace(/%2C/gi, ',').
          replace(/%20/g, (pctEncodeSpaces ? '%20' : '+'));
      }

      function Route(template, defaults) {
        this.template = template;
        this.defaults = defaults || {};
        this.urlParams = {};
      }

      Route.prototype = {
        setUrlParams: function (config, params, actionUrl) {
          var self = this,
              url = actionUrl || self.template,
              val,
              encodedVal;

          var urlParams = self.urlParams = {};
          forEach(url.split(/\W/), function (param) {
            if (param === 'hasOwnProperty') {
              throw $resourceMinErr('badname', "hasOwnProperty is not a valid parameter name.");
            }
            if (!(new RegExp("^\\d+$").test(param)) && param &&
                 (new RegExp("(^|[^\\\\]):" + param + "(\\W|$)").test(url))) {
              urlParams[param] = true;
            }
          });
          url = url.replace(/\\:/g, ':');

          params = params || {};
          forEach(self.urlParams, function (_, urlParam) {
            val = params.hasOwnProperty(urlParam) ? params[urlParam] : self.defaults[urlParam];
            if (angular.isDefined(val) && val !== null) {
              encodedVal = encodeUriSegment(val);
              url = url.replace(new RegExp(":" + urlParam + "(\\W|$)", "g"), function (match, p1) {
                return encodedVal + p1;
              });
            } else {
              url = url.replace(new RegExp("(\/?):" + urlParam + "(\\W|$)", "g"), function (match,
                  leadingSlashes, tail) {
                if (tail.charAt(0) == '/') {
                  return tail;
                } else {
                  return leadingSlashes + tail;
                }
              });
            }
          });

          // strip trailing slashes and set the url
          url = url.replace(/\/+$/, '') || '/';
          // then replace collapse `/.` if found in the last URL path segment before the query
          // E.g. `http://url.com/id./format?q=x` becomes `http://url.com/id.format?q=x`
          url = url.replace(/\/\.(?=\w+($|\?))/, '.');
          // replace escaped `/\.` with `/.`
          config.url = url.replace(/\/\\\./, '/.');


          // set params - delegate param encoding to $http
          forEach(params, function (value, key) {
            if (!self.urlParams[key]) {
              config.params = config.params || {};
              config.params[key] = value;
            }
          });
        }
      };


      function resourceFactory(url, paramDefaults, actions) {
        var route = new Route(url);

        actions = extend({}, DEFAULT_ACTIONS, actions);

        function extractParams(data, actionParams) {
          var ids = {};
          actionParams = extend({}, paramDefaults, actionParams);
          forEach(actionParams, function (value, key) {
            if (isFunction(value)) { value = value(); }
            ids[key] = value && value.charAt && value.charAt(0) == '@' ?
              lookupDottedPath(data, value.substr(1)) : value;
          });
          return ids;
        }

        function defaultResponseInterceptor(response) {
          return response.resource;
        }

        function Resource(value) {
          shallowClearAndCopy(value || {}, this);
        }

        forEach(actions, function (action, name) {
          var hasBody = /^(POST|PUT|PATCH)$/i.test(action.method);
          Resource[name] = function (a0, a1, a2, a3) {
            //uri/params/data/callback
            //params/data/callback
            //params/callback
            //callback


            var params = {}, data, callback;

            switch (arguments.length) {
              case 4:
                callback = a3;
              case 3:
                if (isFunction(a2)) {
                  callback = a2;
                } else {
                  params = a2;
                }
              case 2:
                if (isFunction(a1)) {
                  callback = a1;
                } else {
                  if (hasBody) data = a1;
                  else params = a1
                }
              case 1:
                if (isFunction(a0)) callback = a0;
                else if (angular.isString(a0)) action.url = a0;
                else if (hasBody) data = a0;
                else params = a0
                break;
              case 0: break;
              default:
                throw $resourceMinErr('badargs',
                  "Expected up to 5 arguments [uri,params, data, callback], got {0} arguments",
                  arguments.length);
            }

            var isInstanceCall = this instanceof Resource;
            var value = isInstanceCall ? data : (action.isArray ? [] : new Resource(data));
            var httpConfig = {};
            var responseInterceptor = action.interceptor && action.interceptor.response ||
                                      defaultResponseInterceptor;
            var responseErrorInterceptor = action.interceptor && action.interceptor.responseError ||
                                      undefined;

            forEach(action, function (value, key) {
              if (key != 'params' && key != 'isArray' && key != 'interceptor') {
                httpConfig[key] = copy(value);
              }
            });

            if (hasBody) httpConfig.data = { timestamp: new Date().getTime(), content: data };
            route.setUrlParams(httpConfig,
                               extend({}, extractParams(data, action.params || {}), params),
                               action.url);
            var promise;
            var isValidation = false;

            //校验
            if (name != 'getValidationRules' && httpConfig.data) {
              var intPath = action.url || '/';
              var intMethod = httpConfig['method'];
              var vr = Resource['validationRules'];
              var vt = Resource['valiUtils'];
              if (vr && vt) {
                for (var index = 0; index < vr.length; index++) {
                  var r = vr[index];
                  if (r.intMethod == intMethod && r.intPath == intPath) {
                    var pass = vt(data, r);
                    if (!(pass === true)) {
                      return { timestamp: new Date().getTime(), success: false, errCode: 'Request.ValidateErr', errMsg: '校验错误', content: null, validateErrs: pass };
                    }
                  }
                }
              }
            }
            //校验完成
            var promise = $http(httpConfig).then(function (response) {
              var data = response.data,
                promise = value.$promise;

              if (data) {
                if (angular.isArray(data) !== (!!action.isArray)) {
                  throw $resourceMinErr('badcfg',
                      'Error in resource configuration. Expected ' +
                      'response to contain an {0} but got an {1}',
                    action.isArray ? 'array' : 'object',
                    angular.isArray(data) ? 'array' : 'object');
                }
                if (action.isArray) {
                  value.length = 0;
                  forEach(data, function (item) {
                    if (typeof item === "object") {
                      value.push(new Resource(item));
                    } else {
                      value.push(item);
                    }
                  });
                } else {
                  shallowClearAndCopy(data, value);
                  value.$promise = promise;
                }
              }

              value.$resolved = true;

              response.resource = value;

              return response;
            }, function (response) {//http错误
              value.$resolved = true;
              response.data = {
                timestamp: new Date().getTime,
                success: false,
                errCode: response.status,
                errMsg: response.statusText
              };
              if (callback) {
                (callback)(response);
              } else {
                bootbox.alert('数据请求失败，错误代码：HTTP-' + response.status)
              }
              return $q.reject(response);
            });
            promise = promise.then(
                function (response) {
                  var v = responseInterceptor(response);
                  if (callback) {
                    (callback)(v, response.headers);
                  } else {
                    if (v.success === false) {
                      bootbox.alert('数据返回失败：' + v.errMsg + '，错误代码：' + v.errCode);
                      //return $q.reject(response);
                    }
                  }
                  
                  return v;
                },
                responseErrorInterceptor);

            if (!isInstanceCall) {
              // we are creating instance / collection
              // - set the initial promise
              // - return the instance / collection
              value.$promise = promise;
              value.$resolved = false;

              return value;
            }

            // instance call
            return promise;
          };


          Resource.prototype['$' + name] = function (params, callback) {
            if (isFunction(params)) {
              callback = params; params = {};
            }
            var result = Resource[name].call(this, params, this, callback);
            return result.$promise || result;
          };
        });

        Resource.bind = function (additionalParamDefaults) {
          return resourceFactory(url, extend({}, paramDefaults, additionalParamDefaults), actions);
        };

        return Resource;
      }

      return resourceFactory;
    }]);
})(window, window.angular);