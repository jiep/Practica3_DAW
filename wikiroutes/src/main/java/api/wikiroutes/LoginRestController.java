package api.wikiroutes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.HashPassword;

@RestController
@RequestMapping("/login")
public class LoginRestController {
	
	@Autowired
	private UserRepository users;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody User user){
				
		User u = users.findByUserName(user.getUserName());
		
		if(u != null){
			if(u.getPass().equals(HashPassword.generateHashPassword(user.getPass()))){
				return new ResponseEntity<>(u, HttpStatus.OK);
			}else{
				return new ResponseEntity<>("El usuario y/o la contraseña no coinciden", HttpStatus.UNAUTHORIZED);
			}
		}
		
		return new ResponseEntity<>("El usuario y/o la contraseña no coinciden", HttpStatus.UNAUTHORIZED);
	}
	
}
