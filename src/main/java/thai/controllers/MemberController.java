package thai.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import thai.model.PortalUser;
import thai.services.PortalUserService;

@Controller
public class MemberController {

    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("login")
    public String admin() {
        return "login";
    }

    @GetMapping("register")
    public String register(Model model) {
        PortalUser portalUser = new PortalUser();
        model.addAttribute("member", portalUser);
        return "register";
    }

    @PostMapping("register")
    public ModelAndView register(ModelMap model, @Valid PortalUser portalUser, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            portalUserService.save(portalUser);
            return new ModelAndView("redirect:/", model);
        }
        return new ModelAndView("register", model);
    }
}
