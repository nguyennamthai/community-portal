package thai.services;

import static org.springframework.data.domain.Sort.Direction.DESC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import thai.model.Message;
import thai.model.MessageRepository;

@Service
public class MessageService {
    private final static int PAGE_SIZE = 5;
    @Autowired
    private MessageRepository messageRepository;

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

    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    public Message get(Long id) {
        return messageRepository.findById(id).orElse(getLatest());
    }
}
