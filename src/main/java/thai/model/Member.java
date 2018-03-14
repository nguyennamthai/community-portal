package thai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
public class Member {
    // TODO Add a many-to-many relationship with Message
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Email(message = "Invalid email address")
    private String email;
    // TODO Enforce a validation rule for passwords, keep in mind that the persisted values are encrypted
    // TODO Use a custom validator to verify the repeated password
    private String password;
    private String role; // FIXME Change to enum type Role

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public enum Role {
        ADMIN, USER
    }
}
