package ptit.edu.vn.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import ptit.edu.vn.entity.Category;
import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.model.CategoryModel;
import ptit.edu.vn.repository.BookRepository;
import ptit.edu.vn.repository.CategoryRepository;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("all")
    @Transactional
    public ResponseEntity<List<CategoryModel>> getAll(
        @RequestParam(required = false) Integer page) {
            if (page == null) {
                page = 1;
            }
        return ResponseEntity.ok(categoryRepository.findAll(
            PageRequest.of(page-1, 10)).getContent().stream()
            .map(CategoryModel::convert)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<CategoryModel> getById(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không thấy thể loại này"));
        return ResponseEntity.ok(CategoryModel.convert(category));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CategoryModel> create(@RequestBody CategoryModel categoryModel) {
        Category category = CategoryModel.convert(categoryModel);
        if (categoryRepository.existsByName(category.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST,
             "Thể loại này đã tồn tại",
             "Thử thêm thể loại khác");
        }
        category.setId(null);
        categoryRepository.save(category);
        return ResponseEntity.ok(CategoryModel.convert(category));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không thấy thể loại này"));
        bookRepository.deleteAll(category.getBooks());
        categoryRepository.delete(category);
        return ResponseEntity.ok("Xoá thành công thể loại " + category.getName());
    }
}
