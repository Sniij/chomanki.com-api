package blog.sniij.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Comment")
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    private String commentId;
    private String slug;
    private String content;
    private String createdAt;

    private User user;

    private Comment parentComment;

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Comment(String commentId, String slug, String content, String createdAt, User user, Comment parentComment) {
        this.commentId = commentId;
        this.slug = slug;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.parentComment = parentComment;
    }
}
