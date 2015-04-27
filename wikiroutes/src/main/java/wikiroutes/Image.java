package wikiroutes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Image {
	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String description;
	private String place;
	private Date date;

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

}
