package thai.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import thai.exceptions.PasswordMismatchException;
import thai.model.PortalUser;
import thai.model.Profile;
import thai.model.UserDto;
import thai.services.PortalUserService;

@Controller
public class PortalUserController {
    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        UserDto portalUser = new UserDto();
        model.addAttribute("userDto", portalUser);
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "signup";
        if (!userDto.getPassword().equals(userDto.getPassRetyped()))
            throw new PasswordMismatchException();

        PortalUser portalUser = new PortalUser();
        portalUser.setUsername(userDto.getUsername());
        portalUser.setEmail(userDto.getEmail());
        portalUser.setPassword(userDto.getPassword());
        portalUser.setProfile(new Profile());

        portalUserService.save(portalUser);
        return "redirect:/";
    }

    @GetMapping("view-users")
    public String viewUsers(Model model) {
        Iterable<PortalUser> users = portalUserService.getAll();
        model.addAttribute("users", users);
        return "view-users";
    }
}
