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

	@Autowired
	CommentRepository comments;

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
	public Route getRouteByIdAndUserId(@PathVariable Long user_id,
			@PathVariable Long route_id) {

		List<Route> usersRoutes = routes.findByUserId(user_id);

		Route route = null;

		for (Route r : usersRoutes) {
			if (r.getId() == route_id) {
				route = r;
			}
		}

		return route;
	}

	// PUT /users/{id}/routes/{id}
	@RequestMapping(value = "/{user_id}/routes/{route_id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> modifyRouteByIdAndUserId(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@RequestBody Route route) {

		List<Route> usersRoutes = routes.findByUserId(user_id);

		Route r1 = null;

		for (Route r : usersRoutes) {
			if (r.getId() == route_id) {
				r1 = r;
			}
		}

		Route r = null;

		if (route != null) {

			User user = users.findById(user_id);

			r1 = new Route(route.getName(), route.getDescription(), user,
					route.getRate(), route.isPrivate());

			List<Stretch> ls = route.getStretches();

			r1.setStretches(ls);

			routes.saveAndFlush(r);

			for (Stretch s : ls) {
				List<Point> pl = s.getPoints();
				for (Point p : pl) {
					p.setStretch(s);
					points.save(p);
				}

				s.setRoute(r1);
				stretches.save(s);

			}

			user.getRoutes().add(r1);

			users.saveAndFlush(user);

		}

		return new ResponseEntity<>(r, HttpStatus.CREATED);
	}

	// DELETE /users/{id}/routes/{id}
	@RequestMapping(value = "/{user_id}/routes/{route_id}", method = RequestMethod.DELETE)
	public void deleteRouteByIdAndUserId(@PathVariable Long user_id,
			@PathVariable Long route_id) {

		List<Route> usersRoutes = routes.findByUserId(user_id);

		Route route = null;

		for (Route r : usersRoutes) {
			if (r.getId() == route_id) {
				route = r;
			}
		}

		if (route != null) {
			routes.delete(route);
		}

	}

	// GET /users/{id}/comments
	@RequestMapping(value = "/{id}/comments")
	public List<Comment> getCommentsByUserId(@PathVariable Long id) {

		return comments.findByUserId(id);

	}

	// GET /users/{id}/routes/{id}/comments
	@RequestMapping(value = "/{user_id}/routes/{route_id}/comments")
	public List<Comment> getCommentsByUserIdAndRouteId(
			@PathVariable Long user_id, @PathVariable Long route_id) {

		List<Route> usersRoutes = routes.findByUserId(user_id);

		Route route = null;

		for (Route r : usersRoutes) {
			if (r.getId() == route_id) {
				route = r;
			}
		}

		return route.getComments();

	}

	// POST /users/{id}/routes/{id}/comments
	@RequestMapping(value = "/{user_id}/routes/{route_id}/comments", method = RequestMethod.POST)
	public ResponseEntity<Comment> addNewCommentToRoute(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@RequestBody Comment comment) {
		
		List<Route> usersRoutes = routes.findByUserId(user_id);

		Route route = null;

		for (Route r : usersRoutes) {
			if (r.getId() == route_id) {
				route = r;
			}
		}

		if (route != null) {
			User u = users.findById(user_id);

			List<Comment> commentList = route.getComments();
			comment.setRoute(route);
			comment.setUser(u);
			commentList.add(comment);
			comments.save(commentList);
			

			List<Comment> userComments = u.getComments();
			userComments.add(comment);
			users.saveAndFlush(u);

			route.getComments().add(comment);
			routes.saveAndFlush(route);

			

		}

		return new ResponseEntity<Comment>(comment, HttpStatus.CREATED);
	}
	
	
	
	
}
