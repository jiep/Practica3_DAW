package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	UserRepository users;

	@Autowired
	RouteRepository routes;

	@Autowired
	TypeRouteRepository types;

	@RequestMapping
	public List<User> getUsers() {
		
		return users.findAll();
	}
	
	/*@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) {
		user.setRoutes(new ArrayList<Route>());
		user.setComments(new ArrayList<Comment>());
		user.setPass(HashPassword.generateHashPassword(user.getPass()));
		user.setPermission(false);
		user.setFriendships(new ArrayList<Friendship>());
		User u = users.saveAndFlush(user);
		return new ResponseEntity<>(u, HttpStatus.CREATED);
	}*/

	@RequestMapping("/{id}")
	public User getUserById(@PathVariable Long id) {

		User u = new User("pepe", "1234", "email@dsñfsdf.es", true);

		users.save(u);

		return users.findById(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUserById(@PathVariable Long id) {
		
		if(users.findById(id) != null){
			users.delete(id);
		}		
	}

	/*@RequestMapping("/{id}/routes")
	public List<Route> getRoutesById(@PathVariable Long id) {
		

		TypeRoute tr = new TypeRoute("Bici");

		types.save(tr);

		Route r = new Route("Ruta buena", "Una ruta digna de mención", users.findById(id), tr,
				5, true);

		routes.saveAndFlush(r);
		
		users.findOne(id).getRoutes().add(r);

		return users.findOne(id).getRoutes();
	}*/
	
	

}
