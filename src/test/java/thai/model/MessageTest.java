package thai.model;

import java.util.Calendar;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MessageTest {
    @Test
    public void testMessageGetters() {
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

        Assertions.assertThat(message)
                  .extracting(Message::getId, Message::getContent, Message::getCreated, Message::getModified, Message::getUser)
                  .containsExactly(10L, "A simple message", created, modified, user);
    }
}
