package thai.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import lombok.Data;

@Data
@Entity
public class PortalUser {
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
    // TODO Use username in the url instead of id
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    
    // TODO Add a one-to-one relationship to Profile

    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    public enum Role {
        ADMIN, MEMBER
    }
}
