/// <reference path="../lib/restangular.min.js" />
'use strict';

/* Controllers */

angular.module('dspace.controllers', []).
  controller('pageController', ['$scope', 'book', 'toc', function ($scope, book, toc) {
      $scope.book = book;
      $scope.toc = toc;
      
      //book.then(function (b) { $scope.book.toc = b.toc; });
      // "/xmlui/bitstream/handle/123456789/8/S0001098.xml?sequence=7"
  }])
  .controller('MyCtrl2', [function() {

  }]);
  
function fileController($scope, Restangular) {
	$scope.test = "test2";
	$scope.OpenFile = function($event) {
		alert('open file');
		$event.stopPropagation();
		//return false;
	}
};