package thai.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class MessageTest {
    @Test
    public void testMessageGettersAndSetters() {
        Calendar createdCal = Calendar.getInstance();
        createdCal.add(Calendar.DATE, -1);
        Date created = createdCal.getTime();
        Date modified = Calendar.getInstance().getTime();
        PortalUser user = new PortalUser();

        Message message = new Message();
        message.setId(10L);
        message.setContent("A simple message");
        message.setCreated(created);
        message.setModified(modified);
        message.setUser(user);

        assertThat(message)
          .extracting(Message::getId, Message::getContent, Message::getCreated, Message::getModified, Message::getUser)
          .containsExactly(10L, "A simple message", created, modified, user);
    }
}
