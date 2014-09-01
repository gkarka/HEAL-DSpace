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
    $routeProvider
        .when('/:handle/:bsname/:page?', {
            templateUrl: 'partials/partial1.html',
            controller: 'bookController',
            resolve: {
                toc: function (bookService) {
                    //bookService.init($route.current.params.handle, $route.current.params.bsname, $route.current.params.page);
                    return bookService.getToc();
                },
                page: function ($route, bookService) {
                    return {
                        number: $route.current.params.page,
                        imageUrl: bookService.getPageImageUrl($route.current.params.page)
                    };
                }                
            }
        })
    //$routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
    $routeProvider.otherwise({redirectTo: '/'});
}]).
config(['RestangularProvider', function (RestangularProvider) {    
    RestangularProvider.setBaseUrl('/heal-xmlui');
    //RestangularProvider.setResponseInterceptor(function (data, operation, what, url, response, deferred) {
}]);
