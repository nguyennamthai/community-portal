package thai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import thai.model.PortalUser;
import thai.model.PortalUser.Role;
import thai.repositories.PortalUserRepository;

@Service
public class PortalUserService implements UserDetailsService {
    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(PortalUser portalUser) {
        portalUser.setPassword(passwordEncoder.encode(portalUser.getPassword()));
        portalUser.setRole(Role.MEMBER);
        portalUserRepository.save(portalUser);
    }

    public PortalUser getUserById(long id) {
        return portalUserRepository.findById(id).get();
    }

    public PortalUser getUserByUsername(String username) {
        return portalUserRepository.findByUsername(username);
    }

    public PortalUser getUserByEmail(String email) {
        return portalUserRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PortalUser portalUser = portalUserRepository.findByUsername(username);
        if (portalUser == null) {
            portalUser = portalUserRepository.findByEmail(username);
            if (portalUser == null)
                throw new UsernameNotFoundException("User " + username + " isn't found");
            username = portalUserRepository.findUsernameByEmail(username);
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(portalUser.getRole().name());
        String password = portalUser.getPassword();
        return new User(username, password, roles);
    }
}
