package thai.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import thai.model.Message;
import thai.model.Profile;
import thai.model.Role;

@Getter
@Setter
public class PortalUser {
    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role;

    private Profile profile;

    private List<Message> messages;
}
