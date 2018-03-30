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
import thai.model.PortalUser;
import thai.model.Profile;
import thai.services.PortalUserService;
import thai.services.ProfileService;

@Controller
public class ProfileController {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private ProfileService profileService;

    @Value("${photo.directory}")
    private String photoDirectory;

    // Change method names to match REST conventions
    // Merge the two showProfile methods
    @GetMapping("user")
    public String showProfile(Principal principal, Model model) {
        String username = principal.getName();
        PortalUser portalUser = portalUserService.getUserByUsername(username);
        viewProfile(portalUser, model);
        return "user";
    }

    @GetMapping("user/{id}")
    public String showProfile(@PathVariable long id, Model model) {
        PortalUser portalUser = portalUserService.getUserById(id);
        viewProfile(portalUser, model);
        return "user";
    }

    private void viewProfile(PortalUser portalUser, Model model) {
        Profile profile = profileService.getProfile(portalUser);

        if (profile == null) {
            profile = new Profile();
            profile.setPortalUser(portalUser);
            profileService.save(profile);
        }

        Profile viewProfile = new Profile();
        viewProfile.setInfo(profile.getInfo());

        model.addAttribute("memId", portalUser.getId());
        model.addAttribute("profile", viewProfile);
    }

    @GetMapping("edit-profile")
    public String editProfile(Principal principal, Model model) {
        // FIXME the following code snippet is the same as in showProfile
        String email = principal.getName();
        PortalUser portalUser = portalUserService.getUserByEmail(email);
        Profile profile = profileService.getProfile(portalUser);

        Profile viewProfile = new Profile();
        viewProfile.setInfo(profile.getInfo());

        model.addAttribute("profile", viewProfile);
        return "edit-profile";
    }

    @PostMapping("edit-profile")
    public String saveProfile(Principal principal, @Valid Profile viewProfile, BindingResult bindingResult) {
        String email = principal.getName();
        PortalUser portalUser = portalUserService.getUserByEmail(email);
        Profile profile = profileService.getProfile(portalUser);

        profile.setInfo(viewProfile.getInfo());

        if (!bindingResult.hasErrors()) {
            profileService.save(profile);
            return "redirect:profile";
        }
        return "edit-profile";
    }

    @GetMapping("profile-photo/{memId}")
    public ResponseEntity<InputStreamResource> viewProfilePhoto(@PathVariable long memId) throws IOException {
        String photoPath = "static/img/portal.png";
        PortalUser portalUser = portalUserService.getUserById(memId);
        Profile profile = profileService.getProfile(portalUser);

        InputStream is = null;
        try {
            if (profile == null || profile.getPhotoPath() == null || profile.getPhotoPath().equals("")) {
                Resource classPathResource = new ClassPathResource(photoPath);
                is = classPathResource.getInputStream();
                System.out.println(is == null);
            } else {
                photoPath = profile.getPhotoPath();
                is = Files.newInputStream(Paths.get(photoPath));
            }
        } catch (IOException e) {
            // TODO Log the message
        }

        InputStreamResource resource = new InputStreamResource(is);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath))).body(resource);
    }

    @PostMapping("upload-profile-photo")
    public String savePhoto(@RequestParam("file") MultipartFile file) {
        // FIXME Combile this method with saveProfile to validate photoPath
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PortalUser portalUser = portalUserService.getUserByEmail(auth.getName());
        // Handle the case when profile is null
        Profile profile = profileService.getProfile(portalUser);

        String prefix = System.currentTimeMillis() + profile.getId() + "-";
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        Path path = Paths.get(photoDirectory, prefix + originalName);
        try (InputStream is = file.getInputStream(); OutputStream os = Files.newOutputStream(path)) {
            // Validate file type based on the extension
            if (!file.getContentType().startsWith("image/"))
                throw new InvalidImageException();
            BufferedImage image = ImageIO.read(is);
            // Validate file type based on the actual content
            if (image == null)
                throw new InvalidImageException();
            BufferedImage thumbnail = Thumbnails.of(image).size(WIDTH, HEIGHT).asBufferedImage();
            ImageIO.write(thumbnail, extension, os);
            String oldPhoto = profile.getPhotoPath();
            profile.setPhotoPath(path.toString());
            profileService.save(profile);
            if (oldPhoto != null)
                Files.delete(Paths.get(oldPhoto));
        } catch (IOException e1) {
            // TODO Log the exception
            e1.printStackTrace();
        }
        return "redirect:user";
    }
}
