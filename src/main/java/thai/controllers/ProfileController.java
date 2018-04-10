package thai.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import thai.exceptions.InvalidImageException;
import thai.model.Profile;
import thai.services.ProfileService;

@Controller
public class ProfileController {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    @Autowired
    private ProfileService profileService;

    @Value("${photo.directory}")
    private String photoDirectory;

    @GetMapping("user")
    public String showProfile(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("editable", true);
        viewProfile(username, model);
        return "user";
    }

    @GetMapping("user/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        model.addAttribute("editable", false);
        viewProfile(username, model);
        return "user";
    }

    private void viewProfile(String username, Model model) {
        Profile profile = profileService.getProfileByUsername(username);
        Profile profileDto = new Profile();
        profileDto.setInfo(profile.getInfo());

        model.addAttribute("username", username);
        model.addAttribute("profile", profileDto);
    }

    @GetMapping("edit-profile")
    public String editProfile(Principal principal, Model model) {
        String username = principal.getName();
        Profile profile = profileService.getProfileByUsername(username);

        Profile profileDto = new Profile();
        profileDto.setInfo(profile.getInfo());

        model.addAttribute("profile", profileDto);
        return "edit-profile";
    }

    @PostMapping("edit-profile")
    public String saveProfile(Principal principal, @Valid Profile profileDto, BindingResult bindingResult) {
        String username = principal.getName();
        Profile profile = profileService.getProfileByUsername(username);
        profile.setInfo(profileDto.getInfo());

        if (!bindingResult.hasErrors()) {
            profileService.save(profile);
            return "redirect:user";
        }
        return "edit-profile";
    }

    @GetMapping("profile-photo/{username}")
    public ResponseEntity<InputStreamResource> viewProfilePhoto(@PathVariable String username) throws IOException {
        String photoPath = "static/img/portal.png";
        Profile profile = profileService.getProfileByUsername(username);

        InputStream is = null;
        if (profile == null || profile.getPhotoPath() == null || profile.getPhotoPath().equals("")) {
            Resource classPathResource = new ClassPathResource(photoPath);
            is = classPathResource.getInputStream();
        } else {
            photoPath = profile.getPhotoPath();
            is = Files.newInputStream(Paths.get(photoPath));
        }

        InputStreamResource resource = new InputStreamResource(is);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath))).body(resource);
    }

    @PostMapping("profile-photo")
    public String savePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.getProfileByUsername(auth.getName());

        String prefix = System.currentTimeMillis() + profile.getId() + "-";
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        Path path = Paths.get(photoDirectory, prefix + originalName);
        try (InputStream is = file.getInputStream()) {
            // Validate file type based on the extension
            if (!file.getContentType().startsWith("image/"))
                throw new InvalidImageException();
            BufferedImage image = ImageIO.read(is);
            // Validate file type based on the actual content
            if (image == null)
                throw new InvalidImageException();
            BufferedImage thumbnail = Thumbnails.of(image).size(WIDTH, HEIGHT).asBufferedImage();
            OutputStream os = Files.newOutputStream(path);
            ImageIO.write(thumbnail, extension, os);
            os.close();
            String oldPhoto = profile.getPhotoPath();
            profile.setPhotoPath(path.toString());
            profileService.save(profile);
            if (oldPhoto != null)
                Files.delete(Paths.get(oldPhoto));
        }
        return "redirect:user";
    }
}
