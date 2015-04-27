package api.wikiroutes;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@RequestMapping
	public List<User> getUsers() {
		return null;
	}

}
