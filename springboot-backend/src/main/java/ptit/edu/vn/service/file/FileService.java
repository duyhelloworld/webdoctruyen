package ptit.edu.vn.service.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ptit.edu.vn.exception.AppException;

@Service
public class FileService {

    public final String ROOT_DIR 
        = System.getProperty("user.dir") + "/src/main/resources/static/";
    public final String BOOK_DIR  = ROOT_DIR + "mangas";
    public final String COVERIMAGE_DIR = ROOT_DIR + "coverimages";
    public final String AVATAR_DIR = ROOT_DIR + "avatars";

    public boolean createNewBook(String folderName) {
        Path path = Path.of(BOOK_DIR, folderName);
        if (Files.exists(path))
            return false;
        try {
            Files.createDirectory(path);
            return true;
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tạo!");
        }
    }

    public void saveCoverImage(MultipartFile file){
        try {
            Path path = Path.of(COVERIMAGE_DIR, file.getOriginalFilename());
            if (Files.exists(path)) {
                throw new FileAlreadyExistsException(path.toString());
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new AppException(HttpStatus.BAD_REQUEST, 
                    "Ảnh này đã tồn tại",
                    "Hãy thử lại với tên file khác");
            }
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi lưu file");
        }
    } 

    public void saveAvatar(MultipartFile file) {
        try {
            // Lưu file vào thư mục avatar bằng stream 
            Path path = Path.of(AVATAR_DIR);
            if (FileValidatorService.ValidFileAvatar(file)) {
                if (Files.exists(path)) {
                    throw new FileAlreadyExistsException(path.toString());
                }
                Files.copy(file.getInputStream(), path);
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new AppException(HttpStatus.BAD_REQUEST, 
                    "Ảnh này đã tồn tại",
                    "Hãy thử lại với tên file khác");
            }
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi lưu file");
        }
    }

    public int saveChapter(List<MultipartFile> files, String bookName, String folderName) throws IOException {
        if (bookName.contains(" ")) {
            bookName = bookName.replace(" ", "_");
        }
        if (folderName.contains(" ")) {
            folderName = folderName.replace(" ", "_");
        }
        Path dir = Path.of(BOOK_DIR, bookName, folderName);
        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }
        // Lưu lại những file hợp lệ. nếu file ko hợp lệ thì skip
        int i = 0; 
        for (MultipartFile file : files) {
            if (FileValidatorService.ValidFileChapter(file)) {
                String newFileName = String.format("%02d.jpg", ++i);
                if (Files.exists(Path.of(dir.toString(), newFileName))) {
                    continue;
                }
                Files.copy(file.getInputStream(), 
                    Path.of(dir.toString(), newFileName), 
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return i;
    } 

    // Luôn chắc kèo file tồn tại
    public Resource getCoverImage(String filename){
        File file = new File(Path.of(COVERIMAGE_DIR, filename).toString());
        return new FileSystemResource(file.getPath());
    }

    public Integer getNumberImageOfChapter(String bookName, String chapterName) {
        if (bookName.contains(" ")) {
            bookName = bookName.replace(" ", "_");
        }
        if (chapterName.contains(" ")) {
            chapterName = chapterName.replace(" ", "_");
        }
        Path path = Path.of(BOOK_DIR, bookName, chapterName);
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory() || file.listFiles() == null)
            return 0;
        return file.listFiles().length;
    }

    public Resource getChapter(String bookName, String chapterName, Integer fileId){
        String fileName = String.format("%02d.jpg", fileId);
        String chapterFolder = Path.of(BOOK_DIR, bookName, chapterName, fileName).toString();
        File file = new File(chapterFolder);
        if (!file.exists()) 
            return null;
        return new FileSystemResource(chapterFolder);
    }

    public Resource getAvatar(String filename) {
        File file = new File(Path.of(AVATAR_DIR, filename).toString());
        if (!file.exists()) 
            return null;
        return new FileSystemResource(file.getPath());
    }

    public void deleteAvatar(String filename) {
        File file = new File(Path.of(AVATAR_DIR, filename).toString());
        if (!file.exists()) 
            return;
        boolean isSucceed = file.delete();
        if (!isSucceed) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi xóa file");
        }
    }

    public void deleteCoverImage(String filename) {
        File file = new File(Path.of(COVERIMAGE_DIR, filename).toString());
        if (!file.exists() || filename.equals("default-coverimage.png")) {
            System.out.println("Sách này ko có cover image");
            return;
        }
        boolean isSucceed = file.delete();
        if (!isSucceed) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi xóa file ảnh bìa");
        }
        System.out.println("Xoá thành công ảnh bìa : " + filename);
    }

    public void deleteChapter(String bookName, String chapterName) {
        File file = new File(Path.of(BOOK_DIR, bookName, chapterName).toString());
        if (!file.exists()) {
            System.out.println("Sách này ko có chapter");
            return;
        }
        try {
            for (File fileIn : file.listFiles()) {
                Files.deleteIfExists(fileIn.toPath());                
            }
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi xóa file chapter");
        }
    }

    public void deleteBook(String bookName) {
        File folder = new File(Path.of(BOOK_DIR, bookName).toString());
        if (!folder.exists()) 
            return;
        try {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    for (File fileIn : file.listFiles()) {
                        Files.deleteIfExists(fileIn.toPath());
                    }
                }
                Files.deleteIfExists(file.toPath());
            }
            Files.deleteIfExists(folder.toPath());
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi xóa file");
        }
    }
}
