package wikiroutes;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


public class Route {
	@Id
	@Column(name = "route_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private User user;
	private TypeRoute type;
	private double rate;
	private boolean isPrivate;
	
	public Route(){
		
	}
	
	public Route(String name, String description, User user, TypeRoute type, double rate, boolean isPrivate){
		this.name = name;
		this.description = description;
		this.user = user;
		this.type = type;
		this.rate = rate;
		this.isPrivate = isPrivate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TypeRoute getType() {
		return type;
	}

	public void setType(TypeRoute type) {
		this.type = type;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Long getId() {
		return id;
	}
	
	
}