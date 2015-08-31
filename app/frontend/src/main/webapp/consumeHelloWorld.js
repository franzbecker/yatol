/**
 * Tries to get hello world restservice.
 */

angular.module('consumeHelloWorld', []).controller('HelloWorldController',
    function($scope, $http) {
      var controller = this;

      controller.getHelloWorldJSON = function() {
        $http.get('/backend/json').then(function(data) {
          $scope.bla = data.data;
        }, function(response) {
          alert(response);
        });
      }

      controller.getHelloWorldJSON();
    });