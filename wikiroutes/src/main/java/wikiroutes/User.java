package wikiroutes;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String userName;
	private String pass;
	private boolean permision;
	private String apiKey;
	
	public User(){
		
	}

	public User(String name, String pass, boolean permision) {
		this.userName = name;
		this.pass = HashPassword.generateHashPassword(pass);
		this.permision = permision;
		this.apiKey = generateApiKey();
	}
	
	public User(User user) {
		this(user.getUserName(),user.getPass(),user.getPermision());
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = HashPassword.generateHashPassword(pass);
	}

	public Boolean getPermision() {
		return permision;
	}

	public void setPermision(Boolean permision) {
		this.permision = permision;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setPermision(boolean permision) {
		this.permision = permision;
	}
	
	private String generateApiKey(){
		return UUID.randomUUID().toString();
	}
	
	
}



