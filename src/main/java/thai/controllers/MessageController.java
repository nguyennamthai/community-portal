package thai.controllers;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import thai.model.Message;
import thai.model.PortalUser;
import thai.services.MessageService;
import thai.services.PortalUserService;

@Slf4j
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("view-messages")
    public String viewMessages(Model model, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {
        Page<Message> page = messageService.getPage(pageNumber);
        model.addAttribute("page", page);
        return "view-messages";
    }

    @GetMapping("view-messages/{username}")
    public String viewMessagesPerUser(Model model, @PathVariable String username) {
        PortalUser user = portalUserService.getUserByUsername(username);
        List<Message> messages = messageService.getMessagesPerUser(user);
        model.addAttribute("messages", messages);
        return "messages-per-user";
    }

    @GetMapping("add-message")
    public String addMessage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        return "add-message";
    }

    @PostMapping("add-message")
    public String addMessage(Principal principal, Model model, @Valid Message message, BindingResult bindingResult) {
        PortalUser user = portalUserService.getUserByUsername(principal.getName());
        message.setUser(user);
        if (!bindingResult.hasErrors()) {
            messageService.save(message);
            model.addAttribute("message", new Message());
            return "redirect:view-messages";
        }

        Message lastMsg = messageService.getLatest();
        model.addAttribute("lastMsg", lastMsg);
        return "add-message";
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id) {
        messageService.delete(id);
        return "redirect:view-messages";
    }

    @GetMapping("edit-message")
    public String editMessage(Model model, @RequestParam Long id) {
        Message message = messageService.get(id);
        model.addAttribute("message", message);
        return "edit-message";
    }

    @PostMapping("edit-message")
    public String editMessage(Model model, @Valid Message message, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            messageService.save(message);
            return "redirect:view-messages";
        }

        bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
        Message lastMsg = messageService.getLatest();
        model.addAttribute("lastMsg", lastMsg);
        return "edit-message";
    }
}
