package ptit.edu.vn.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ptit.edu.vn.entity.Comment;

@Data
public class CommentModel {
    private Integer id;
    
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentAt;

    private Boolean isEdited;

    private String userName;
    private String userAvatar;

    private Integer chapterId;

    public static CommentModel convert(Comment comment) {
        CommentModel commentModel = new CommentModel();
        commentModel.setId(comment.getId());
        commentModel.setContent(comment.getContent());
        commentModel.setCommentAt(comment.getCommentAt());
        commentModel.setIsEdited(comment.getIsEdited());
        commentModel.setUserName(comment.getUser().getFullname());
        commentModel.setUserAvatar(comment.getUser().getAvatar());
        commentModel.setChapterId(comment.getChapter().getId());
        return commentModel;
    }

    public static Comment convert(CommentModel commentModel) {
        Comment comment = new Comment();
        comment.setId(commentModel.getId());
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(commentModel.getCommentAt());
        comment.setIsEdited(commentModel.getIsEdited());
        return comment;
    }
}
