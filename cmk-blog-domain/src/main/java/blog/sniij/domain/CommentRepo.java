package blog.sniij.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

    @Query("{'slug':0}")
    List<Comment> findAll(String slug);

    Page<Comment> findBySlug(String slug, Pageable pageable);

    long count();
}
