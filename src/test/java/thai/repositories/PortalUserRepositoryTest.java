package thai.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import thai.model.PortalUser;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PortalUserRepositoryTest {
    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testFindByUsername() {
        String username = "johndoe";
        PortalUser input = new PortalUser();
        input.setUsername(username);
        input.setEmail("johndoe@company.com");
        portalUserRepository.save(input);

        PortalUser output = portalUserRepository.findByUsername(username);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void testFindByEmail() {
        String email = "johndoe@company.com";
        PortalUser input = new PortalUser();
        input.setUsername("johndoe");
        input.setEmail(email);
        portalUserRepository.save(input);

        PortalUser output = portalUserRepository.findByEmail(email);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void testFindAll() {
        PortalUser user1 = new PortalUser();
        user1.setUsername("johndoe");
        user1.setEmail("johndoe@company.com");
        portalUserRepository.save(user1);

        PortalUser user2 = new PortalUser();
        user2.setUsername("janeroe");
        user2.setEmail("janeroe@company.com");
        portalUserRepository.save(user2);

        Iterable<PortalUser> users = portalUserRepository.findAll();
        assertThat(users).containsExactly(user1, user2);
    }

    @Test
    public void testFindUsernameByEmail() {
        String username = "johndoe";
        String email = "johndoe@company.com";

        PortalUser input = new PortalUser();
        input.setUsername(username);
        input.setEmail(email);
        portalUserRepository.save(input);

        String output = portalUserRepository.findUsernameByEmail(email);
        assertThat(output).isEqualTo(username);
    }
}
