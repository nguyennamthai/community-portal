package thai.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import thai.model.Message;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Message findFirstByOrderByModifiedDesc();
}
