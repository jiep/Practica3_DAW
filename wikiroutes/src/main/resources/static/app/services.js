angular.module('wikiroutes.services', [])
.factory("User", function($resource){
	
	
	var UserResource = $resource('/users/:id', {
		id : '@id'
	}, {
		update : {
			method : "PUT"
		}
	});

	return {
		add: function(new_user){
			console.log(UserResource(new_user));
			new UserResource(new_user).$save().then(function(user){
				console.log(user);
			});
		}
	}
});