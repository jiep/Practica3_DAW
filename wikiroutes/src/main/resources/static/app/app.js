var app = angular.module('WikiroutesApp', [ 'ngMaterial', 'uiGmapgoogle-maps',
		'ngMdIcons', 'ngRoute', 'ngResource', 'slick', 'ngMessages',
		'wikiroutes.services', 'ngDroplet' ]);



app.config(function($mdThemingProvider) {
	$mdThemingProvider.theme('default').primaryPalette('blue-grey')
			.accentPalette('blue');
});

app.config(function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl : "/templates/home.html"
	}).when("/editRoute", {
		templateUrl : "/templates/editRoute.html"
	}).when("/login", {
		templateUrl : "/templates/login.html"
	}).when("/register", {
		templateUrl : "/templates/register.html"
	}).when("/profile", {
		templateUrl : "/templates/profile.html"
	}).when("/search", {
		templateUrl : "/templates/search.html"
	}).when("/developers", {
		templateUrl : "/templates/developers.html"
	}).when("/viewRoute", {
		templateUrl : "/templates/viewRoute.html"
	}).otherwise({
		redirectTo : "/"
	})
});

app.config(function($locationProvider) {
	$locationProvider.html5Mode(true);
});

app.controller('NavCtrl', NavCtrl);
function NavCtrl($timeout, $q, $log) {

	var self = this;
	self.simulateQuery = false;
	self.isDisabled = false;
	// list of `state` value/display objects
	self.states = loadAll();
	self.querySearch = querySearch;
	self.selectedItemChange = selectedItemChange;
	self.searchTextChange = searchTextChange;
	// ******************************
	// Internal methods
	// ******************************
	/**
	 * Search for states... use $timeout to simulate remote dataservice call.
	 */
	function querySearch(query) {
		var results = query ? self.states.filter(createFilterFor(query))
				: self.states, deferred;
		if (self.simulateQuery) {
			deferred = $q.defer();
			$timeout(function() {
				deferred.resolve(results);
			}, Math.random() * 1000, false);
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
		return allStates.split(/, +/g).map(function(state) {
			return {
				value : state.toLowerCase(),
				display : state
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
	}
	;
};

app.controller('MapCtrl', function($scope, $rootScope, Route, $location) {
	
	$scope.route = {};

	$scope.map = {
		center : {
			latitude : 40.000,
			longitude : -3.000
		},
		zoom : 4,
		bounds : {},
		events : {
			click : function(mapModel, eventName, originalEventArgs) {
				$scope.$apply(function() {
					$scope.polyline.path.push({
						latitude : originalEventArgs[0].latLng.A,
						longitude : originalEventArgs[0].latLng.F
					});
				});
			}
		}
	};
	$scope.polyline = {
		id : 1,
		path : [],
		stroke : {
			color : '#6060FB',
			weight : 3
		},
		events : {
			rightclick : function(mapModel, eventName, originalEventArgs,
					arguments) {
				$scope.$apply(function() {
					console.log("1", mapModel);
					console.log("2", eventName);
					console.log("3", originalEventArgs);
					console.log("4", arguments);

				});
			}
		},
		editable : true,
		draggable : true,
		visible : true,
		icons : [ {
			icon : {
				path : google.maps.SymbolPath.FORWARD_OPEN_ARROW
			},
			offset : '25px',
			repeat : '50px'
		} ]
	};

	function setRoute() {
		var route = {
			name : "",
			description : "",
			user : {},
			type : {},
			isPrivate : false,
			stretches : []
		};
		route.stretches.points = [];
		for (var i = 0; i < $scope.polyline.path.length - 1; i++) {
			var p1 = $scope.polyline.path[i];
			var p2 = $scope.polyline.path[i + 1];
			var stretch = {
				points : [ {
					latitude : p1.latitude,
					longitude : p1.longitude,
					altitude : 0
				}, {
					latitude : p2.latitude,
					longitude : p2.longitude,
					altitude : 0
				} ],
				order : i + 1
			};

			route.stretches.push(stretch);
		}

		return route;
	}

	$scope.createRoute = function(route) {
		$scope.route = setRoute();
		$scope.route.name = route.name;
		$scope.route.description = route.description;
		$scope.route.isPrivate = route.isPrivate;
		$scope.route.user = $rootScope.user;
		console.log("identificativo", $scope.route.user);

		var newroute = new Route($scope.route).$save({
			id : $rootScope.user.id
		}).then(function(r) {
			console.log("Usuariosds");
			$scope.routes = $rootScope.user.routes;
			$scope.routes.push(r);
			$rootScope.user.routes = $scope.routes;
			console.log("Usuario");
			console.log("Usuario root", $rootScope.user);

		});

		$location.path("/profile");

	}
});

app.controller('MapViewCtrl', function($scope) {

	$scope.map = {
		center : {
			latitude : 40.1451,
			longitude : -99.6680
		},
		zoom : 4,
		bounds : {},

	};
	$scope.polyline = {
		id : 1,
		path : [],
		stroke : {
			color : '#6060FB',
			weight : 3
		},
		editable : false,
		draggable : false,
		visible : true,
		icons : [ {
			icon : {
				path : google.maps.SymbolPath.FORWARD_OPEN_ARROW
			},
			offset : '25px',
			repeat : '50px'
		} ]
	};
});

app.controller("HomeCtrl", function($scope) {
	$scope.routes = [ {
		"id" : null,
		"name" : "Ruta de la muerte",
		"description" : "La ruta de la muerte es temida",
		"type" : {
			"id" : null,
			"name" : null,
			"routes" : null
		},
		"rate" : 6.0,
		"stretches" : [ {
			"id" : null,
			"points" : [ {
				"latitude" : 30.0,
				"longitude" : 40.0,
				"altitude" : 50.0
			}, {
				"latitude" : 10.0,
				"longitude" : 20.0,
				"altitude" : 30.0
			} ],
			"order" : 1
		}, {
			"id" : null,
			"points" : [ {
				"latitude" : 30.0,
				"longitude" : 40.0,
				"altitude" : 50.0
			}, {
				"latitude" : 10.0,
				"longitude" : 20.0,
				"altitude" : 30.0
			} ],
			"order" : 2
		} ],
		"comments" : null,
		"private" : false
	}, {
		"id" : null,
		"name" : "Ruta de la muerte",
		"description" : "La ruta de la muerte es temida",
		"type" : {
			"id" : null,
			"name" : null,
			"routes" : null
		},
		"rate" : 6.0,
		"stretches" : [ {
			"id" : null,
			"points" : [ {
				"latitude" : 30.0,
				"longitude" : 40.0,
				"altitude" : 50.0
			}, {
				"latitude" : 10.0,
				"longitude" : 20.0,
				"altitude" : 30.0
			} ],
			"order" : 1
		}, {
			"id" : null,
			"points" : [ {
				"latitude" : 30.0,
				"longitude" : 40.0,
				"altitude" : 50.0
			}, {
				"latitude" : 10.0,
				"longitude" : 20.0,
				"altitude" : 30.0
			} ],
			"order" : 2
		} ],
		"comments" : null,
		"private" : false
	}

	];
	$scope.users = [ {
		"id" : null,
		"userName" : "CarlosElGRande",
		"pass" : null,
		"permission" : null,
		"apiKey" : null,
		"email" : "carlosElGrandeHaceMasCommitsQueGermán@syahoo.com",
		"activatedNotifications" : null
	} ]
});

app.controller("LoginCtrl", function($scope, $rootScope) {
	$scope.user = $rootScope.user;
});

app.controller("RegisterCtrl",
		function($scope, Register, $location, $rootScope) {
			$scope.register = function(new_user) {
				newuser = new Register(new_user);
				newuser.$save().then(function(user) {
					$scope.user = user;
					$rootScope.user = $scope.user;
					$location.path('/profile');
				});
			};

		});

app.controller("ProfileCtrl", function($rootScope, $scope) {
	console.log($rootScope.user.routes);
	console.log("hola");

	$scope.user = $rootScope.user;
	$scope.routes = $rootScope.user.routes;
	console.log("Rutas root", $rootScope.user.routes);

});



app.controller('ImgCtrl', function IndexController($scope, $timeout) {

        /**
         * @property interface
         * @type {Object}
         */
        $scope.interface = {};

        /**
         * @property uploadCount
         * @type {Number}
         */
        $scope.uploadCount = 0;

        /**
         * @property success
         * @type {Boolean}
         */
        $scope.success = false;

        /**
         * @property error
         * @type {Boolean}
         */
        $scope.error = false;

        // Listen for when the interface has been configured.
        $scope.$on('$dropletReady', function whenDropletReady() {

            $scope.interface.allowedExtensions(['png', 'jpg', 'bmp', 'gif', 'svg', 'torrent']);
            $scope.interface.setRequestUrl('upload.html');
            $scope.interface.defineHTTPSuccess([/2.{2}/]);
            $scope.interface.useArray(false);

        });

        // Listen for when the files have been successfully uploaded.
        $scope.$on('$dropletSuccess', function onDropletSuccess(event, response, files) {

            $scope.uploadCount = files.length;
            $scope.success     = true;
            console.log(response, files);

            $timeout(function timeout() {
                $scope.success = false;
            }, 5000);

        });

        // Listen for when the files have failed to upload.
        $scope.$on('$dropletError', function onDropletError(event, response) {

            $scope.error = true;
            console.log(response);

            $timeout(function timeout() {
                $scope.error = false;
            }, 5000);

        });

    }).directive('progressbar', function ProgressbarDirective() {

        return {

            /**
             * @property restrict
             * @type {String}
             */
            restrict: 'A',

            /**
             * @property scope
             * @type {Object}
             */
            scope: {
                model: '=ngModel'
            },

            /**
             * @property ngModel
             * @type {String}
             */
            require: 'ngModel',

            /**
             * @method link
             * @param scope {Object}
             * @param element {Object}
             * @return {void}
             */
            link: function link(scope, element) {

                var progressBar = new ProgressBar.Path(element[0], {
                    strokeWidth: 2
                });

                scope.$watch('model', function() {

                    progressBar.animate(scope.model / 100, {
                        duration: 1000
                    });

                });

                scope.$on('$dropletSuccess', function onSuccess() {
                    progressBar.animate(0);
                });

                scope.$on('$dropletError', function onSuccess() {
                    progressBar.animate(0);
                });

            }

        }

})



