package thai.model;

import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;
import thai.validators.UsernameConstraint;

@Getter
@Setter
public class UserDto {
    @UsernameConstraint
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    private String password;

    private String passRetyped;
}
