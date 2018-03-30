package thai.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import thai.model.Message;
import thai.services.MessageService;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("view")
    public String view(Model model, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {
        Page<Message> page = messageService.getPage(pageNumber);
        model.addAttribute("page", page);
        return "view";
    }

    @GetMapping("post-message")
    public String postMessage(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        Message lastMsg = messageService.getLatest();
        model.addAttribute("lastMsg", lastMsg);
        return "post-message";
    }

    @PostMapping("post-message")
    public String postMessage(Model model, @Valid Message message, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            messageService.save(message);
            model.addAttribute("message", new Message());
            return "redirect:view";
        }

        Message lastMsg = messageService.getLatest();
        model.addAttribute("lastMsg", lastMsg);
        return "post-message";
    }

    @GetMapping("delele") // FIXME Change method to DELETE
    public String delete(@RequestParam Long id) {
        messageService.delete(id);
        return "redirect:view";
    }

    @GetMapping("edit-message") // FIXME Change method to PUT
    public String editMessage(Model model, @RequestParam Long id) {
        Message message = messageService.get(id);
        model.addAttribute("message", message);
        return "edit-message";
    }

    @PostMapping("edit-message") // FIXME Change method to PUT
    public String editMessage(Model model, @Valid Message message, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            message.setCreated(new Date());
            message.setModified(new Date());
            messageService.save(message);
            model.addAttribute("message", new Message());
            return "redirect:view";
        }

        Message lastMsg = messageService.getLatest();
        model.addAttribute("lastMsg", lastMsg);
        return "post-message";
    }
}
