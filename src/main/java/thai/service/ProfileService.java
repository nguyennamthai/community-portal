package thai.service;

import thai.domain.Profile;

public interface ProfileService {
    public void save(Profile profile);

    public Profile getByUsername(String username);
}
