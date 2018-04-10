package thai.exceptions;

@SuppressWarnings("serial")
public class InvalidImageException extends RuntimeException {
    public InvalidImageException() {
        super("The uploaded file must be one of these types: BMP, GIF, JPG and PNG");
    }
}
