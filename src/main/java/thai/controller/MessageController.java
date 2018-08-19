package thai.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import thai.domain.Message;
import thai.domain.PortalUser;
import thai.service.MessageService;
import thai.service.PortalUserService;
import thai.service.dto.MessageDto;

import static thai.domain.PortalUser.Role.ADMIN;
import static thai.service.dto.mapper.MessageMapper.convertMessageDtoToMessage;
import static thai.service.dto.mapper.MessageMapper.convertMessageToMessageDto;

@Slf4j
@Controller
public class MessageController {
    private static final String ADD_MESSAGE = "add-message";
    private static final String EDIT_MESSAGE = "edit-message";
    private static final String VIEW_MESSAGES = "view-messages";
    private static final String REDIRECT_VIEW_MESSAGES = "redirect:view-messages";

    private final MessageService messageService;
    private final PortalUserService portalUserService;

    public MessageController(MessageService messageService, PortalUserService portalUserService) {
        this.messageService = messageService;
        this.portalUserService = portalUserService;
    }

    @GetMapping("view-messages")
    public String viewMessages(Principal principal, Model model, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {
        PortalUser user = portalUserService.getByUsername(principal.getName());
        Page<Message> page = messageService.getPage(pageNumber);
        Page<MessageDto> pageDto = page.map(message -> {
            MessageDto messageDto = convertMessageToMessageDto(message);
            if (user.equals(message.getUser()) || user.getRole() == ADMIN) {
                messageDto.setEditable(true);
            }
            return messageDto;
        });

        model.addAttribute("page", pageDto);
        return VIEW_MESSAGES;
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
        return ADD_MESSAGE;
    }

    @PostMapping("add-message")
    public String addMessage(Principal principal, @Valid @ModelAttribute("message") MessageDto messageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return ADD_MESSAGE;
        }
        return redirectViewMessages(principal.getName(), messageDto);
    }

    @GetMapping("edit-message")
    public String editMessage(Model model, @RequestParam Long id) {
        Message message = messageService.get(id);
        MessageDto messageDto = convertMessageToMessageDto(message);
        model.addAttribute("message", messageDto);
        return EDIT_MESSAGE;
    }

    @PostMapping("edit-message")
    public String editMessage(Principal principal, @Valid @ModelAttribute("message") MessageDto messageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return EDIT_MESSAGE;
        }
        return redirectViewMessages(principal.getName(), messageDto);
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id) {
        messageService.delete(id);
        return REDIRECT_VIEW_MESSAGES;
    }

    private String redirectViewMessages(String name, MessageDto messageDto) {
        PortalUser user = portalUserService.getByUsername(name);
        Message message = convertMessageDtoToMessage(messageDto);
        message.setUser(user);
        messageService.save(message);
        return REDIRECT_VIEW_MESSAGES;
    }
}
