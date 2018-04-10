package thai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thai.model.Message;
import thai.model.PortalUser;
import thai.services.MessageService;
import thai.services.PortalUserService;

@Controller
public class HomeController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private PortalUserService portalUserService;

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

    @GetMapping("view-users")
    public String viewUsers(Model model) {
        Iterable<PortalUser> users = portalUserService.getAll();
        model.addAttribute("users", users);
        return "view-users";
    }
}
