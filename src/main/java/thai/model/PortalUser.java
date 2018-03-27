package thai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        ADMIN, MEMBER
    }
}
