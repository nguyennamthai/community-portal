package thai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thai.domain.Profile;
import thai.repository.ProfileRepository;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile getByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
}
