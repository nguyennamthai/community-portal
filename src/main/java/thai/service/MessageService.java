package thai.service;

import org.springframework.data.domain.Page;
import thai.domain.Message;
import thai.domain.PortalUser;

import java.util.List;

public interface MessageService {
    public void save(Message message);

    public Message getLatest();

    public Page<Message> getPage(int pageNumber);

    public List<Message> getByUser(PortalUser user);

    public Message get(Long id);

    public void delete(Long id);
}
