package thai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thai.model.Message;
import thai.services.MessageService;

@Controller
public class HomeController {
    // TODO handle error 500 and create error page
    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String home(Model model) {
        Message message = messageService.getLatest();
        model.addAttribute("message", message);
        return "home";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }
    
    @GetMapping("403")
    public String accessDenied() {
        return "403";
    }
}
