package thai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import thai.domain.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p JOIN PortalUser u ON p = u.profile WHERE u.username = ?1")
    Profile findByUsername(String username);
}
