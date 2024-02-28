package blog.sniij.api.dto;

import blog.sniij.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Post{
        private String slug;
        private String content;
    }

    @NoArgsConstructor
    @Getter
    public static class Response{
        private String id;
        private String slug;
        private String content;

        private String createdAt;

        private String nickname;

        private String imgUrl;

        private String userId;


        public Response(Comment comment) {
            this.userId = comment.getUser().getUserId();
            this.id = comment.getCommentId();
            this.slug = comment.getSlug();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.imgUrl = comment.getUser().getImgUrl();
            this.nickname = comment.getUser().getNickname();
        }

        public List<CommentDto.Response> responseList(List<Comment> comments) {
            if(comments.isEmpty()) return null;

            return comments.stream().map(Response::new).collect(Collectors.toList());
        }
    }


}
