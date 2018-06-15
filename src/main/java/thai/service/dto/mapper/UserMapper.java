package thai.service.dto.mapper;

import thai.domain.PortalUser;
import thai.domain.Profile;
import thai.service.dto.UserDto;

public class UserMapper {
    private UserMapper() { }

    public static PortalUser convertUserDtoToPortalUser(UserDto userDto) {
        PortalUser portalUser = new PortalUser();
        portalUser.setUsername(userDto.getUsername());
        portalUser.setEmail(userDto.getEmail());
        portalUser.setPassword(userDto.getPassword());
        portalUser.setProfile(new Profile());
        return portalUser;
    }
}
