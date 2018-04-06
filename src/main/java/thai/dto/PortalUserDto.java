package thai.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import thai.model.Role;

@Getter
@Setter
public class PortalUserDto {
    private String username;

    private String email;

    private String password;

    private Role role;

    private ProfileDto profile;

    private List<MessageDto> messages;
}
