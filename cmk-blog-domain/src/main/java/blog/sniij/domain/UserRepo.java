package blog.sniij.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface UserRepo extends MongoRepository<User, String> {

    @Query("{email:'?0'}")
    Optional<User> findByEmail(String email);

/*    @Query("{id:'?0'}")
    Optional<User> findById(String id);*/
}
