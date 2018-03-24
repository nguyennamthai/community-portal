package thai.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import thai.model.Member;
import thai.model.Profile;
import thai.services.MemberService;
import thai.services.ProfileService;

@Controller
public class ProfileController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("profile")
    public String showProfile(Principal principal, Model model) {
        String email = principal.getName();
        Member member = memberService.getMember(email);
        Profile profile = profileService.getProfile(member);

        if (profile == null) {
            profile = new Profile();
            profile.setMember(member);
            profileService.save(profile);
        }

        Profile viewProfile = new Profile();
        viewProfile.setInfo(profile.getInfo());

        model.addAttribute("profile", viewProfile);
        return "profile";
    }

    @GetMapping("edit-profile")
    public String editProfile(Principal principal, Model model) {
        // FIXME the following code snippet is the same as in showProfile
        String email = principal.getName();
        Member member = memberService.getMember(email);
        Profile profile = profileService.getProfile(member);
        
        Profile viewProfile = new Profile();
        viewProfile.setInfo(profile.getInfo());

        model.addAttribute("profile", viewProfile);
        return "edit-profile";
    }
    
    @PostMapping("edit-profile")
    public String saveProfile(Principal principal, @Valid Profile viewProfile, BindingResult bindingResult) {
        String email = principal.getName();
        Member member = memberService.getMember(email);
        Profile profile = profileService.getProfile(member);
        
        profile.setInfo(viewProfile.getInfo());

        if (!bindingResult.hasErrors()) {
            profileService.save(profile);
            return "redirect:profile";
        }
        return "edit-profile";
    }
}
