package thai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private Long id;

    private PortalUser portalUser;

    private String info;

    private String photoPath;
}
