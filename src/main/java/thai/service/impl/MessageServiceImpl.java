package thai.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thai.domain.Message;
import thai.domain.PortalUser;
import thai.repository.MessageRepository;
import thai.service.MessageService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class MessageServiceImpl implements MessageService {
    private final static int PAGE_SIZE = 5;

    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public Message getLatest() {
        return messageRepository.findFirstByOrderByModifiedDesc();
    }

    public Page<Message> getPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, DESC, "modified");
        return messageRepository.findAll(pageable);
    }

    public List<Message> getByUser(PortalUser user) {
        return messageRepository.findByUserOrderByModifiedDesc(user);
    }

    public Message get(Long id) {
        return messageRepository.findById(id).orElse(getLatest());
    }

    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}
