package api.wikiroutes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

	@Autowired
	FriendshipRepository friendships;

	// GET /users
	@RequestMapping()
	public List<User> getUsers() {

		return users.findAll();
	}

	// POST /users
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) {
		user.setRoutes(new ArrayList<Route>());
		user.setComments(new ArrayList<Comment>());
		user.setPass(user.getPass());
		user.setPermission(false);
		// user.setFriendships(new ArrayList<Friendship>());
		user.setApiKey(ApiKeyGenerator.generate());
		User u = users.save(user);
		return new ResponseEntity<>(u, HttpStatus.CREATED);
	}

	// PUT /users/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = { "Authorization" })
	public ResponseEntity<User> modifyUser(@PathVariable Long id,
			@RequestBody User user,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		User u = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == id
				&& !authorization.isEmpty()) {

			User updatedUser = users.findById(id);

			updatedUser.setUserName(user.getUserName());
			updatedUser.setEmail(user.getEmail());
			updatedUser.setPass(HashPassword.generateHashPassword(user
					.getPass()));
			updatedUser.setActivatedNotifications(user
					.isActivatedNotifications());

			u = users.save(updatedUser);

			status = HttpStatus.CREATED;

		}
		return new ResponseEntity<>(u, status);
	}

	// GET /users/id
	@RequestMapping("/{id}")
	public User getUserById(@PathVariable Long id) {

		return users.findById(id);
	}

	// DELETE /users/id
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = { "Authorization" })
	public ResponseEntity<User> deleteUserById(@PathVariable Long id,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		User u = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == id
				&& !authorization.isEmpty()) {

			if (users.findById(id) != null) {
				users.delete(id);
				status = HttpStatus.CREATED;
			}

		}

		return new ResponseEntity<>(u, status);
	}

	// GET /users/{id}/routes
	@RequestMapping("/{id}/routes")
	public List<Route> getRoutesById(@PathVariable Long id,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		List<Route> routes_list = null;

		if (auth_user != null && auth_user.getId() == id
				&& !authorization.isEmpty()) {

			routes_list = users.findOne(id).getRoutes();
		} else {

			routes_list = routes.findByUserIdAndIsPrivate(id, false);
		}

		return routes_list;

	}

	// POST /users/{id}/routes
	@RequestMapping(value = "/{id}/routes", method = RequestMethod.POST)
	public ResponseEntity<Route> insertRouteIntoUser(@RequestBody Route route,
			@PathVariable Long id,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		Route r = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == id
				&& !authorization.isEmpty()) {

			User user = users.findById(id);

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

				status = HttpStatus.CREATED;

			}
		}

		return new ResponseEntity<>(r, status);
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
	public ResponseEntity<Route> modifyRouteByIdAndUserId(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@RequestBody Route route,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		Route r = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == user_id
				&& !authorization.isEmpty()) {

			List<Route> usersRoutes = routes.findByUserId(user_id);

			Route r1 = null;

			for (Route r2 : usersRoutes) {
				if (r2.getId() == route_id) {
					r1 = r2;
				}
			}

			r = null;

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

				status = HttpStatus.CREATED;

			}
		}

		return new ResponseEntity<>(r, status);
	}

	// DELETE /users/{id}/routes/{id}
	@RequestMapping(value = "/{user_id}/routes/{route_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Route> deleteRouteByIdAndUserId(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		Route r = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == user_id
				&& !authorization.isEmpty()) {

			List<Route> usersRoutes = routes.findByUserId(user_id);

			Route route = null;

			for (Route r1 : usersRoutes) {
				if (r1.getId() == route_id) {
					route = r1;
				}
			}

			if (route != null) {
				routes.delete(route);
			}

			status = HttpStatus.CREATED;
		}

		return new ResponseEntity<>(r, status);

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
			@RequestBody Comment comment,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == user_id
				&& !authorization.isEmpty()) {

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

			status = HttpStatus.CREATED;

		}

		return new ResponseEntity<Comment>(comment, status);
	}

	// PUT /users/{id}/routes/{id}/comments/{id}
	@RequestMapping(value = "/{user_id}/routes/{route_id}/comments/{comment_id}", method = RequestMethod.PUT)
	public ResponseEntity<Comment> modifyCommentInRoute(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@PathVariable Long comment_id, @RequestBody Comment comment,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == user_id
				&& !authorization.isEmpty()) {

			List<Route> usersRoutes = routes.findByUserId(user_id);

			Route route = null;

			Comment cmmnt = null;

			for (Route r : usersRoutes) {
				if (r.getId() == route_id) {
					route = r;
					for (Comment c : r.getComments()) {
						if (c.getId() == comment_id) {
							cmmnt = c;
						}
					}
				}
			}

			if (cmmnt != null) {
				User u = users.findById(user_id);

				List<Comment> commentList = route.getComments();
				cmmnt.setRoute(route);
				cmmnt.setUser(u);
				cmmnt.setDate(comment.getDate());
				cmmnt.setComment(comment.getComment());
				commentList.add(comment);
				comments.save(commentList);

				List<Comment> userComments = u.getComments();
				userComments.add(comment);
				users.saveAndFlush(u);

				route.getComments().add(comment);
				routes.saveAndFlush(route);
			}

			status = HttpStatus.CREATED;

		}

		return new ResponseEntity<Comment>(comment, status);
	}

	// DELETE /users/{id}/routes/{id}/comments/{id}
	@RequestMapping(value = "/{user_id}/routes/{route_id}/comments/{comment_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Comment> deleteCommentInRoute(
			@PathVariable Long user_id, @PathVariable Long route_id,
			@PathVariable Long comment_id,
			@RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		Comment cm = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == user_id
				&& !authorization.isEmpty()) {

			List<Route> usersRoutes = routes.findByUserId(user_id);

			Route route = null;

			Comment cmmnt = null;

			for (Route r : usersRoutes) {
				if (r.getId() == route_id) {
					route = r;
					for (Comment c : route.getComments()) {
						if (c.getId() == comment_id) {
							cmmnt = c;
						}
					}
				}
			}

			if (cmmnt != null) {
				User u = users.findByCommentsId(comment_id);
				List<Comment> usersComments = u.getComments();

				if (usersComments != null) {

					for (Comment c : usersComments) {
						if (c.getId() == comment_id) {
							usersComments.remove(c);
						}
					}

					u.setComments(usersComments);

					users.save(u);

					Route rt = routes.findByCommentsId(comment_id);
					List<Comment> routeComments = rt.getComments();

					for (Comment rout : routeComments) {
						if (rout.getId() == comment_id) {
							routeComments.remove(rout);
						}
					}

					rt.setComments(routeComments);

					routes.save(rt);

					comments.delete(cmmnt);

				}
			}

			status = HttpStatus.CREATED;

		}

		return new ResponseEntity<>(cm, status);

	}

	// POST /users/{id}/friends
	@RequestMapping(value = "/{id}/friends", method = RequestMethod.POST)
	public ResponseEntity<User> addFriend(@PathVariable Long id,
			User newFriend, @RequestHeader("Authorization") String authorization) {

		User auth_user = users.findByApiKey(authorization);

		User user = null;

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (auth_user != null && auth_user.getId() == newFriend.getId()
				&& !authorization.isEmpty()) {

			user = users.findById(id);

			Friendship fs = new Friendship(newFriend, user);

			friendships.save(fs);

			user.getFriendships().add(fs);

			users.saveAndFlush(user);
			
			status = HttpStatus.CREATED;

		}

		return new ResponseEntity<User>(user, status);
	}

	// GET /users/{id}/friends
	@RequestMapping("/{id}/friends")
	public List<User> getFriendsByUserId(@PathVariable Long id) {
		List<Friendship> friendships = users.findById(id).getFriendships();

		List<User> friends = new ArrayList<>();

		for (Friendship f : friendships) {
			friends.add(users.findById(f.getTargetUser().getId()));
		}

		return friends;

	}

	// GET /users/{id}/friends/routes
	@RequestMapping("/{id}/friends/routes")
	public List<Route> getFriendsRoutesByUserId(@PathVariable Long id) {
		List<Friendship> friendships = users.findById(id).getFriendships();

		List<Route> routes = new ArrayList<>();

		for (Friendship f : friendships) {
			routes.addAll(users.findById(f.getTargetUser().getId()).getRoutes());
		}

		return routes;

	}

}