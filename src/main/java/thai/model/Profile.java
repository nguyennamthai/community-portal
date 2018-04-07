package thai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "portalUser")
@Entity
public class Profile {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "profile")
    private PortalUser portalUser;

    @Column(length = 5000)
    @Size(max = 5000, message = "The information is too long")
    private String info;

    @Size(max = 255, message = "The file name is too long")
    private String photoPath;
}
