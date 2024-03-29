package api.wikiroutes;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import api.wikiroutes.TypeRoute.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Route {

	@Id
	@Column(name = "route_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@JsonIgnore
	@ManyToOne
	private User user;

    @Enumerated(EnumType.STRING)
	private Type type;
	private double rate;
	private boolean isPrivate;

	@JsonManagedReference
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
	private List<Image> images;

	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
	private List<Stretch> stretches;

	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	@Column(columnDefinition = "TEXT")
	private String image;

	public Route() {

	}

	public Route(String name, String description, User user, Type type,
			double rate, boolean isPrivate) {
		this.name = name;
		this.description = description;
		this.user = user;
		this.type = type;
		this.rate = rate;
		this.isPrivate = isPrivate;
	}

	public Route(String name, String description, User user, double rate,
			boolean isPrivate) {
		this.name = name;
		this.description = description;
		this.user = user;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
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

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}