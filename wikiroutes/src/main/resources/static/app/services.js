angular.module('wikiroutes.services', [])
.factory("Register", function($resource){
		
	return $resource('/users/:id', {
		id : '@id'
	}, {
		update : {
			method : "PUT"
		}
	});
});
