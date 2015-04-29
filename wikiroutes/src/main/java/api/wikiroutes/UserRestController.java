package api.wikiroutes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.ApiKeyGenerator;
import utils.HashPassword;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	UserRepository users;

	@Autowired
	RouteRepository routes;

	@Autowired
	TypeRouteRepository types;

	
	// GET /users
	@RequestMapping
	public List<User> getUsers() {
		Long id = (long) 1;
		User u = users.findOne(id);
		
		Point p1 = new Point(10, 20, 30);
		Point p2 = new Point(30, 40, 50);
		List<Point> pl = new ArrayList<Point>();
		pl.add(p1);
		pl.add(p2);
		
		Stretch s1 = new Stretch(pl, 1);
		Collections.reverse(pl);
		Stretch s2 = new Stretch(pl, 2);
		
		List<Stretch> sl = new ArrayList<Stretch>();
		sl.add(s1);
		sl.add(s2);

		
		Route r = new Route("Ruta de la muerte", "La ruta de la muerte es temida", u, new TypeRoute(), 6, true);

		ObjectMapper mapper = new ObjectMapper();

		try {
			System.out.println(mapper.writeValueAsString(r));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		
		return users.findAll();
	}

	// POST /users
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) {
		user.setRoutes(new ArrayList<Route>());
		user.setComments(new ArrayList<Comment>());
		user.setPass(HashPassword.generateHashPassword(user.getPass()));
		user.setPermission(false);
		user.setFriendships(new ArrayList<Friendship>());
		user.setApiKey(ApiKeyGenerator.generate());
		User u = users.save(user);
		return new ResponseEntity<>(u, HttpStatus.CREATED);
	}
	
	// PUT /users/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> modifyUser(@PathVariable Long id, @RequestBody User user) {
		User updatedUser = users.findById(id);
		
		updatedUser.setUserName(user.getUserName());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setPass(HashPassword.generateHashPassword(user.getPass()));
		updatedUser.setActivatedNotifications(user.isActivatedNotifications());
		
		User u = users.save(updatedUser);
		return new ResponseEntity<>(u, HttpStatus.CREATED);
	}

	// GET /users/id
	@RequestMapping("/{id}")
	public User getUserById(@PathVariable Long id) {

		return users.findById(id);
	}

	// DELETE /users/id
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUserById(@PathVariable Long id) {

		if (users.findById(id) != null) {
			users.delete(id);
		}
	}

	// GET /users/{id}/routes
	@RequestMapping("/{id}/routes")
	public List<Route> getRoutesById(@PathVariable Long id) {

		return users.findOne(id).getRoutes();

	}
	
	// POST /users/{id}/routes
	@RequestMapping(value = "/{id}/routes", method = RequestMethod.POST)
	public ResponseEntity<Object> insertRouteIntoUser(@RequestBody Route route, @PathVariable Long id){
		
		User user = users.findById(id);
		
		Route r = null;
				
		if(user != null){
			
			r = new Route(route.getName(), route.getDescription(), user, route.getRate(),
			route.isPrivate());
			
			routes.saveAndFlush(r);
			
			user.getRoutes().add(r);
			
			users.saveAndFlush(user);
			
		}
		
		return new ResponseEntity<>(r, HttpStatus.CREATED);
	}

}
