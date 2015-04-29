package api.wikiroutes;

import java.util.ArrayList;
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

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	UserRepository users;

	@Autowired
	RouteRepository routes;

	@Autowired
	TypeRouteRepository types;

	@Autowired
	StretchRepository stretches;

	@Autowired
	PointRepository points;

	// GET /users
	@RequestMapping
	public List<User> getUsers() {

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
	public ResponseEntity<User> modifyUser(@PathVariable Long id,
			@RequestBody User user) {
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
	public ResponseEntity<Object> insertRouteIntoUser(@RequestBody Route route,
			@PathVariable Long id) {

		User user = users.findById(id);

		Route r = null;

		if (user != null) {

			r = new Route(route.getName(), route.getDescription(), user,
					route.getRate(), route.isPrivate());

			List<Stretch> ls = route.getStretches();

			r.setStretches(ls);

			routes.saveAndFlush(r);

			for (Stretch s : ls) {
				List<Point> pl = s.getPoints();
				for (Point p : pl) {
					p.setStretch(s);
					points.save(p);
				}

				s.setRoute(r);
				stretches.save(s);

			}

			user.getRoutes().add(r);

			users.saveAndFlush(user);

		}

		return new ResponseEntity<>(r, HttpStatus.CREATED);
	}

	// GET /users/{id}/routes/{id}
	@RequestMapping("/{user_id}/routes/{route_id}")
	public Route getRouteByIdAndUserId(@PathVariable Long user_id, @PathVariable Long route_id) {

		List<Route> usersRoutes = routes.findByUserId(user_id);
		
		Route route = null;
		
		for(Route r : usersRoutes){
			if(r.getId() == route_id){
				route = r;
			}
		}
		
		return route;
	}

}
