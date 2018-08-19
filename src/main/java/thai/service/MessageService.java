package thai.service;

import org.springframework.data.domain.Page;
import thai.domain.Message;
import thai.domain.PortalUser;

import java.util.List;

public interface MessageService {
    void save(Message message);

    Message getLatest();

    Page<Message> getPage(int pageNumber);

    List<Message> getByUser(PortalUser user);

    Message get(Long id);

    void delete(Long id);
}
