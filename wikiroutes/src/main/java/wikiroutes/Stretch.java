package wikiroutes;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Stretch {
	@Id
	@Column(name = "strecht_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Point initialPoint;
	private Point finalPoint;
	private int order;

	public Stretch() {

	}

	public Stretch(Point initialPoint, Point finalPoint, int order) {
		this.initialPoint = initialPoint;
		this.finalPoint = finalPoint;
		this.order = order;
	}

	public Point getInitialPoint() {
		return initialPoint;
	}

	public void setInitialPoint(Point initialPoint) {
		this.initialPoint = initialPoint;
	}

	public Point getFinalPoint() {
		return finalPoint;
	}

	public void setFinalPoint(Point finalPoint) {
		this.finalPoint = finalPoint;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Long getId() {
		return id;
	}

}
