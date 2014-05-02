'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('dspace.services', ['restangular']).
  value('version', '0.1').
  factory('bookService', function (Restangular) {

  });
