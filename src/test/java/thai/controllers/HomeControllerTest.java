package thai.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import thai.model.Message;
import thai.model.PortalUser;
import thai.services.MessageService;
import thai.services.PortalUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(secure = false)
public class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PortalUserService portalUserService;

    @Test
    public void testHome() throws Exception {
        Message message = new Message();
        message.setContent("Hello World");
        given(messageService.getLatest()).willReturn(message);
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"))
               .andExpect(model().attribute("message", message))
               .andExpect(content().string(containsString("Hello World")));
    }

    @Test
    public void testAbout() throws Exception {
        mockMvc.perform(get("/about"))
               .andExpect(status().isOk())
               .andExpect(view().name("about"))
               .andExpect(content().string(containsString("About This Portal")));
    }

    @Test
    public void testViewUsers() throws Exception {
        PortalUser user = new PortalUser();
        user.setUsername("johndoe");
        given(portalUserService.getAllUsers()).willReturn(Arrays.asList(user));
        mockMvc.perform(get("/view-users"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("users", Arrays.asList(user)))
               .andExpect(view().name("view-users"))
               .andExpect(content().string(containsString("johndoe")));
    }

    @Test
    public void test403() throws Exception {
        mockMvc.perform(get("/403"))
               .andExpect(status().isOk())
               .andExpect(view().name("403"))
               .andExpect(content().string(containsString("Access denied")));
    }
    
    @Test
    public void testGlobalException() throws Exception {
        given(messageService.getLatest()).willThrow(new RuntimeException("An error occurred"));
        mockMvc.perform(get("/"))
               .andExpect(status().isInternalServerError())
               .andExpect(model().attribute("exception", "An error occurred"))
               .andExpect(view().name("exception"))
               .andExpect(content().string(containsString("Failed URL:")));
    }
}
