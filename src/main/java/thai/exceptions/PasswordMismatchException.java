package thai.exceptions;

@SuppressWarnings("serial")
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Password does not match");
    }
}
