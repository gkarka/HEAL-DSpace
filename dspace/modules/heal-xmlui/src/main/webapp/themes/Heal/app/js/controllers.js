/// <reference path="../lib/restangular.min.js" />
'use strict';

/* Controllers */

angular.module('dspace.controllers', [])
    .controller('bookController', ['$scope', '$location', 'bookService', 'toc', 'page', function ($scope, $location, bookService, toc, page) {
        $scope.toc = toc;
        $scope.page = page;
    
        $scope.isActive = function (pageRange) {
            return page.number >= pageRange._start && page.number <= pageRange._end;
        };
        $scope.prev = function () {
            var num = new Number(page.number);
            $location.path(bookService.getPagePath(num - 1));
        };
        $scope.next = function () {
            var num = new Number(page.number);
            $location.path(bookService.getPagePath(num + 1));
        };
    }]);
    
