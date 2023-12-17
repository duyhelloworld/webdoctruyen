package ptit.edu.vn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import ptit.edu.vn.entity.Chapter;
import ptit.edu.vn.entity.Comment;
import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.model.CommentModel;
import ptit.edu.vn.repository.ChapterRepository;
import ptit.edu.vn.repository.CommentRepository;
import ptit.edu.vn.security.AppUserDetails;
import ptit.edu.vn.security.AppUserDetailsService;
import ptit.edu.vn.security.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserDetailsService appUserDetailsService;
    
    private final int PAGE_SIZE = 10;

    @GetMapping("all")
    public List<CommentModel> getAll(@RequestParam(required = false) Integer page) {
        if (page == null) {
            page = 0;
        }
        List<Comment> comments = commentRepository.findAll(
            PageRequest.of(page, PAGE_SIZE).withPage(page))
            .getContent();
        return comments.stream()
            .map(CommentModel::convert)
            .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public CommentModel getById(@PathVariable Integer id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận mã " + id));
        return CommentModel.convert(comment);
    }

    @GetMapping("chapter/{chapterId}")
    public List<CommentModel> getByChapterId(
        @PathVariable Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> 
            new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + chapterId));
        return commentRepository.findByChapter(chapter).stream()
            .map(CommentModel::convert)
            .collect(Collectors.toList());
    }
    
    @PostMapping
    public ResponseEntity<CommentModel> create(@RequestBody CommentModel commentModel, HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để thực hiện hành động này!");
        }
        Integer uid = jwtService.getUserIdFromToken(token);
        if (uid == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Lỗi xác thực", "Vui lòng đăng nhập để kiểm tra lại");
        }
        if (commentModel.getChapterId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Chương truyện không được để trống");
        }
        Comment comment = new Comment();
        Chapter chapter = chapterRepository.findById(commentModel.getChapterId())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + commentModel.getChapterId()));
        comment.setChapter(chapter);
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(false);
        comment.setUser(appUserDetailsService.loadUser(uid));
        commentRepository.save(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }

    @PutMapping("{id}")
    public ResponseEntity<CommentModel> update(
        @RequestBody CommentModel commentModel,
        @PathVariable Integer id) 
    {   
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUserDetails appUserDetails = (AppUserDetails) principal;
        Comment comment = commentRepository.findById(commentModel.getId()).orElse(null);
        if (comment == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        }
        if (comment.getUser().getId() != appUserDetails.getId()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền chỉnh sửa bình luận này");
        }
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(true);
        commentRepository.save(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommentModel> delete(@RequestParam Integer id) {
        AppUserDetails appUserDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        }
        if (comment.getUser().getId() != appUserDetails.getId()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa bình luận này");
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }
}