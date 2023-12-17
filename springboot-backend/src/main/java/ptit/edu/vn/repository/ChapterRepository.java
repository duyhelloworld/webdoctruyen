package ptit.edu.vn.repository;

import org.springframework.stereotype.Repository;

import ptit.edu.vn.entity.Chapter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    @Query("SELECT c FROM Chapter c WHERE c.book.id = ?1 AND c.Id = ?2")
    public Chapter findByBookIdAndNumber(Integer bookId, Integer chapterId);

    @Query("SELECT c FROM Chapter c WHERE c.book.id = ?1 AND c.folderName = ?2")
    public Optional<Chapter> isExistChapter(Integer bookId, String folderName);

    @Query("SELECT MAX(CAST(SUBSTRING(c.folderName, 9) AS integer)) FROM Chapter c WHERE c.book.id = ?1")
    public Integer getMaxChapterNumber(Integer bookId);
}
