var app = angular.module('WikiroutesApp', ['ngMaterial', 'uiGmapgoogle-maps', 'ngMdIcons', 'ngRoute', 'slick', 'ngMessages']);

app.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey')
    .accentPalette('blue');
});

app.config(function($routeProvider){
  $routeProvider
  .when("/", {
      templateUrl: "/templates/home.html"
  })
  .when("/editRoute", {
      templateUrl: "/templates/editRoute.html"
  })
  .when("/login", {
      templateUrl: "/templates/login.html"
  })
  .when("/register", {
      templateUrl: "/templates/register.html"
  })
  .when("/profile", {
      templateUrl: "/templates/profile.html"
  })
  .otherwise({
      redirectTo: "/"
  })
});


 app.controller('NavCtrl', NavCtrl);
  function NavCtrl ($timeout, $q, $log) {
	  
    var self = this;
    self.simulateQuery = false;
    self.isDisabled    = false;
    // list of `state` value/display objects
    self.states        = loadAll();
    self.querySearch   = querySearch;
    self.selectedItemChange = selectedItemChange;
    self.searchTextChange   = searchTextChange;
    // ******************************
    // Internal methods
    // ******************************
    /**
     * Search for states... use $timeout to simulate
     * remote dataservice call.
     */
    function querySearch (query) {
      var results = query ? self.states.filter( createFilterFor(query) ) : self.states,
          deferred;
      if (self.simulateQuery) {
        deferred = $q.defer();
        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
        return deferred.promise;
      } else {
        return results;
      }
    }
    function searchTextChange(text) {
      $log.info('Text changed to ' + text);
    }
    function selectedItemChange(item) {
      $log.info('Item changed to ' + JSON.stringify(item));
    }
    /**
     * Build `states` list of key/value pairs
     */
    function loadAll() {
      var allStates = 'Alabama, Alaska, Arizona, Arkansas, California, Colorado, Connecticut, Delaware,\
              Florida, Georgia, Hawaii, Idaho, Illinois, Indiana, Iowa, Kansas, Kentucky, Louisiana,\
              Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, Montana,\
              Nebraska, Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina,\
              North Dakota, Ohio, Oklahoma, Oregon, Pennsylvania, Rhode Island, South Carolina,\
              South Dakota, Tennessee, Texas, Utah, Vermont, Virginia, Washington, West Virginia,\
              Wisconsin, Wyoming';
      return allStates.split(/, +/g).map( function (state) {
        return {
          value: state.toLowerCase(),
          display: state
        };
      });
    }
    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);
      return function filterFn(state) {
        return (state.value.indexOf(lowercaseQuery) === 0);
      };
    };
  };

app.controller('MapCtrl', function($scope){

        $scope.map = {
          center: {
            latitude: 40.1451, longitude: -99.6680
          },
          zoom: 4,
          bounds: {},
          events: {
            click: function(mapModel, eventName, originalEventArgs){
              $scope.$apply(function(){
                $scope.polyline.path.push({latitude: originalEventArgs[0].latLng.A, longitude : originalEventArgs[0].latLng.F});
              });
            }
          }
        };
        $scope.polyline = {
                id: 1,
                path: [],
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                events: {
                  rightclick: function(mapModel, eventName, originalEventArgs, arguments){
                    $scope.$apply(function(){
                      console.log("1", mapModel);
                      console.log("2", eventName);
                      console.log("3", originalEventArgs);
                      console.log("4", arguments);

                    });
                  }
                },
                editable: true,
                draggable: true,
                visible: true,
                icons: [{
                    icon: {
                        path: google.maps.SymbolPath.FORWARD_OPEN_ARROW
                    },
                    offset: '25px',
                    repeat: '50px'
                }]
            };
});

app.controller("HomeCtrl", function($scope){
	$scope.routes = [
{"id":null,"name":"Ruta de la muerte","description":"La ruta de la muerte es temida","type":{"id":null,"name":null,"routes":null},"rate":6.0,"stretches":[{"id":null,"points":[{"latiude":30.0,"longitude":40.0,"altitude":50.0},{"latiude":10.0,"longitude":20.0,"altitude":30.0}],"order":1},{"id":null,"points":[{"latiude":30.0,"longitude":40.0,"altitude":50.0},{"latiude":10.0,"longitude":20.0,"altitude":30.0}],"order":2}],"comments":null,"private":false},
{"id":null,"name":"Ruta de la muerte","description":"La ruta de la muerte es temida","type":{"id":null,"name":null,"routes":null},"rate":6.0,"stretches":[{"id":null,"points":[{"latiude":30.0,"longitude":40.0,"altitude":50.0},{"latiude":10.0,"longitude":20.0,"altitude":30.0}],"order":1},{"id":null,"points":[{"latiude":30.0,"longitude":40.0,"altitude":50.0},{"latiude":10.0,"longitude":20.0,"altitude":30.0}],"order":2}],"comments":null,"private":false}
	                 
	];
});

app.controller("LoginCtrl", function($scope){
	
});

