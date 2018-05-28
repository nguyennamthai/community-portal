package thai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thai.domain.PortalUser;
import thai.repository.PortalUserRepository;
import thai.service.PortalUserService;

import java.util.List;

@Service
public class PortalUserServiceImpl implements PortalUserService {
    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(PortalUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(PortalUser.Role.MEMBER);
        portalUserRepository.save(user);
    }

    public PortalUser getByUsername(String username) {
        return portalUserRepository.findByUsername(username);
    }

    public PortalUser getByEmail(String email) {
        return portalUserRepository.findByEmail(email);
    }

    public Iterable<PortalUser> getAll() {
        return portalUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PortalUser portalUser = portalUserRepository.findByUsername(username);
        if (portalUser == null) {
            portalUser = portalUserRepository.findByEmail(username);
            if (portalUser == null)
                throw new UsernameNotFoundException("The user " + username + " could not be found");
            username = portalUserRepository.findUsernameByEmail(username);
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(portalUser.getRole().name());
        String password = portalUser.getPassword();
        return new User(username, password, roles);
    }
}