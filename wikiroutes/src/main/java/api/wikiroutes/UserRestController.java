package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping("/{id}")
	public User getUserById(@PathVariable Long id){
		
		return users.findById(id);
	}

}
