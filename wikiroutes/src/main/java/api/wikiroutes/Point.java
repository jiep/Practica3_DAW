package api.wikiroutes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Point {
	@Id
	@Column(name = "point_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private double latitude;
	private double longitude;
	private double altitude;

	@JsonIgnore
	@ManyToOne
	private Stretch stretch;

	public Point() {

	}

	public Point(double latitude, double longitude, double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public Stretch getStretch() {
		return stretch;
	}

	public void setStretch(Stretch stretch) {
		this.stretch = stretch;
	}
}
