package entitymgr.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
@Entity(name = "Event")
@Table(name = "Events")
public class Event {
    @Id
    @Column(name = "Name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TriggeredAt")
    private Date triggeredAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ExpireAt")
    private Date expireAt;

    public Event() {
    }

    public Event(final String name) {
        this.name = name;
        this.triggeredAt = new Date();
        this.expireAt = new Date(this.triggeredAt.getTime() + 5000);
    }

    public Date getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(final Date triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
