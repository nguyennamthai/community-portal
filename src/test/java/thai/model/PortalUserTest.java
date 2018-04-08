package thai.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PortalUserTest {
    @Test
    public void testPortalUserGettersAndSetters() {
        Profile profile = new Profile();
        Message message = new Message();
        List<Message> messages = Arrays.asList(message);

        PortalUser user = new PortalUser();
        user.setId(5L);
        user.setUsername("johndoe");
        user.setEmail("johndoe@company.com");
        user.setPassword("password");
        user.setRole(PortalUser.Role.MEMBER);
        user.setProfile(profile);
        user.setMessages(messages);

        assertThat(user)
          .extracting(PortalUser::getId, PortalUser::getUsername, PortalUser::getEmail, PortalUser::getPassword, PortalUser::getRole, PortalUser::getProfile, PortalUser::getMessages)
          .containsExactly(5L, "johndoe", "johndoe@company.com", "password", PortalUser.Role.MEMBER, profile, messages);
    }
}
