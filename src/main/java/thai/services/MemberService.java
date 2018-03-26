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

import thai.model.Member;
import thai.repositories.MemberRepository;

@Service
public class MemberService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole("ROLE_MEMBER");
        memberRepository.save(member);
    }
    
    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public Member getMemberbyId(long id) {
        return memberRepository.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("User " + email + " isn't found");
        }

        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(member.getRole());
        String password = member.getPassword();
        return new User(email, password, roles);
    }
}
