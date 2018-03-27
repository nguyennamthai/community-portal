package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.PortalUser;

public interface PortalUserRepository extends CrudRepository<PortalUser, Long> {
    PortalUser findByEmail(String email);
}
