/**
 * 输入插件，包括chosen，icheck，datetime-picker。。。
 * auther:zhangdongfeng
 */

define(function (require, exports, module) {
  "use strict";
  //ngType
  var ngType = [{
    require: '?ngModel',
    name: 'ngType', requires: function ($parse) {
      return {
        link: function ($scope, element, attrs) {
          var beginArrayTag=attrs.ngModel.lastIndexOf('[');
          var endArrayTag=attrs.ngModel.lastIndexOf(']');
          if(endArrayTag!=-1&&endArrayTag == attrs.ngModel.length-1){
            var array=attrs.ngModel.substring(0,beginArrayTag);
          	$parse(array + "$$type").assign($scope, attrs['ngType']);
          }else{
            $parse(attrs.ngModel + "$$type").assign($scope, attrs['ngType']);
          }	
        }
      }
    }
  }];
  var ngChosen = [{
    name: 'chosen', requires: function ($parse) {
      return {
        restrict: 'A',
        require: '?ngModel',
        //terminal: true,
        link: function ($scope, element, attrs, ngModel) {
          var CHOSEN_OPTION_WHITELIST, NG_OPTIONS_REGEXP, isEmpty, snakeCase;
          CHOSEN_OPTION_WHITELIST = ['noResultsText', 'allowSingleDeselect', 'disableSearchThreshold', 'disableSearch', 'enableSplitWordSearch', 'inheritSelectClasses', 'maxSelectedOptions', 'placeholderTextMultiple', 'placeholderTextSingle', 'searchContains', 'singleBackstrokeDelete', 'displayDisabledOptions', 'displaySelectedOptions', 'width'];
          NG_OPTIONS_REGEXP = /^\s*(.*?)(?:\s+as\s+(.*?))?(?:\s+group\s+by\s+(.*))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+(.*?)(?:\s+track\s+by\s+(.*?))?$/;


          element = $(element);

          var chosen, defaultText, disableWithMessage, empty, initOrUpdate, match, options, origRender, removeEmptyMessage, startLoading, stopLoading, valuesExpr, viewWatch;

          //获取配置
          options = {};
          angular.forEach(CHOSEN_OPTION_WHITELIST, function (value) {
            if (attrs[value]) {
              var evalValue;
              if (attrs[value].toString().indexOf('{') != -1) {
                evalValue = $scope.$eval(attrs[value]);
              } else {
                evalValue = attrs[value].toString();
              }
              options[value.replace(/[A-Z]/g, function ($1) { return "_" + ($1.toLowerCase()); })] = evalValue;
            }
          });

          startLoading = function () {
            return element.addClass('loading').attr('disabled', true).trigger('chosen:updated');
          };

          stopLoading = function () {
            var disabled_value = false;
            if (attrs['ngDisabled']) {
              disabled_value = $scope.$eval(attrs['ngDisabled']);
            } else {
              disabled_value = element.is(':disabled');
            }
            return element.removeClass('loading').attr('disabled', disabled_value).trigger('chosen:updated');
          };

          chosen = null;
          initOrUpdate = function () {
            if (chosen) {
              return element.trigger('chosen:updated');
            } else {
              chosen = element.chosen(options).data('chosen');
              return defaultText = chosen.default_text;
            }
          };

          removeEmptyMessage = function () {
            return element.attr('data-placeholder', defaultText);
          };
          disableWithMessage = function () {
            return element.attr('data-placeholder', chosen.results_none_found).trigger('chosen:updated');
          };

          if (ngModel) {
            origRender = ngModel.$render;
            ngModel.$render = function () {
              origRender();
              return initOrUpdate();
            };
          } else {
            initOrUpdate();
          }

          attrs.$observe('ngDisabled', function (value) {
            return element.attr('disabled', value).trigger('chosen:updated');
          });

          if (attrs.ngOptions) {
            match = attrs.ngOptions.match(NG_OPTIONS_REGEXP);
            valuesExpr = match[7];
            $scope.$watchCollection(valuesExpr, function (newVal, oldVal) {
              if (!newVal) {
                return disableWithMessage();
              } else {
                initOrUpdate();
                removeEmptyMessage();
                stopLoading();
              }
            });
          }
          
          
        }
      }
    }
  }];
  
  var ngValid = [
     {   //校验标记
       name: 'ngValid', requires: function ($parse) {
         return {
           link: function ($scope, element, attrs) {
             $scope.$watch(attrs.ngModel, function ngBindWatchAction(valiValue) {
               $parse(attrs.ngValid).assign($scope, '');
             });
             $scope.$watch(attrs.ngValid, function ngBindWatchAction(valiValue) {
               var input = jQuery(element);
               if (valiValue) {
                 input.parent().parent().addClass("has-error")
                 input.prev().attr('data-original-title', valiValue).tooltip();
                 input.prev().removeClass('fa-check').addClass("fa-warning");
               } else {
                 input.parent().parent().removeClass("has-error");
                 input.prev().tooltip('destroy')
                 input.prev().removeClass('fa-warning').addClass("fa-check");
               }
             });
           }
         }
       }
     }];

  var ngFinishRenderFilter = [{
      name: 'onFinishRenderFilters', requires: function ($timeout) {
          return {
              restrict: 'A',
              link: function (scope, element, attr) {
                  if (scope.$last === true) {
                      $timeout(function () {
                          scope.$emit('ngRepeatFinished');
                      });
                  }
              }
          }
      }
  }];

  //checkbox    
  var icheck = [
      {   //普通icheck，用div模拟icheck，更新ngModel值
        name: 'icheck', requires: function ($parse) {
          return {
            restrict: 'A',
            replace: true,
            template: '<div class="icheckbox_minimal-blue"></div>',

            link: function ($scope, element, attrs) {
              //hover
              element.on('mouseover', function () {
                element.addClass('hover');
              });
              element.on('mouseout', function () {
                element.removeClass('hover');
              });

              //先停用click事件
              if (attrs['ngClick']) {
                element.off('click');
              }

              //启用click事件函数
              var enableClick = function () {
                if (attrs['ngClick']) {
                  var fn = $parse(attrs['ngClick'], /* expensiveChecks */ true);
                  var callback = function () {
                    fn($scope, { $event: event });
                  };
                  $scope.$apply(callback);
                }
              };

              if (attrs.group) {
                element.attr('group', attrs.group);
                element.attr('ng-model', attrs.ngModel);
                if (!element.hasClass('disabled')) {
                  element.on('click', function () {
                    element.toggleClass('checked');
                    var data = new Array();
                    var checked = element.hasClass('checked');
                    if (checked) {
                      jQuery('div[group-by="' + attrs.group + '"]').each(function () {
                        jQuery(this).addClass('checked');
                        data.push(jQuery(this).data().value);
                      });
                    } else {
                      jQuery('div[group-by="' + attrs.group + '"]').each(function () { jQuery(this).removeClass('checked'); });
                    }

                    $parse(attrs.ngModel).assign($scope, data);
                    $scope.$apply($scope.model);
                    enableClick();
                  });
                }
              } else if (attrs.groupBy) {
                var value = $parse(attrs.ngValue)($scope);

                element.data({ value: value });//添加数据到data中
                element.attr('group-by', attrs.groupBy);

                jQuery('div[group=' + attrs.groupBy + ']').removeClass('checked');
                if (!element.hasClass('disabled')) {
                  element.on('click', function () {
                    element.toggleClass('checked');
                    if (attrs.clickBefore)
                      enableClick();
                    var allChecked = true;
                    var data = new Array();

                    jQuery('div[group-by=' + attrs.groupBy + ']').each(function () {
                      var checked = jQuery(this).hasClass('checked');
                      if (!checked) {
                        allChecked = false;
                      } else {
                        data.push(jQuery(this).data().value);
                      }
                    });

                    var group = jQuery('div[group=' + attrs.groupBy + ']');
                    if (allChecked) {
                      group.addClass('checked');
                    } else {
                      group.removeClass('checked');
                    }
                    $parse(group.attr('ng-model')).assign($scope.$parent, data);
                    $scope.$parent.$apply($scope.$parent.model);
                    if (!attrs.clickBefore) enableClick();
                  });
                }
              } else {
                if ($scope[attrs.ngModel]) {
                    element.addClass('checked');
                } else {
                    element.removeClass('checked');
                }
                //simple checkbox
                if (!element.hasClass('disabled')) {
                  element.on('click', function () {
                    element.toggleClass('checked');
                    if (element.hasClass('checked'))
                      $parse(attrs.ngModel).assign($scope, element.data());
                    else
                      $parse(attrs.ngModel).assign($scope, null);
                    enableClick();
                  });
                }
              }

            }
          }
        }
      }];


  var iradio = [
       {   //用div模拟radio
         name: 'iradio', requires: function ($parse) {
           return {
             restrict: 'A',
             replace: true,
             template: '<div class="iradio_minimal-blue"></div>',

             link: function ($scope, element, attrs) {
               //hover
               element.on('mouseover', function () {
                 element.addClass('hover');
               });
               element.on('mouseout', function () {
                 element.removeClass('hover');
               });

               //先停用click事件
               if (attrs['ngClick']) {
                 element.off('click');
               }

               //启用click事件函数
               var enableClick = function () {
                 if (attrs['ngClick']) {
                   var fn = $parse(attrs['ngClick'], /* expensiveChecks */ true);
                   var callback = function () {
                     fn($scope, { $event: event });
                   };
                   $scope.$apply(callback);
                 }
               };
               
               //ngModel不为空 && ngModel和value值相同则缺省选中
               if ($parse(attrs['ngModel'])($scope) && $parse(attrs['ngModel'])($scope) == $parse(attrs['ngValue'])($scope)) {
                   element.addClass('checked');
               } else {
                   element.removeClass('checked');
               }
               //simple radio
               element.on('click', function () {
                   jQuery('div[ng-model="' + attrs.ngModel + '"]').removeClass("checked");
                   element.addClass('checked');
                   var data = $parse(attrs.ngValue)($scope);
                   $parse(attrs.ngModel).assign($scope, data);
                   $scope.$apply($scope.model);
                   enableClick();
               });

             }
           }
         }
       }];

  var itree = [
         {   //用div模拟radio
           name: 'itree', requires: function ($parse, ajax) {
             return {
               restrict: 'A',
               replace: true,
               template: '<ul class="tree tree-folder-select" role="tree">\
                              <li class="tree-branch hide" data-template="treebranch" role="treeitem" aria-expanded="false">\
                                <div class="tree-branch-header">\
                                  <button class="glyphicon icon-caret glyphicon-play"><span class="sr-only">Open</span></button>\
                                  <button class="tree-branch-name">\
                                    <span class="glyphicon icon-folder glyphicon-folder-close"></span>\
                                    <span class="tree-label"></span>\
                                  </button>\
                                </div>\
                                <ul class="tree-branch-children" role="group"></ul>\
                                <div class="tree-loader" role="alert">Loading...</div>\
                              </li>\
                              <li class="tree-item hide" data-template="treeitem" role="treeitem">\
                                <button class="tree-item-name">\
                                  <span class="glyphicon icon-item fueluxicon-bullet"></span>\
                                  <span class="tree-label"></span>\
                                </button>\
                              </li>\
                            </ul>',
               link: function ($scope, element, attrs) {
                 var decode = function (model) {
                   var data = [];
                   for (var index = 0; index < model.length; index++) {
                     var d = model[index];
                     data.push({
                       name: $parse(attrs['nodeText'])(d),
                       type: $parse(attrs['nodeType'])(d) || 'folder',
                       value: $parse(attrs['nodeValue'])(d) || d
                     });
                   }
                   return data;
                 }

                 var dataSource = function (options, callback) {
                   if (attrs['callback']) {
                     var fn = $parse(attrs['callback'], /* expensiveChecks */ true);
                     callback({ data: fn(options) }, 400);
                   } else {
                     ajax.query(attrs['url'], options['value'], function (resp) {
                       callback({
                         data: decode(resp.content)
                       }, 400);
                     });
                   }
                 };

                 var tree = $(element).tree({
                   dataSource: dataSource,
                   multiSelect: attrs['multiSelect'] === 'true',
                   cacheItems: !(attrs['cacheItems'] === 'false'),
                   folderSelect: !(attrs['folderSelect'] === 'false')
                 });
                 $(element).parent().addClass('fuelux');
                 $(element).on('click', function () {
                   var d = $(element).data('fu.tree').selectedItems();
                   var data = [];
                   $.each(d, function (index, item) {
                     data.push(item.value);
                   });

                   if (attrs.ngModel) {
                     $parse(attrs.ngModel).assign($scope, data);
                     $scope.$apply($scope.model);
                   }
                 });
               }
             }
           }
         }];

  var custDirective = function () {
    var def = [];
    def = def.concat(ngType);
    def = def.concat(ngValid);
    def = def.concat(ngFinishRenderFilter);

    //chosen
    require('angular-chosen.jquery');
    //require('angular-chosen');
    require('angular-chosen.css');
    require('angular-chosen-spinner.css');
    def = def.concat(ngChosen);
    require('icheck-css');
    def = def.concat(icheck);
    def = def.concat(iradio);
    require('fuelux');
    def = def.concat(itree);
    return def;
  }

  var datetimepicker = function (dom) {
    var datatimeElements = jQuery(dom).find('.form_datetime');
    var dataElements = jQuery(dom).find('.form_date');

    if (datatimeElements.length > 0 || dataElements.length > 0) {
      require('datetimepicker');
      require('datetimepicker-css');
      jQuery.fn.datetimepicker.dates['zh-CN'] = {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        today: "今日",
        suffix: [],
        meridiem: ["上午", "下午"],
        format: "yyyy-mm-dd" /*控制显示格式,默认为空，显示小时分钟*/
      };

      datatimeElements.datetimepicker({
        autoclose: true,
        isRTL: true,
        language: 'zh-CN',
        todayBtn: true,
        format: "yyyy-mm-dd hh:ii:ss",
        pickerPosition: "bottom-right"
      });

      dataElements.datetimepicker({
        autoclose: true,
        minView: 2,
        isRTL: true,
        language: 'zh-CN',
        todayBtn: true,
        format: "yyyy-mm-dd",
        pickerPosition: "bottom-right"
      });
    }
  };

  var timepicker = function (dom) {
    var timeNoSecondsPickerElements = jQuery(dom).find('.timepicker-no-seconds');
    var timepickerElements = jQuery(dom).find('.timepicker-24');
    if (timeNoSecondsPickerElements.length > 0 || timepickerElements.length > 0) {
      require('timepicker');
      require('timepicker-css');
      timeNoSecondsPickerElements.timepicker({
        autoclose: true,
        minuteStep: 5
      });

      timepickerElements.timepicker({
        defaultTime: "0:00:00",
        autoclose: true,
        minuteStep: 5,
        showSeconds: true,
        showMeridian: false
      });
    }
  };

  module.exports.init = function (dom) {
    datetimepicker(dom);
    timepicker(dom);
  };

  module.exports.directive = function (module) {
    var def = custDirective();
    for (var i = 0; i < def.length; i++) {
      module.register.directive(def[i].name, def[i].requires);
    }
  };
});
