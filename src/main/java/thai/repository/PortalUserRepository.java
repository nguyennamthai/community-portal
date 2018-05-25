package thai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import thai.domain.PortalUser;

public interface PortalUserRepository extends CrudRepository<PortalUser, Long> {
    PortalUser findByUsername(String username);

    PortalUser findByEmail(String email);

    @Query("SELECT username FROM PortalUser u WHERE u.email = ?1")
    String findUsernameByEmail(String email);
}
