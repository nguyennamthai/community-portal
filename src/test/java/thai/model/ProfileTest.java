package thai.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testProfileGettersAndSetters() {
        PortalUser user = new PortalUser();
        Profile profile = new Profile();
        profile.setId(20L);
        profile.setPortalUser(user);
        profile.setInfo("Basic information");
        profile.setPhotoPath("/images");

        assertThat(profile)
          .extracting(Profile::getId, Profile::getPortalUser, Profile::getInfo, Profile::getPhotoPath)
          .containsExactly(20L, user, "Basic information", "/images");
    }
}
