package thai.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thai.model.Member;
import thai.model.Profile;
import thai.repositories.ProfileRepository;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    
    public void save(Profile profile) {
        profileRepository.save(profile);
    }
    
    public Profile getProfile(Member member) {
        return profileRepository.findByMember(member);
    }
    
    public Profile getById(long id) {
        return profileRepository.findById(id).get();
    }
}
