package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByEmail(String email);
}
