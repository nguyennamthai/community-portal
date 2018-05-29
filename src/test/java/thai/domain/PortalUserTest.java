package thai.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static thai.domain.PortalUser.Role;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PortalUserTest {
    @Test
    public void testPortalUserGettersAndSetters() {
        Long id = 5L;
        String username = "johndoe";
        String email = "johndoe@company.com";
        String password = "password";
        Role role = Role.MEMBER;
        Profile profile = new Profile();
        Message message = new Message();
        List<Message> messages = Collections.singletonList(message);

        PortalUser user = new PortalUser();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setProfile(profile);
        user.setMessages(messages);

        assertThat(user)
          .extracting(PortalUser::getId, PortalUser::getUsername, PortalUser::getEmail, PortalUser::getPassword, PortalUser::getRole, PortalUser::getProfile, PortalUser::getMessages)
          .containsExactly(id, username, email, password, role, profile, messages);
    }
}
