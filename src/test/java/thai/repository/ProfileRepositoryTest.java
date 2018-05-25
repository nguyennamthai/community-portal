package thai.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import thai.domain.PortalUser;
import thai.domain.Profile;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProfileRepositoryTest {
    @Autowired
    private PortalUserRepository portalUserRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testFindByUsername() {
        String username = "johndoe";
        Profile input = new Profile();

        PortalUser user = new PortalUser();
        user.setUsername(username);
        user.setEmail("johndoe@company.com");
        user.setProfile(input);
        portalUserRepository.save(user);

        Profile output = profileRepository.findByUsername(username);
        assertThat(output).isEqualTo(input);
    }
}
