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


@RestController
@RequestMapping("/routes")
public class RouteRestController {

	
	@Autowired
	RouteRepository routes;
	
	@Autowired
	UserRepository users;
	
	@Autowired
	CommentRepository comments;
	
	@RequestMapping
	public List<Route> getRoutes(){
		return routes.findByIsPrivate(false);
	}
	
	@RequestMapping(value="/{id}")
	public Route getRouteById(@PathVariable Long id){
		Route r = routes.findOne(id);
		Route return_route = null;
		if(!r.isPrivate()){
			return_route = r;
		}
		
		return return_route;
	}
	
	@RequestMapping(value="/{id}/comments", method = RequestMethod.POST)
	public ResponseEntity<Comment> newComment(@PathVariable Long id,
			@RequestHeader("Authorization") String authorization, @RequestBody Comment comment){
		
		User u = users.findByApiKey(authorization);
		
		Route r = routes.getOne(id);
		
		Comment c = null;
		
		if(!r.isPrivate() && u != null){
			r.getComments().add(comment);
			u.getComments().add(comment);
		
			comment.setUser(u);
			
			routes.saveAndFlush(r);
			users.saveAndFlush(u);
			
			c = comments.save(comment);
		}
		
		return new ResponseEntity<>(c, HttpStatus.CREATED);
		
	}
	
	@RequestMapping("/{id}/comments")
	public List<Comment> getComments(@PathVariable Long id){
		
		
		Route r = routes.getOne(id);
		
		List<Comment> comment_list = new ArrayList<Comment>();
		
		if(!r.isPrivate()){
		
			
			comment_list = r.getComments();
		}
		
		return comment_list;
		
	}
}
