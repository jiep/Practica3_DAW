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
		}).factory("Route", function($resource, $rootScope) {
	return $resource('/users/:id/routes', {
		id : '@id'
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
		}
	});
});