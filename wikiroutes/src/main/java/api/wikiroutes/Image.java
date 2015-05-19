package api.wikiroutes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Image {
	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String description;
	private String place;
	private Date date;

	@JsonBackReference
	@ManyToOne
	private Route route;

	public Image() {

	}

	public Image(String description, String place, Date date) {
		this.description = description;
		this.place = place;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
