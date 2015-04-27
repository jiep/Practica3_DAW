package wikiroutes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class User{
	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userName;
	private String pass;
	private Boolean permision;
	
	public User(String name, String pass, Boolean permision) {
		this.userName = name;
		this.pass = pass;
		this.permision = permision;
	}
	
	public User(User user) {
		this(user.getUserName(),user.getPass(),user.getPermision());
	}

	public int getId() {
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
		this.pass = pass;
	}

	public Boolean getPermision() {
		return permision;
	}

	public void setPermision(Boolean permision) {
		this.permision = permision;
	}
	
	
}



