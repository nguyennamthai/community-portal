package thai.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import thai.domain.Message;
import thai.domain.PortalUser;
import thai.service.MessageService;
import thai.service.PortalUserService;
import thai.service.dto.MessageDto;

@Slf4j
@Controller
public class MessageController {
    private MessageService messageService;
    private PortalUserService portalUserService;

    public MessageController(MessageService messageService, PortalUserService portalUserService) {
        this.messageService = messageService;
        this.portalUserService = portalUserService;
    }

    @GetMapping("view-messages")
    public String viewMessages(Model model, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {
        Page<Message> page = messageService.getPage(pageNumber);
        model.addAttribute("page", page);
        return "view-messages";
    }

    @GetMapping("view-messages/{username}")
    public String viewMessagesByUser(Model model, @PathVariable String username) {
        PortalUser user = portalUserService.getByUsername(username);
        List<Message> messages = messageService.getByUser(user);
        model.addAttribute("messages", messages);
        return "messages-by-user";
    }

    @GetMapping("add-message")
    public String addMessage(Model model) {
        MessageDto messageDto = new MessageDto();
        model.addAttribute("message", messageDto);
        return "add-message";
    }

    @PostMapping("add-message")
    public String addMessage(Principal principal, @Valid MessageDto messageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return "add-message";
        }

        PortalUser user = portalUserService.getByUsername(principal.getName());

        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setModified(messageDto.getModified());

        messageService.save(message);
        return "redirect:view-messages";
    }

    @GetMapping("edit-message")
    public String editMessage(Model model, @RequestParam Long id) {
        Message message = messageService.get(id);
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setContent(message.getContent());
        messageDto.setModified(message.getModified());

        model.addAttribute("message", messageDto);
        return "edit-message";
    }

    @PostMapping("edit-message")
    public String editMessage(@Valid MessageDto messageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return "edit-message";
        }

        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setModified(messageDto.getModified());

        messageService.save(message);
        return "redirect:view-messages";
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id) {
        messageService.delete(id);
        return "redirect:view-messages";
    }
}
