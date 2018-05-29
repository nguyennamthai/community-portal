package thai.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import thai.domain.PortalUser;
import thai.service.PortalUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(PortalUserController.class)
public class PortalUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortalUserService portalUserService;

    @Test
    @WithMockUser
    public void testGetSignup() throws Exception {
        mockMvc.perform(get("/signup"))
               .andExpect(status().isOk())
               .andExpect(view().name("signup"))
               .andExpect(model().attributeExists("userDto"))
               .andExpect(content().string(containsString("Account")));
    }

    @Test
    @WithMockUser
    public void testPostSignup() throws Exception {
        mockMvc.perform(post("/signup").with(csrf())
                                       .param("username", "johndoe")
                                       .param("email", "johndoe@company.com")
                                       .param("password", "password")
                                       .param("passRetyped", "password"))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser
    public void testGetViewUsers() throws Exception {
        PortalUser user = new PortalUser();
        user.setUsername("johndoe");
        given(portalUserService.getAll()).willReturn(Collections.singletonList(user));

        mockMvc.perform(get("/view-users"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("users", Collections.singletonList(user)))
               .andExpect(view().name("view-users"))
               .andExpect(content().string(containsString("johndoe")));
    }
}
