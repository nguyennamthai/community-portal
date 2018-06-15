package thai.service.dto.mapper;

import thai.domain.Profile;
import thai.service.dto.ProfileDto;

public class ProfileMapper {
    private ProfileMapper() { }

    public static ProfileDto convertProfileToProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setInfo(profile.getInfo());
        return profileDto;
    }
}
