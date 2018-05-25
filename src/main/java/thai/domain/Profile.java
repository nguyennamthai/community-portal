package thai.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Profile {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "profile")
    private PortalUser portalUser;

    @Column(length = 5000)
    private String info;

    @Size(max = 255, message = "The file name is too long")
    private String photoPath;
}
