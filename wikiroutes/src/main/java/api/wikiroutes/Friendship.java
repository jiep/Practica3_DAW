package api.wikiroutes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Friendship {
	
	@Id
	@Column(name = "point_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    	
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User sourceUser;

    @ManyToOne()
    @JoinColumn(name = "friend_id")
    private User targetUser;
    
    public Friendship() {
    	
    }
    
    public Friendship(User sourceUser, User targetUser){
    	this.sourceUser = sourceUser;
    	this.targetUser = targetUser;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(User sourceUser) {
		this.sourceUser = sourceUser;
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}
    
    
}