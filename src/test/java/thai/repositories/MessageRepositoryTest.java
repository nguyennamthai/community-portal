package thai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import thai.model.Message;
import thai.repositories.MessageRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void testSave() {
        Message status = new Message("A message test");
        messageRepository.save(status);
        assertNotNull(status.getId());
        assertNotNull(status.getModified());

        Message retrieved = messageRepository.findById(status.getId()).get();
        assertEquals(status, retrieved);
    }
}
