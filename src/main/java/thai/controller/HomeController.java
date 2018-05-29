package thai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thai.domain.Message;
import thai.service.MessageService;

@Controller
public class HomeController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String home(Model model) {
        Message message = messageService.getLatest();
        model.addAttribute("message", message);
        return "home";
    }

    @GetMapping("about")
    public String describe() {
        return "about";
    }

    @GetMapping("403")
    public String accessDenied() {
        return "403";
    }
}
