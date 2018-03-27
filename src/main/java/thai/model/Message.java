package thai.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Message {
    // TODO Add a many-to-many relationship with PortalUser
    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 5, max = 255, message = "Enter between {min} and {max} characters")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public Message(String text, Date modified) {
        this.content = text;
        this.modified = modified;
    }

    @PrePersist
    protected void onCreate() {
        if (modified != null)
            return;
        modified = new Date();
    }
}
