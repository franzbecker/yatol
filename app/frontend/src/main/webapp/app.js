/**
 * 
 */
var yatolApp = angular.module('yatolApp', [ 'ngRoute', 'yatolControllers',
    'yatolServices' ]);

yatolApp.config([ '$routeProvider', function($routeProvider) {
  $routeProvider.when('/register', {
    templateUrl : 'register.html',
    controller : 'RegisterCtrl'

  }).when('/lists', {
    templateUrl : 'lists.html',
    controller : 'ListsCtrl'

  }).otherwise({
    redirectTo : '/lists'
  });
} ]);