package thai.service.dto.mapper;

import org.springframework.stereotype.Component;
import thai.domain.Profile;
import thai.service.dto.ProfileDto;

@Component
public class ProfileMapper {
    public ProfileDto convertProfileToProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setInfo(profile.getInfo());
        return profileDto;
    }
}
