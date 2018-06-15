package thai.service.dto.mapper;

import org.springframework.stereotype.Component;
import thai.domain.PortalUser;
import thai.domain.Profile;
import thai.service.dto.UserDto;

@Component
public class UserMapper {
    public PortalUser convertUserDtoToPortalUser(UserDto userDto) {
        PortalUser portalUser = new PortalUser();
        portalUser.setUsername(userDto.getUsername());
        portalUser.setEmail(userDto.getEmail());
        portalUser.setPassword(userDto.getPassword());
        portalUser.setProfile(new Profile());
        return portalUser;
    }
}
