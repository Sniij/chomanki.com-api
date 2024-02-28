package blog.sniij.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {

    boolean existsByRefreshToken(String refreshToken);


}
