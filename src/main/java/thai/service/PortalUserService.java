package thai.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import thai.domain.PortalUser;

public interface PortalUserService extends UserDetailsService {
    void save(PortalUser user);

    PortalUser getByUsername(String username);

    Iterable<PortalUser> getAll();
}
