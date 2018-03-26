package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.PortalUser;
import thai.model.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByPortalUser(PortalUser portalUser);
}
