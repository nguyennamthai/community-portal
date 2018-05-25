package thai.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import thai.domain.PortalUser;

public interface PortalUserService extends UserDetailsService {
    public void save(PortalUser user);

    public PortalUser getByUsername(String username);

    public PortalUser getByEmail(String email);

    public Iterable<PortalUser> getAll();
}
