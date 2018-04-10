package thai.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import thai.model.Message;
import thai.services.MessageService;
import thai.services.PortalUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PortalUserService portalUserService;

    @Test
    @WithMockUser
    public void testGetHome() throws Exception {
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
    @WithMockUser
    public void testGetAbout() throws Exception {
        mockMvc.perform(get("/about"))
               .andExpect(status().isOk())
               .andExpect(view().name("about"))
               .andExpect(content().string(containsString("About This Portal")));
    }

    @Test
    @WithMockUser
    public void testGet403() throws Exception {
        mockMvc.perform(get("/403"))
               .andExpect(status().isOk())
               .andExpect(view().name("403"))
               .andExpect(content().string(containsString("Access denied")));
    }

    @Test
    @WithMockUser
    public void testGlobalException() throws Exception {
        given(messageService.getLatest()).willThrow(new RuntimeException("An error occurred"));
        mockMvc.perform(get("/"))
               .andExpect(status().isInternalServerError())
               .andExpect(model().attribute("exception", "An error occurred"))
               .andExpect(view().name("exception"))
               .andExpect(content().string(containsString("Failed URL:")));
    }
}
