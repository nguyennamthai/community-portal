package thai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import lombok.Data;

@Data
@Entity
public class PortalUser {
    // TODO Add a many-to-many relationship with Message
    @Id
    @GeneratedValue
    private Long id;

    // TODO Add a constraint to avoid the @ character
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    @Email(message = "Invalid email address")
    private String email;

    // TODO Use a custom validator to verify the repeated password
    // TODO Add a separate username column to be used in the url
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, MEMBER
    }
}
