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
    private PortalUserRepository portalUserRepository;
    private PasswordEncoder passwordEncoder;

    public PortalUserServiceImpl(PortalUserRepository portalUserRepository, PasswordEncoder passwordEncoder) {
        this.portalUserRepository = portalUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
            username = findUsernameByEmail(username);
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(portalUser.getRole().name());
        String password = portalUser.getPassword();
        return new User(username, password, roles);
    }

    private String findUsernameByEmail(String email) {
        PortalUser portalUser = portalUserRepository.findByEmail(email);
        if (portalUser == null) {
            throw new UsernameNotFoundException("The user " + email + " could not be found");
        }
        return portalUserRepository.findUsernameByEmail(email);
    }
}
