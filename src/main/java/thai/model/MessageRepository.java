package thai.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Message findFirstByOrderByModifiedDesc();
}
