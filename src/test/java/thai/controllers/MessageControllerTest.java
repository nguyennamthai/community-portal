package thai.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import thai.model.Message;
import thai.services.MessageService;
import thai.services.PortalUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PortalUserService portalUserService;

    @Test
    @WithMockUser
    public void testGetViewMessages() throws Exception {
        Page<Message> page = Page.empty();
        given(messageService.getPage(anyInt())).willReturn(page);
        mockMvc.perform(get("/view-messages"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("page", page))
               .andExpect(view().name("view-messages"));
    }

    @Test
    @WithMockUser
    public void testGetViewMessagesPerUser() throws Exception {
        given(messageService.getByUser(any())).willReturn(Collections.emptyList());
        mockMvc.perform(get("/view-messages/johndoe"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("messages"))
               .andExpect(view().name("messages-by-user"));
    }

    @Test
    @WithMockUser
    public void testGetAddMessage() throws Exception {
        mockMvc.perform(get("/add-message"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("message"))
               .andExpect(view().name("add-message"));
    }

    @Test
    @WithMockUser
    public void testPostAddMessage() throws Exception {
        mockMvc.perform(post("/add-message").with(csrf()))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:view-messages"));
    }

    @Test
    @WithMockUser
    public void testGetDelete() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:view-messages"));
    }

    @Test
    @WithMockUser
    public void testGetEditMessage() throws Exception {
        Message message = new Message();
        given(messageService.get(anyLong())).willReturn(message);
        mockMvc.perform(get("/edit-message").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("message", message))
               .andExpect(view().name("edit-message"));
    }

    @Test
    @WithMockUser
    public void testPostEditMessage() throws Exception {
        mockMvc.perform(post("/edit-message").with(csrf()))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:view-messages"));
    }
}
