package thai.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import thai.model.Message;
import thai.model.PortalUser;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Message findFirstByOrderByModifiedDesc();

    List<Message> findByUser(PortalUser user);
}
