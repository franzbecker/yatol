/**
 * Webservices for yatol
 */
var yatolServices = angular.module('yatolServices', [ 'ngResource' ]);

yatolServices.factory('User', [ '$resource', function($resource) {
  return $resource('/backend/users/:action', {}, {
    register : {
      method : 'POST',
      params : {
        action : 'register'
      }
    },
    login : {
      method : 'POST',
      params : {
        action : 'login'
      }
    }
  });
} ]);
