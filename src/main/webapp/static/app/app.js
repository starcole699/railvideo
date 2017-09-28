(function(angular) {
    angular.module("alarmsApp.controllers", []);
    angular.module("alarmsApp.services", []);
    angular.module("alarmsApp", ["ngResource", "spring-data-rest", "alarmsApp.controllers", "alarmsApp.services"]);
}(angular));