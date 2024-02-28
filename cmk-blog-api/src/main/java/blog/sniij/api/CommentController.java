package blog.sniij.api;


import blog.sniij.api.dto.CommentDto;
import blog.sniij.domain.Comment;
import blog.sniij.dto.MultiResponseDto;
import blog.sniij.dto.SingleResponseDto;
import blog.sniij.service.CommentService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

/*
    @GetMapping("/init")
    public ResponseEntity init() {

        List<Comment> comments = commentService.init();
        List<CommentDto.Response> response = new CommentDto.Response().responseList(comments);
        return new ResponseEntity<> (response, HttpStatus.OK);
    }
    @GetMapping("/get")
    public ResponseEntity get() {

        List<Comment> comments = commentService.get();
        List<CommentDto.Response> response = new CommentDto.Response().responseList(comments);
        return new ResponseEntity<> (response, HttpStatus.OK);
    }
    @GetMapping("/delete")
    public ResponseEntity delete() {

        commentService.delete();
        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }*/


    @PostMapping
    public ResponseEntity<SingleResponseDto<CommentDto.Response>> postComment(
            @RequestParam("userId") String userId,
            @RequestBody CommentDto.Post request) {

        Comment comment = commentService.createComment(Comment.builder()
                .slug(request.getSlug())
                .content(request.getContent())
                .build(), userId);
        CommentDto.Response response = new CommentDto.Response(comment);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteComment(
            @RequestParam("userId") String userId,
            @RequestParam String commentId
    ){
        commentService.deleteComment(commentId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<MultiResponseDto<CommentDto.Response>> getComments(
            @RequestParam String slug,
            @Positive @RequestParam(required = false, defaultValue = "1") int page,
            @Positive @RequestParam(required = false, defaultValue = "10") int size) {

        // TODO: Consider adding "isMine" in response dto
        Page<Comment> pageComments = commentService.findComments(page-1, size, slug);
        List<Comment> comments = pageComments.getContent();
        List<CommentDto.Response> response = new CommentDto.Response().responseList(comments);

        return new ResponseEntity<>(new MultiResponseDto<>( response, pageComments), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<SingleResponseDto<CommentDto.Response>> getComment(
            @PathVariable String commentId
    ) {

        // TODO: Consider adding "isMine" in response dto
        Comment comment = commentService.findComment(commentId);
        CommentDto.Response response = new CommentDto.Response(comment);

        return new ResponseEntity<>(new SingleResponseDto<>( response ), HttpStatus.OK);
    }



}
