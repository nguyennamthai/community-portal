package thai.model;

import javax.validation.constraints.Email;

import lombok.Data;
import thai.validators.UsernameConstraint;

@Data
public class UserDto {
    @UsernameConstraint
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    private String password;

    private String passRetyped;
}
