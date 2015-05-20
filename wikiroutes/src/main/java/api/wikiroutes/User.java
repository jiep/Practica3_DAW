package api.wikiroutes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import utils.ApiKeyGenerator;
import utils.HashPassword;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
    @Column(unique=true)
	private String userName;
	private String pass;
	private boolean permission;
	private String apiKey;
	private String email;
	private boolean activatedNotifications;
	
	@OneToMany(cascade= CascadeType.ALL)
	private List<Route> routes = new ArrayList<Route>();
	
    @JsonManagedReference
	@OneToMany(mappedBy = "sourceUser")
    private List<Friendship> friendships = new ArrayList<Friendship>();
	

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<Comment>();

	public User() {

	}

	public User(String name, String pass, String email, boolean permission) {
		this.userName = name;
		this.pass = HashPassword.generateHashPassword(pass);
		this.permission = permission;
		this.apiKey = ApiKeyGenerator.generate();
		this.email = email;
	}

	public long getId() {
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

	public Boolean getPermission() {
		return permission;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setPermission(boolean permission) {
		this.permission = permission;
	}

	public boolean isActivatedNotifications() {
		return activatedNotifications;
	}

	public void setActivatedNotifications(boolean activatedNotifications) {
		this.activatedNotifications = activatedNotifications;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public List<Friendship> getFriendships() {
		return friendships;
	}

	public void setFriendships(List<Friendship> friendships) {
		this.friendships = friendships;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
