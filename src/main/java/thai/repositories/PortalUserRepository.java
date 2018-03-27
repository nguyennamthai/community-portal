package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.PortalUser;

public interface PortalUserRepository extends CrudRepository<PortalUser, Long> {
    PortalUser findByUsername(String username);

    PortalUser findByEmail(String email);
}
