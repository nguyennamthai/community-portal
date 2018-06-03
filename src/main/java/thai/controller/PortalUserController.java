package thai.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import thai.domain.PortalUser;
import thai.domain.Profile;
import thai.service.dto.UserDto;
import thai.service.PortalUserService;

@Slf4j
@Controller
public class PortalUserController {
    private PortalUserService portalUserService;

    public PortalUserController(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        UserDto portalUser = new UserDto();
        model.addAttribute("user", portalUser);
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return "signup";
        }

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
