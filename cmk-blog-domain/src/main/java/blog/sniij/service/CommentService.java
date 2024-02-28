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



    /*
    public List<Comment> init() {

        User user = User.builder()
                .userId("65a58ee388122936483937fa")
                .nickname("key cho")
                .imgUrl("https://lh3.googleusercontent.com/a/ACg8ocIeihAFrs32Fem8f5wntsd0a8jzvWt2Tk_pOynLqmDQ=s96-c")
                .email("dokeycho@gmail.com")
                .provider("google")
                .build();

        Comment comment1 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 1")
                .user(user)
                .build();
        Comment comment2 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 2")
                .user(user)
                .build();
        Comment comment3 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 3")
                .user(user)
                .build();
        Comment comment4 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 4")
                .user(user)
                .build();
        Comment comment5 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 5")
                .user(user)
                .build();
        Comment comment6 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 6")
                .user(user)
                .build();
        Comment comment7 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 7")
                .user(user)
                .build();
        Comment comment8 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 8")
                .user(user)
                .build();
        Comment comment9 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 9")
                .user(user)
                .build();
        Comment comment10 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 10")
                .user(user)
                .build();
        Comment comment11 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 11")
                .user(user)
                .build();
        Comment comment12 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 12")
                .user(user)
                .build();
        Comment comment13 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 13")
                .user(user)
                .build();
        Comment comment14 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 14")
                .user(user)
                .build();
        Comment comment15 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 15")
                .user(user)
                .build();
        Comment comment16 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 16")
                .user(user)
                .build();
        Comment comment17 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 17")
                .user(user)
                .build();
        Comment comment18 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 18")
                .user(user)
                .build();
        Comment comment19 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 19")
                .user(user)
                .build();
        Comment comment20 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 20")
                .user(user)
                .build();
        Comment comment21 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 21")
                .user(user)
                .build();
        Comment comment22 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 22")
                .user(user)
                .build();
        Comment comment23 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 23")
                .user(user)
                .build();
        Comment comment24 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 24")
                .user(user)
                .build();
        Comment comment25 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 25")
                .user(user)
                .build();
        Comment comment26 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 26")
                .user(user)
                .build();
        Comment comment27 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 27")
                .user(user)
                .build();
        Comment comment28 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 28")
                .user(user)
                .build();
        Comment comment29 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 29")
                .user(user)
                .build();
        Comment comment30 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 30")
                .user(user)
                .build();
        Comment comment31 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 31")
                .user(user)
                .build();
        Comment comment32 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 32")
                .user(user)
                .build();
        Comment comment33 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 33")
                .user(user)
                .build();
        Comment comment34 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 34")
                .user(user)
                .build();
        Comment comment35 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 35")
                .user(user)
                .build();
        Comment comment36 = Comment.builder()
                .createdAt(String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .slug("chomanki.com")
                .content("Mock comment 36")
                .user(user)
                .build();
        List<Comment> comments = new ArrayList<>();
        comments.add(commentRepo.save(comment1));
        comments.add(commentRepo.save(comment2));
        comments.add(commentRepo.save(comment3));
        comments.add(commentRepo.save(comment4));
        comments.add(commentRepo.save(comment5));
        comments.add(commentRepo.save(comment6));        comments.add(commentRepo.save(comment7));
        comments.add(commentRepo.save(comment8));
        comments.add(commentRepo.save(comment9));        comments.add(commentRepo.save(comment10));
        comments.add(commentRepo.save(comment11));
        comments.add(commentRepo.save(comment12));        comments.add(commentRepo.save(comment13));
        comments.add(commentRepo.save(comment14));
        comments.add(commentRepo.save(comment15));        comments.add(commentRepo.save(comment16));
        comments.add(commentRepo.save(comment17));
        comments.add(commentRepo.save(comment18));        comments.add(commentRepo.save(comment19));
        comments.add(commentRepo.save(comment20));
        comments.add(commentRepo.save(comment21));        comments.add(commentRepo.save(comment22));
        comments.add(commentRepo.save(comment23));
        comments.add(commentRepo.save(comment24));        comments.add(commentRepo.save(comment25));
        comments.add(commentRepo.save(comment26));
        comments.add(commentRepo.save(comment27));        comments.add(commentRepo.save(comment28));
        comments.add(commentRepo.save(comment29));
        comments.add(commentRepo.save(comment30));        comments.add(commentRepo.save(comment31));
        comments.add(commentRepo.save(comment32));
        comments.add(commentRepo.save(comment33));        comments.add(commentRepo.save(comment34));
        comments.add(commentRepo.save(comment35));
        comments.add(commentRepo.save(comment36));

        return comments;
    }

    public List<Comment> get() {
        return commentRepo.findAll();
    }
    public void delete() {
        commentRepo.deleteAll();
    }
*/


}
