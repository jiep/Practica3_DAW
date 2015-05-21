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
	}).when("/newRoute", {
		templateUrl : "/templates/newRoute.html"
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
	}).when("/route/:id", {
		templateUrl : "/templates/viewPublicRoute.html"
	}).when("/category/:type", {
		templateUrl : "/templates/search.html"
	}).when("/preferences", {
		templateUrl : "/templates/preferences.html"
	}).otherwise({
		redirectTo : "/"
	})
});

app.config(function($locationProvider) {
	$locationProvider.html5Mode(true);
});

app.controller('NavCtrl', NavCtrl);
function NavCtrl($http, $rootScope, $scope, $location) {
	
	$scope.$apply(function(){
		$scope.user = $rootScope.user;
	});

	$scope.logout = function(){
		$rootScope.user = undefined;
		$scope.user = undefined;
		$location.path("/login");
	};

	this.querySearch = function() {
		return $http.get('/routes').then(function(result) {
			return result.data;
		});
	};

	$scope.findByName = function(object){
		$location.path("/route/" + object.id);
	};

};

app.controller('MapCtrl', function($scope, $rootScope, Route, $location,
		$routeParams) {

	$scope.route = {};

	if ($routeParams.id) { // Editar ruta
		
		function setPath(route) {
			route.path = [];
			for (var i = 0; i < route.stretches.length; i++) {
				if (i != route.stretches.length - 1) {
					route.path.push({
						latitude : route.stretches[i].points[0].latitude,
						longitude : route.stretches[i].points[0].longitude
					});
				} else {
					route.path.push({
						latitude : route.stretches[i].points[0].latitude,
						longitude : route.stretches[i].points[0].longitude
					});
					route.path.push({
						latitude : route.stretches[i].points[1].latitude,
						longitude : route.stretches[i].points[1].longitude
					});
				}
			}

			return route.path;
		}

		var r = new Route({
			id : $rootScope.user.id,
			id_route : $routeParams.id
		});
		r.$query().then(function(return_route) {
			$scope.route = return_route;
			$scope.route.path = setPath(return_route);
			console.log("Path route", $scope.route.path);
		});

		console.log("Ruta", $scope.route);
	}

	$scope.map = {
		center : {
			latitude : 40,
			longitude : -3.00
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
	
	console.log("Polilínea", $scope.polyline)

	$scope.createRoute = function(route) {
		$scope.route = setRoute();
		$scope.route.name = route.name;
		$scope.route.description = route.description;
		$scope.route.private = route.isPrivate;
		$scope.route.type = route.type;
		console.log($scope.route.type);
		console.log($scope.route.private);
		$scope.route.user = $rootScope.user;

		var newroute = new Route($scope.route).$save({
			id : $rootScope.user.id
		}).then(function(r) {
			$scope.routes = $rootScope.user.routes;
			$scope.routes.push(r);
			$rootScope.user.routes = $scope.routes;

		});

		$location.path("/profile");

	}
});

app.controller('MapViewCtrl', function($scope, Comment, $rootScope, Route,
		$routeParams) {

	function setPath(route) {
		route.path = [];
		for (var i = 0; i < route.stretches.length; i++) {
			if (i != route.stretches.length - 1) {
				route.path.push({
					latitude : route.stretches[i].points[0].latitude,
					longitude : route.stretches[i].points[0].longitude
				});
			} else {
				route.path.push({
					latitude : route.stretches[i].points[0].latitude,
					longitude : route.stretches[i].points[0].longitude
				});
				route.path.push({
					latitude : route.stretches[i].points[1].latitude,
					longitude : route.stretches[i].points[1].longitude
				});
			}
			console.log(route.path);
		}

		return route.path;
	}

	var route = {};

	var r = new Route({
		id : $rootScope.user.id,
		id_route : $routeParams.id
	});
	r.$query().then(function(return_route) {
		$scope.route = return_route;
		route = setPath(return_route);
		console.log(return_route);

		$scope.map = {
			center : {
				latitude : route[0].latitude,
				longitude : route[0].longitude
			},
			zoom : 4,
			bounds : {},

		};
		$scope.polyline = {
			id : 1,
			path : route,
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

	$scope.newcomment = function(new_comment) {
		newcomment = new Comment(new_comment);
		newcomment.$save().then(function(comment) {
			$scope.user.comments.push(comment);
			$rootScope.user.comments = $scope.user.comments;
			$scope.comment = "";
		});
	};

});

app.controller("HomeCtrl", function($scope, $http, $rootScope) {
	$http.get('/routes').then(function(result) {
			$scope.routes =  result.data;
			$scope.user = $rootScope.user;
	});
	
	console.log($scope.routes);
});

app.controller("LoginCtrl", function($scope, $rootScope, Login, $location) {
	$scope.login = function(user) {
		var login = new Login(user);
		login.$save().then(function(user_logged) {
			console.log(user_logged);
			if (user_logged.apiKey) {
				$scope.user = user_logged;
				$rootScope.user = user_logged;
				$location.path("/profile");
			} else {
				$scope.message = user_logged;
			}
		});
	}
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
	$scope.comment = function(new_comment) {
		newcomment = new Comment(new_comment);
		$location.path('/profile');
	};
})

app.controller('EditMapCtrl', function($scope, $rootScope, Route, $routeParams, $location) {

	$scope.route = {};

	if ($routeParams.id) { // Editar ruta
		
		function setPath(route) {
			route.path = [];
			for (var i = 0; i < route.stretches.length; i++) {
				if (i != route.stretches.length - 1) {
					route.path.push({
						latitude : route.stretches[i].points[0].latitude,
						longitude : route.stretches[i].points[0].longitude
					});
				} else {
					route.path.push({
						latitude : route.stretches[i].points[0].latitude,
						longitude : route.stretches[i].points[0].longitude
					});
					route.path.push({
						latitude : route.stretches[i].points[1].latitude,
						longitude : route.stretches[i].points[1].longitude
					});
				}
			}

			return route.path;
		}

		var r = new Route({
			id : $rootScope.user.id,
			id_route : $routeParams.id
		});
		r.$query().then(function(return_route) {
			$scope.route = return_route;
			$scope.route.path = setPath(return_route);
			console.log("Path route", $scope.route.path);
			
			$scope.map = {
					center : {
						latitude : $scope.route.path[0].latitude,
						longitude : $scope.route.path[0].longitude
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
					path : $scope.route.path,
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
				
				console.log("Polilínea", $scope.polyline);
			
				$scope.editRoute = function(route) {
					$scope.route = setRoute();
					$scope.route.name = route.name;
					$scope.route.description = route.description;
					$scope.route.private = route.isPrivate;
					console.log($scope.route.private);
					$scope.route.user = $rootScope.user;
			
					var newroute = new Route($scope.route).$update({
						id : $rootScope.user.id,
						id_route : $routeParams.id
					}).then(function(r) {
						$scope.routes = $rootScope.user.routes;
						$scope.routes.push(r);
						$rootScope.user.routes = $scope.routes;
					});
			
					$location.path("/profile");
			
				}
		});
	

		
	}
});

app.controller("SearchByCategoryCtrl", function($scope, $http, $routeParams){
	if($routeParams.type){
		$http.get('/search/category/' + $routeParams.type).then(function(result) {
			$scope.routes =  result.data;
		});
	}
});

app.controller("ViewPublicRouteCtrl", function($scope, $http, $routeParams){
	if($routeParams.id){
		$http.get('/search/' + $routeParams.id).then(function(result) {
			$scope.route =  result.data;
			function setPath(route) {
				route.path = [];
				for (var i = 0; i < route.stretches.length; i++) {
					if (i != route.stretches.length - 1) {
						route.path.push({
							latitude : route.stretches[i].points[0].latitude,
							longitude : route.stretches[i].points[0].longitude
						});
					} else {
						route.path.push({
							latitude : route.stretches[i].points[0].latitude,
							longitude : route.stretches[i].points[0].longitude
						});
						route.path.push({
							latitude : route.stretches[i].points[1].latitude,
							longitude : route.stretches[i].points[1].longitude
						});
					}
					console.log(route.path);
				}

				return route.path;
			}

			var route = {};


				route = setPath($scope.route);

				$scope.map = {
					center : {
						latitude : route[0].latitude,
						longitude : route[0].longitude
					},
					zoom : 4,
					bounds : {},

				};
				$scope.polyline = {
					id : 1,
					path : route,
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
	}
});

app.controller("PreferencesCtrl", function($rootScope, $location, UploadUser, $scope){
	$scope.changeProfile = function(user){
		var upload_user = new UploadUser(user);
		console.log(user);
		upload_user.$update({id: $rootScope.user.id}).then(function(uploaded_user){
			$rootScope.user = uploaded_user;
			$location.path("/profile");
		});
	}
});
