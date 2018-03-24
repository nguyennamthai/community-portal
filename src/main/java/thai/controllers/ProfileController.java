package thai.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thai.model.Profile;

@Controller
public class ProfileController {
    @GetMapping("profile")
    public String showProfile(Principal principal, Model model) {
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        return "profile";
    }
}
