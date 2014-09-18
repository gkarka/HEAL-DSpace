/// <reference path="../lib/restangular.min.js" />
'use strict';

/* Controllers */

angular.module('dspace.controllers', [])
    .controller('bookController', ['$scope', '$location', 'bookService', 'toc', 'page', function ($scope, $location, bookService, toc, page) {
        $scope.toc = toc;
        $scope.page = page;
        $scope.index = 0;

        $scope.isActive = function (pageRange, singlePageRange) {
            if (singlePageRange) {
                return page.number == singlePageRange._num;                
            }
            return page.number >= pageRange._start && page.number <= pageRange._end;
        };

        $scope.getPageStart = function (chapter) {
            if (chapter.page) return chapter.page._num;
            return chapter.pageRange._start;
        };

        $scope.prev = function () {
            if ($scope.index == 0) return;

            $scope.index = $scope.index - 1;
            var num = $scope.pages[$scope.index];
            $location.path(bookService.getPagePath(num));
        };
        $scope.next = function () {
            if ($scope.index + 1 == $scope.pages.length) return;

            $scope.index = $scope.index + 1;
            var num = $scope.pages[$scope.index];            
            $location.path(bookService.getPagePath(num));
        };

        $scope.$watch('toc.book', function() {            
            $scope.pages = _.flatten(_.map(toc.book.chapter, function (chapter) {
                var m;
                if (_.isArray(chapter.pageRange)) {
                    return _.map(chapter.pageRange, function (pageRange) {
                        return _.range(new Number(pageRange._start).valueOf(), new Number(pageRange._end).valueOf(), 1);
                    });
                }
                else if (chapter.page) {
                    var n = new Number(chapter.page._num).valueOf();
                    return [n];
                }
                else {
                    return _.range(new Number(chapter.pageRange._start).valueOf(), new Number(chapter.pageRange._end).valueOf(), 1);
                }
            }));

            var pnum = new Number(page.number).valueOf();
            if (pnum == 0) {
                $scope.index = 0;
            }
            else {
                $scope.index = $scope.pages.indexOf(pnum);
            }
            
        });        
        
    }]);
    
