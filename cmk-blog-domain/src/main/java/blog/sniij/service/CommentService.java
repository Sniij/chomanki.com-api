package blog.sniij.service;

import blog.sniij.domain.Comment;
import blog.sniij.domain.CommentRepo;
import blog.sniij.domain.User;
import blog.sniij.exception.BusinessLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepo commentRepo, UserService userService) {
        this.commentRepo = commentRepo;
        this.userService = userService;
    }

    public Comment createComment(Comment comment, String userId){

        User user =  userService.findUserById(userId);

        Comment createdComment = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug(comment.getSlug())
                .content(comment.getContent())
                .user(user)
                .build();

        return commentRepo.save(createdComment);
    }
    public Comment findComment(String commentId) {
        return findVerifiedComment(commentId);
    }

    public Page<Comment> findComments(int page, int size, String slug) {

        var result = Optional
                .ofNullable(commentRepo.findBySlug(slug, PageRequest.of(page, size, Sort.by("createdAt").ascending())))
                .orElseThrow( ()->
                        new BusinessLogicException(HttpStatus.NO_CONTENT,"Can not find comments")
        );

        return result;
    }


    public void deleteComment(String commentId, String userId) {

        User user = userService.findUserById(userId);
        Comment comment = findVerifiedComment(commentId);

        if(user.getUserId().equals(comment.getUser().getUserId())){
            commentRepo.deleteById(commentId);
        }else{
            throw new BusinessLogicException(HttpStatus.FORBIDDEN, "Requested user does not write this comment");
        }

    }

    private Comment findVerifiedComment(String commentId) {
        var comment = commentRepo.findById(commentId);

        return comment.orElseThrow(()->
            new BusinessLogicException(HttpStatus.NOT_FOUND, "Comment not found")
        );
    }



}
