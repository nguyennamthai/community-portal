package thai.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import thai.domain.Message;
import thai.domain.PortalUser;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testFindById() {
        Message input = new Message();
        messageRepository.save(input);
        assertThat(input.getId()).isNotNull();
        assertThat(input.getModified()).isNotNull();

        Message output = messageRepository.findById(input.getId()).get();
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void testFindByUser() {
        PortalUser user = new PortalUser();
        user.setUsername("johndoe");
        user.setEmail("johndoe@company.com");
        portalUserRepository.save(user);

        Message input = new Message();
        input.setUser(user);
        messageRepository.save(input);

        Message output = messageRepository.findByUserOrderByModifiedDesc(user).get(0);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void testFindAll() {
        Message input1 = new Message();
        messageRepository.save(input1);
        Message input2 = new Message();
        messageRepository.save(input2);

        Pageable pageable = PageRequest.of(1, 1, DESC, "modified");
        Page<Message> page = messageRepository.findAll(pageable);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(1);

        Message output = page.getContent().get(0);
        assertThat(output).isEqualTo(input1);
    }

    @Test
    public void testFindFirstByOrderByModifiedDesc() {
        Message input1 = new Message();
        messageRepository.save(input1);
        Message input2 = new Message();
        messageRepository.save(input2);

        Message output = messageRepository.findFirstByOrderByModifiedDesc();
        assertThat(output).isEqualTo(input2);
    }

    @Test
    public void testDeleteById() {
        Message input = new Message();
        messageRepository.save(input);

        Long id = input.getId();
        assertThat(id).isNotNull();

        messageRepository.deleteById(id);
        Optional<Message> optional = messageRepository.findById(id);
        assertThat(optional.isPresent()).isFalse();
    }
}
