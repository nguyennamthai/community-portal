package thai.interceptors;

import static org.springframework.http.HttpStatus.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import thai.exceptions.PasswordMismatchException;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String globalHandler(Model model, HttpServletRequest request, Exception exception) {
        model.addAttribute("url", request.getRequestURL());
        model.addAttribute("exception", exception.getMessage());
        log.error("Exception thrown:", exception);
        return "exception";
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public String duplicateUserHandler(Model model, DataIntegrityViolationException exception) {
        model.addAttribute("exception", "The username or email address has already been used");
        log.error("Exception thrown:", exception);
        return "exception";
    }

    @ExceptionHandler
    @ResponseStatus(PRECONDITION_FAILED)
    public String passwordMismatch(Model model, PasswordMismatchException exception) {
        model.addAttribute("exception", "The two provided passwords do not match");
        log.error("Exception thrown:", exception);
        return "exception";
    }
}
