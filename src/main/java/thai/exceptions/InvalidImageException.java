package thai.exceptions;

@SuppressWarnings("serial")
public class InvalidImageException extends RuntimeException {
    public InvalidImageException() {
        super("The uploaded file is not an image; it must has one of these types: BMP, GIF, JPG and PNG");
    }
}
