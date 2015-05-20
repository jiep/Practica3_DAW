angular.module('wikiroutes.services', []).factory("Register",
		function($resource) {

			return $resource('/users/:id', {
				id : '@id'
			}, {
				update : {
					method : "PUT"
				},
				save : {
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					}
				}

			});
		})
.factory("Route", function($resource, $rootScope) {
	return $resource('/users/:id/routes/:id_route', {
		id : '@id', id_route : '@id_route'
	}, {
		update : {
			method : "PUT"
		},
		save : {
			method : "POST",
			headers : {
				'Content-Type' : 'application/json',
				'Authorization' : $rootScope.user.apiKey
			}
		},
		query : {
			method : "GET",
			isArray : false
		}
	})
}).factory("Comment", function($resource, $rootScope) {

	return $resource('/users/:idu/routes/:idr/comments', {
		idu : '@idu',
		idr : '@idr'
	}, {

		save : {
			method : "POST",
			headers : {
				'Content-Type' : 'application/json',
				'Authorization' : $rootScope.user.apiKey
			}
		}

	});
})
.factory("Search", function($resource){
	return $resource('/routes', {}, {
		update : {
			method : "PUT"
		},
		query : {
			method : "GET",
			isArray : false,
			headers : {
				'Content-Type' : 'application/json'
			}
		}
	})
})
.factory("Login", function($resource){
	return $resource('/login', {}, {
		update : {
			method : "PUT"
		},
		query : {
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			}
		}
	});
});