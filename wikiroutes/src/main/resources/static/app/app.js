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
	}).when("/editRoute/:id", {
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
	}).when("/viewRoute/:id", {
		templateUrl : "/templates/viewRoute.html"
	}).otherwise({
		redirectTo : "/"
	})
});

app.config(function($locationProvider) {
	$locationProvider.html5Mode(true);
});

app.controller('NavCtrl', NavCtrl);
function NavCtrl($http) {
	
	this.querySearch = function(){
		return $http.get('/routes').then(function(result){
			return result.data;
		});
	}
	
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
			private : false,
			stretches : []
		};
		route.stretches.points = [];
		console.log(route);
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
		$scope.route.private = route.isPrivate;
		console.log($scope.route.private);
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

app.controller('MapViewCtrl', function($scope, Comment, $rootScope) {
	

		$scope.newcomment = function(new_comment) {
			newcomment= new Comment(new_comment);
			newcomment.$save().then(function(comment) {
				$scope.user.comments.push(comment);
				$rootScope.user.comments = $scope.user.comments;
				$scope.comment= ""; 
			});
		};
	
	
	
	
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

app.controller("ProfileCtrl", function($rootScope, $scope) {
	console.log($rootScope.user.routes);

	$scope.user = $rootScope.user;
	$scope.routes = $rootScope.user.routes;

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


app.controller("ViewCtrl", function($scope, Comment, $rootScope) {
	console.log("hola");
	$scope.comment = function(new_comment) {
		newcomment = new Comment(new_comment);
		$location.path('/profile');
	};
});
