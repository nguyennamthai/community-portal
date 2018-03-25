package thai.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import thai.exceptions.InvalidImageException;
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

    @Value("${photo.directory}")
    private String photoDirectory;

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

    @PostMapping("upload-profile-photo")
    public String savePhotos(@RequestParam("file") MultipartFile file) {
        Path path = Paths.get(photoDirectory, file.getOriginalFilename());
        try {
            InputStream inputFile = file.getInputStream();
            BufferedImage image = ImageIO.read(inputFile);
            if (image == null)
                throw new InvalidImageException();
            Files.copy(inputFile, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            // TODO Log the exception
            e1.printStackTrace();
        }
        return "redirect:profile";
    }
}
