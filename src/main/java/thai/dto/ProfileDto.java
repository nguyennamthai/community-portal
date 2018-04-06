package thai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private PortalUserDto portalUser;

    private String info;

    private String photoPath;
}
