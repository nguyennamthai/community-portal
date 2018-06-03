package thai.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import thai.domain.Profile;
import thai.service.ProfileService;

@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Test
    @WithMockUser
    public void testGetUser() throws Exception {
        Profile profile = new Profile();
        profile.setInfo("Basic information");
        given(profileService.getByUsername(anyString())).willReturn(profile);
        mockMvc.perform(get("/user"))
               .andExpect(status().isOk())
               .andExpect(view().name("user"))
               .andExpect(model().attribute("username", "user"))
               .andExpect(model().attribute("editable", true))
               .andExpect(content().string(containsString("Basic information")));
    }

    @Test
    @WithMockUser
    public void testGetUserWithUsername() throws Exception {
        Profile profile = new Profile();
        profile.setInfo("My name is John Doe");
        given(profileService.getByUsername("johndoe")).willReturn(profile);
        mockMvc.perform(get("/user/johndoe"))
               .andExpect(status().isOk())
               .andExpect(view().name("user"))
               .andExpect(model().attribute("username", "johndoe"))
               .andExpect(model().attribute("editable", false))
               .andExpect(content().string(containsString("My name is John Doe")));
    }

    @Test
    @WithMockUser
    public void testGetEditProfile() throws Exception {
        Profile profile = new Profile();
        profile.setInfo("Basic Information");
        given(profileService.getByUsername("user")).willReturn(profile);
        mockMvc.perform(get("/edit-profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("edit-profile"))
               .andExpect(model().attributeExists("profile"))
               .andExpect(content().string(containsString("Basic Information")));
    }

    @Test
    @WithMockUser
    public void testPostEditProfile() throws Exception {
        given(profileService.getByUsername("user")).willReturn(new Profile());
        mockMvc.perform(post("/edit-profile").with(csrf()).param("info", "Information"))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:user"));
    }

    @Test
    @WithMockUser
    public void testGetProfileImage() throws Exception {
        given(profileService.getByUsername(anyString())).willReturn(new Profile());
        mockMvc.perform(get("/profile-image/johndoe"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    @WithMockUser
    public void testPostProfileImage() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        given(profileService.getByUsername("user")).willReturn(profile);

        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", new byte[0]);
        mockMvc.perform(multipart(new URI("/profile-image")).file(file).with(csrf()))
               .andExpect(status().isUnsupportedMediaType())
               .andExpect(view().name("exception"));
    }
}
