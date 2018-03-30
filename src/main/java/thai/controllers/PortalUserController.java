package thai.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import thai.model.PortalUser;
import thai.services.PortalUserService;

@Controller
public class PortalUserController {

    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("login")
    public String admin() {
        return "login";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        PortalUser portalUser = new PortalUser();
        model.addAttribute("member", portalUser);
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@Valid PortalUser portalUser, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            portalUserService.save(portalUser);
            return "redirect:/";
        }
        return "signup";
    }
}
