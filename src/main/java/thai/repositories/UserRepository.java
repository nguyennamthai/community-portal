package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
