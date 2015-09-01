/**
 * 
 */
var yatolControllers = angular.module('yatolControllers', []);

yatolControllers.factory('Data', function() {
  var data = {};
  return data;
});

yatolControllers.controller('RegisterCtrl', [ '$location', '$scope', '$http',
    'User', 'Data', function($location, $scope, $http, User, Data) {
      var controller = this;
      $scope.registerError = false;
      $scope.loginError = false;

      if (Data.loginToken != null) {
        $location.path("/lists");
      }
      
      controller._doLogin = function(username) {
        User.login(username, function(response, getResponseHeaders) {
          $scope.loginError = !response.success;
          if (response.success) {
            Data.loginToken = response.token;
            $location.path("/lists");
          }
        });
      }

      $scope.loginUser = function() {
        controller._doLogin($scope.login)
      }

      $scope.registerUser = function() {

        User.register($scope.register, function(response, getResponseHeaders) {
          $scope.registerError = !response.success;
          if (response.success) {
            controller._doLogin(response.user.username);
          }
        });
      }

    } ]);

yatolControllers.controller('ListsCtrl', [ '$location', '$scope', '$http',
    'Data', function($location, $scope, $http, Data) {
      if (Data.loginToken == null) {
        $location.path("/register");
      }
    } ]);
