package thai.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import static java.net.URLConnection.guessContentTypeFromName;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.http.ResponseEntity.ok;
import static org.thymeleaf.util.StringUtils.isEmpty;
import static thai.service.dto.mapper.ProfileMapper.convertProfileToProfileDto;

@Slf4j
@Controller
public class ProfileController {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final String DEFAULT_IMAGE = "static/img/portal.png";
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Value("${image.directory:}")
    private String imageDirectory;

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
        ProfileDto profileDto = convertProfileToProfileDto(profile);
        model.addAttribute("username", username);
        model.addAttribute("profile", profileDto);
    }

    @GetMapping("edit-profile")
    public String editProfile(Principal principal, Model model) {
        String username = principal.getName();
        Profile profile = profileService.getByUsername(username);
        ProfileDto profileDto = convertProfileToProfileDto(profile);
        model.addAttribute("profile", profileDto);
        return "edit-profile";
    }

    @PostMapping("edit-profile")
    public String editProfile(Principal principal, @Valid @ModelAttribute("profile") ProfileDto profileDto, BindingResult bindingResult) {
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

    @GetMapping("profile-image/{username}")
    public ResponseEntity<InputStreamResource> getProfileImage(@PathVariable String username) throws IOException {
        Profile profile = profileService.getByUsername(username);
        InputStream inputStream = openInputStreamOnImagePath(profile);
        InputStreamResource resource = new InputStreamResource(inputStream);
        String imagePath = isDefaultImage(profile) ? DEFAULT_IMAGE : profile.getImagePath();
        return ok().contentType(parseMediaType(guessContentTypeFromName(imagePath))).body(resource);
    }

    private InputStream openInputStreamOnImagePath(Profile profile) throws IOException {
        if (isDefaultImage(profile)) {
            Resource classPathResource = new ClassPathResource(DEFAULT_IMAGE);
            return classPathResource.getInputStream();
        }
        String imagePath = profile.getImagePath();
        return Files.newInputStream(Paths.get(imagePath));
    }

    private boolean isDefaultImage(Profile profile) {
        return profile == null || isEmpty(profile.getImagePath());
    }

    @PostMapping("profile-image")
    public String editImage(@RequestParam("file") MultipartFile file) throws IOException {
        validateImageFileExtension(file);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.getByUsername(auth.getName());

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            validateImageBinaryContent(image);

            BufferedImage thumbnail = Thumbnails.of(image).size(WIDTH, HEIGHT).asBufferedImage();
            Path path = buildLocalImagePath(profile, file);
            saveImageThumbnail(path, thumbnail);
            profile.setImagePath(path.toString());
        }

        profileService.save(profile);
        String imagePath = profile.getImagePath();
        if (imagePath != null) {
            Files.deleteIfExists(Paths.get(imagePath));
        }
        return "redirect:user";
    }

    private void validateImageFileExtension(MultipartFile file) {
        if (!file.getContentType().startsWith("image/")) {
            throw new InvalidImageException();
        }
    }

    private void validateImageBinaryContent(BufferedImage image) {
        if (image == null) {
            throw new InvalidImageException();
        }
    }

    private void saveImageThumbnail(Path path, BufferedImage thumbnail) throws IOException {
        String extension = extractFileExtension(path.getFileName().toString());
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            ImageIO.write(thumbnail, extension, outputStream);
        }
    }

    private Path buildLocalImagePath(Profile profile, MultipartFile file) {
        String prefix = System.currentTimeMillis() + profile.getId() + "-";
        String originalName = file.getOriginalFilename();
        return Paths.get(imageDirectory, prefix + originalName);
    }

    private String extractFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
