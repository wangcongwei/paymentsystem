'use strict';

//seaj config
seajs.config({
  base: '/' + window.location.pathname.split("/")[1] + '/resources/',
  paths: {
    'plugins': 'global/plugins',
    'css': 'styles/css',
    'framework': 'scripts/framework',
    'ex': 'scripts/examples'
  },
  alias: {
    'angular-lazyload':
        'plugins/angularjs/angular-lazyload.min',
    //angular & plugins
    'angular':
        'plugins/angularjs/angular.min',
    'angular-touch':
        'plugins/angularjs/angular-touch.min',
    'angular-route':
        'plugins/angularjs/angular-route.min',
    'angular-file-upload':
        'plugins/angularjs/plugins/angular-file-upload/angular-file-upload.min',
    'angular-cookies':
        'plugins/angularjs/angular-cookies.min',
    'angular-sanitize':
        'plugins/angularjs/angular-sanitize.min',
    'angular-ui-utils-ieshiv.min':
        'plugins/angularjs/plugins/ui-utils-ieshiv.min',
    'angular-ui-utils.min':
        'plugins/angularjs/plugins/ui-utils.min',
    'angular-chosen.jquery':
        'plugins/angularjs/plugins/chosen/chosen.jquery',
    'angular-chosen':
        'plugins/angularjs/plugins/chosen/chosen.js',
    'angular-chosen.css':
        'plugins/angularjs/plugins/chosen/chosen.css',
    'angular-chosen-spinner.css':
        'plugins/angularjs/plugins/chosen/chosen-spinner.css',
    //jqurey & bootstrap plugins
    'jquery':
        'plugins/jquery.min',
    'bootstrap':
        'plugins/bootstrap/js/bootstrap.16columns.min',
    'fuelux':
        'plugins/fuelux/js/fuelux.min',
    'fuelux-css':
        'plugins/fuelux/css/fuelux.min.css',
    'icheck-css':
        'plugins/icheck/skins/minimal/blue.css',
    'datetimepicker':
        'plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker',
    'datetimepicker-css':
        'plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css',
    'timepicker':
        'plugins/bootstrap-timepicker/js/bootstrap-timepicker.min',
    'timepicker-css':
        'plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css',
    'bootbox':
        'plugins/bootbox/bootbox.min',
    'jquery.idTabs':
        'plugins/jquery-idtabs/jquery.idTabs',
    'jquery.idTabs-css':
         'plugins/jquery-idtabs/jquery.idTabs.css',
    'modernizr':
        'plugins/modernizr/modernizr-2.6.2',
    'app-init':
        'framework/app-init',
    'common-resource':
        'framework/common-resource',
    'common-paging':
         'framework/common-paging',
    'common-ui':
        'framework/common-ui',
    'common-ui-form':
        'framework/common-ui-form',
    'common-vali':
        'framework/common-vali'
  },
  preload: [
      'plugins/font-awesome/css/font-awesome.min.css',
      'plugins/bootstrap/css/bootstrap.16columns.min.css',
      'css/common.css',
      'css/page.css',
      'css/reset.css',
      'modernizr',
      'jquery',
      'angular'
  ],
  charset: 'utf-8'
});
// end seaj config
