package api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wikiroutes.User;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@RequestMapping("/")
	public List<User> getUsers(){
		List<User> users = new ArrayList<User>();
		
		User user1 = new User();
		User user2 = new User();
		
		users.add(user1);
		users.add(user2);
		
		return users;
	}
	
}
