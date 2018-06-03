package thai.service.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class MessageDto {
    private Long id;

    @Size(min = 5, max = 255, message = "Enter between {min} and {max} characters")
    private String content;

    private Date modified;

    private boolean editable;
}
