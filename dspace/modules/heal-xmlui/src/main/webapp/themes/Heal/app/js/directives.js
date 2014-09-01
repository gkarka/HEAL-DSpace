'use strict';

/* Directives */


angular.module('dspace.directives', ['ngRoute'])
    .directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }])
    .controller('pageLinkController', ['$scope', 'bookService', function ($scope, bookService) {
        $scope.getPath = function (page) {
            return bookService.getPagePath(page)
        };
    }])
    .directive('pageLink', function () {
        return {
            restrict: 'E',
            template: '<a ng-href="#{{getPath(page)}}">{{text}}</a>',
            controller: 'pageLinkController',
            scope: {
                page: '@',
                text: '@'
            }
        }
    });