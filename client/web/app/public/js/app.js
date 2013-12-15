var tuxknifeApp = angular.module('tuxknifeApp', ['ngRoute']);

tuxknifeApp.config(function($routeProvider, $locationProvider) {
  $routeProvider.when('/', 
    {
      templateUrl: 'views/login.html',
      controller: 'LoginCtrl'
    }
  );
  $routeProvider.when('/home', 
    {
      templateUrl: 'views/home.html',
      controller: 'LoginCtrl'
    }
  );
  $locationProvider.html5Mode(true);
});

tuxknifeApp.controller('LoginCtrl', function($scope) {
  $scope.model = {
    message: 'nem sei' 
  }
});
