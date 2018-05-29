package thai.controller;

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

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import thai.exception.InvalidImageException;
import thai.domain.Profile;
import thai.service.dto.ProfileDto;
import thai.service.ProfileService;

@Slf4j
@Controller
public class ProfileController {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private String photoPath = "static/img/portal.png";
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Value("${photo.directory:}")
    private String photoDirectory;

    @GetMapping("user")
    public String showProfile(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("editable", true);
        addAttributes(username, model);
        return "user";
    }

    @GetMapping("user/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        model.addAttribute("editable", false);
        addAttributes(username, model);
        return "user";
    }

    private void addAttributes(String username, Model model) {
        Profile profile = profileService.getByUsername(username);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setInfo(profile.getInfo());

        model.addAttribute("username", username);
        model.addAttribute("profile", profileDto);
    }

    @GetMapping("edit-profile")
    public String editProfile(Principal principal, Model model) {
        String username = principal.getName();
        Profile profile = profileService.getByUsername(username);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setInfo(profile.getInfo());

        model.addAttribute("profile", profileDto);
        return "edit-profile";
    }

    @PostMapping("edit-profile")
    public String editProfile(Principal principal, @Valid ProfileDto profileDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.error(e.getDefaultMessage()));
            return "edit-profile";
        }

        String username = principal.getName();
        Profile profile = profileService.getByUsername(username);
        profile.setInfo(profileDto.getInfo());

        profileService.save(profile);
        return "redirect:user";
    }

    @GetMapping("profile-photo/{username}")
    public ResponseEntity<InputStreamResource> getProfilePhoto(@PathVariable String username) throws IOException {
        Profile profile = profileService.getByUsername(username);
        InputStream inputStream = null;

        if (profile == null || profile.getPhotoPath() == null || profile.getPhotoPath().equals("")) {
            Resource classPathResource = new ClassPathResource(photoPath);
            inputStream = classPathResource.getInputStream();
        } else {
            photoPath = profile.getPhotoPath();
            inputStream = Files.newInputStream(Paths.get(photoPath));
        }

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath))).body(resource);
    }

    @PostMapping("profile-photo")
    public String editPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.getByUsername(auth.getName());
        String prefix = System.currentTimeMillis() + profile.getId() + "-";

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        Path path = Paths.get(photoDirectory, prefix + originalName);

        try (InputStream inputStream = file.getInputStream()) {
            // Validate file type based on the extension
            if (!file.getContentType().startsWith("image/"))
                throw new InvalidImageException();
            BufferedImage image = ImageIO.read(inputStream);
            // Validate file type based on the actual content
            if (image == null)
                throw new InvalidImageException();

            BufferedImage thumbnail = Thumbnails.of(image).size(WIDTH, HEIGHT).asBufferedImage();
            OutputStream outputStream = Files.newOutputStream(path);
            ImageIO.write(thumbnail, extension, outputStream);
            outputStream.close();

            String oldPhoto = profile.getPhotoPath();
            profile.setPhotoPath(path.toString());
            profileService.save(profile);
            if (oldPhoto != null)
                Files.deleteIfExists(Paths.get(oldPhoto));
        }

        return "redirect:user";
    }
}
