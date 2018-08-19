package thai.service;

import thai.domain.Profile;

public interface ProfileService {
    void save(Profile profile);

    Profile getByUsername(String username);
}
