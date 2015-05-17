angular.module('wikiroutes.services', [])
.factory("User", function($resource){
	
	var user_info = {};
	
	var UserResource = $resource('/users/:id', {
		id : '@id'
	}, {
		update : {
			method : "PUT"
		}
	});

	return {
		register: function(new_user){
			new UserResource(new_user).$save(function(user){
				console.log(user_info);
				user_info.apply = user;
			});			
		},
		logout: function(){
			user_info = {};
		},
		upload: function(uploaded_user){
			new UserResource({id: uploaded_user.id}).$update(function(user){
				user_info = user;
			});
		},
		delete_user: function(user){
			new UserResource({id: user.id}).$delete(function(deleted_user){
				user_info = {};
			});
		},
		getInfo: function(){
			return user_info;
		}
	}
});