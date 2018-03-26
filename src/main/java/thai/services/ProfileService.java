package thai.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thai.model.PortalUser;
import thai.model.Profile;
import thai.repositories.ProfileRepository;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    
    public void save(Profile profile) {
        profileRepository.save(profile);
    }
    
    public Profile getProfile(PortalUser portalUser) {
        return profileRepository.findByPortalUser(portalUser);
    }
    
    public Profile getById(long id) {
        return profileRepository.findById(id).get();
    }
}
