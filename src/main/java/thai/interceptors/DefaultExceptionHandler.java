package thai.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler
    public String globalHandler(Model model, HttpServletRequest request, Exception exception) {
        model.addAttribute("url", request.getRequestURL());
        model.addAttribute("exception", exception.getMessage());
        // TODO Send an email to the admin
        log.error("Exception thrown:", exception);
        return "exception";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String duplicateUserHandler(Model model) {
        model.addAttribute("exception", "Username already existed");
        return "exception";
    }
}
