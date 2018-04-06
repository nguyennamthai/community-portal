package thai.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import thai.model.PortalUser;

@Getter
@Setter
public class Message {
    private Long id;

    private String content;

    private Date created;

    private Date modified;

    private PortalUser user;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public Message(String text, Date modified) {
        this.content = text;
        this.modified = modified;
    }
}
