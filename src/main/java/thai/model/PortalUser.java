package thai.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import thai.validators.UsernameConstraint;

@Getter
@Setter
@Entity
public class PortalUser {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    @UsernameConstraint
    private String username;

    @Column(unique = true)
    @Email(message = "Invalid email address")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;
}
