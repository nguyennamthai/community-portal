package thai.service.impl;

import org.springframework.stereotype.Service;
import thai.domain.Profile;
import thai.repository.ProfileRepository;
import thai.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {
    private ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile getByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
}
