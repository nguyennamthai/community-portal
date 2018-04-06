package thai.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private Long id;

    private String content;

    private Date created;

    private Date modified;

    private PortalUserDto user;
}
