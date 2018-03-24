package thai.repositories;

import org.springframework.data.repository.CrudRepository;

import thai.model.Member;
import thai.model.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByMember(Member member);
}
