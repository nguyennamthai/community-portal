package thai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import thai.model.User;
import thai.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " isn't found");
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList("ROLE_USER");
        String password = "{noop}" + user.getPassword(); // FIXME Get rid of no-op
        return new org.springframework.security.core.userdetails.User(email, password, roles);
    }
}
