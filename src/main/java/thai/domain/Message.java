package thai.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @ManyToOne
    private PortalUser user;

    @PrePersist
    private void onCreate() {
        modified = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        modified = new Date();
    }
}
