'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('dspace.services', ['restangular']).
    value('version', '0.1').
    factory('bookService', ['$route', '$location', 'Restangular', function ($route, $location, Restangular) {
        var bitstream = Restangular.all('bitstream/handle/123456789'); // /38141/S0001300.xml').get()
        var _toc = {};
    
        var _handleId = 0;
        var _bitstreamName = '';
        var _tocName = '';
          

        var _getToc = function () {
            if (_handleId != $route.current.params.handle || _bitstreamName != $route.current.params.bsname) {

                _handleId = $route.current.params.handle;
                _bitstreamName = $route.current.params.bsname;
                _tocName = _bitstreamName.replace('.kar', '.xml');

                _toc.book = _getBitstream(_handleId, _tocName)
                    .then(function (res) {
                        _toc.book = x2js.xml_str2json(res).book;
                    });                
            }
            return _toc;
        }


        var _getBitstream = function (handle, name) {
            return bitstream.one(handle).one(name).get();
        };
        
        var _getPagePath = function (page) {
            return '/' + $route.current.params.handle + '/' + $route.current.params.bsname + '/' + page;
        };

        
        var _getPageImageUrl = function (pageNum) {
            var baseUrl = $location.absUrl().substring(0, $location.absUrl().indexOf('/themes/'));
            return baseUrl + '/bitstream/handle/123456789/' + $route.current.params.handle + '/' + $route.current.params.bsname + '?page=' + pageNum;
        };

        return {
            getToc: _getToc,                        
            getPagePath: _getPagePath,
            getPageImageUrl: _getPageImageUrl
        };
  }]);
