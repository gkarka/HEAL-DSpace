'use strict';

var x2js = new X2JS();

// Declare app level module which depends on filters, and services
angular.module('dspace', [
  'ngRoute',
  'restangular',
  'dspace.filters',
  'dspace.services',
  'dspace.directives',
  'dspace.controllers'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/:bsname/:page', {
        templateUrl: 'partials/partial1.html',
        controller: 'pageController',
        resolve: {
            book: function ($route) {                
                var b = 
                {
                    bitstreamName: $route.current.params.bsname,
                    page: $route.current.params.page
                };
                return b;
            },
            toc: function ($route, Restangular) {
                var _toc = {};
                Restangular.one('/bitstream/handle/123456789/8/S0001098.xml').get()
                    .then(function (res) {
                        _toc.book = x2js.xml_str2json(res).book;
                    });
                return _toc;
            }
        }
    });
    //$routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
    $routeProvider.otherwise({redirectTo: '/'});
}]).
config(['RestangularProvider', function (RestangularProvider) {    
    RestangularProvider.setBaseUrl('/xmlui');
    //RestangularProvider.setResponseInterceptor(function (data, operation, what, url, response, deferred) {
}]);
