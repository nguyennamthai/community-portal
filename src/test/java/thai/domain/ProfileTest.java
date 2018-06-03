package thai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testProfileGettersAndSetters() {
        Long id = 20L;
        PortalUser user = new PortalUser();
        String info = "Basic information";
        String imagePath = "/images";

        Profile profile = new Profile();
        profile.setId(20L);
        profile.setPortalUser(user);
        profile.setInfo("Basic information");
        profile.setImagePath("/images");

        assertThat(profile)
          .extracting(Profile::getId, Profile::getPortalUser, Profile::getInfo, Profile::getImagePath)
          .containsExactly(id, user, info, imagePath);
    }
}
