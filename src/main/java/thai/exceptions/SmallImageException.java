package thai.exceptions;

public class SmallImageException extends RuntimeException {
    public SmallImageException() {
        super("The image is too small");
    }
}
