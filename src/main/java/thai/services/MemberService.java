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
import thai.repositories.MemberRepository;

@Service
public class MemberService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(PortalUser portalUser) {
        portalUser.setPassword(passwordEncoder.encode(portalUser.getPassword()));
        portalUser.setRole("MEMBER");
        memberRepository.save(portalUser);
    }
    
    public PortalUser getMember(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public PortalUser getMemberbyId(long id) {
        return memberRepository.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PortalUser portalUser = memberRepository.findByEmail(email);
        if (portalUser == null) {
            throw new UsernameNotFoundException("User " + email + " isn't found");
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(portalUser.getRole());
        String password = portalUser.getPassword();
        return new User(email, password, roles);
    }
}
