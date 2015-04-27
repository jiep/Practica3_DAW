package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {
	
	@Autowired
	UserRepository users;

	@RequestMapping
	public List<User> getUsers() {
		
		return users.findAll();
	}

}
