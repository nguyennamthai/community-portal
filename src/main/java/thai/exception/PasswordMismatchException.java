package thai.exception;

@SuppressWarnings("serial")
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("The given passwords do not match");
    }
}
