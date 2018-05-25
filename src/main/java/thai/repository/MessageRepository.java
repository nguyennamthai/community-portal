package thai.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import thai.domain.Message;
import thai.domain.PortalUser;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Message findFirstByOrderByModifiedDesc();

    List<Message> findByUser(PortalUser user);
}
