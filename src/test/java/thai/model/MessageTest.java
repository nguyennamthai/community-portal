package thai.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class MessageTest {
    @Test
    public void testMessageGettersAndSetters() {
        Long id = 10L;
        String content = "A simple message";
        Date modified = Calendar.getInstance().getTime();
        PortalUser user = new PortalUser();

        Message message = new Message();
        message.setId(id);
        message.setContent(content);
        message.setModified(modified);
        message.setUser(user);

        assertThat(message)
          .extracting(Message::getId, Message::getContent, Message::getModified, Message::getUser)
          .containsExactly(id, content, modified, user);
    }
}
