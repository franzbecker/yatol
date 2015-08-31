/**
 * 
 */
var yatolControllers = angular.module('yatolControllers', []);

yatolControllers.controller('RegisterCtrl', [ '$scope', '$http',
    function($scope, $http) {
      var controller = this;

      $scope.loginUser = function() {
        window.alert("Login mit Username: " + $scope.login);
      }

      $scope.registerUser = function() {
        window.alert("Register");
      }

    } ]);

yatolControllers.controller('ListsCtrl', [ '$location', '$scope', '$http',
    function($location, $scope, $http) {
      if ($scope.loggedIn == null) {
        $location.path("/register");
      }
    } ]);
