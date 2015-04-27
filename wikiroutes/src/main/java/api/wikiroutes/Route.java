package api.wikiroutes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Route {

	@Id
	@Column(name = "route_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeroute_id")
	private TypeRoute type;
	private double rate;
	private boolean isPrivate;

	@OneToMany(fetch = FetchType.LAZY)
	private List<Stretch> stretches;

	@OneToMany(fetch = FetchType.LAZY)
	private List<Comment> comments;

	public Route() {

	}

	public Route(String name, String description, User user, TypeRoute type,
			double rate, boolean isPrivate) {
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

	public List<Stretch> getStretches() {
		return stretches;
	}

	public void setStretches(List<Stretch> stretches) {
		this.stretches = stretches;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setId(Long id) {
		this.id = id;
	}

}