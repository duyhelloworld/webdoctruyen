package com.duyhelloworld.service.impl.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public void createNewBook(String folderName) {
        Path path = Path.of(AppConstant.BOOK_DIR, folderName);
        if (Files.exists(path))
            throw new AppException(HttpStatus.BAD_REQUEST, "Sách này đã tồn tại");
        try {
            Files.createDirectory(path);
            return;
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tạo!");
        }
    }

    private void saveImage(MultipartFile file, String parentDirectory, String defaultFileName) {
        try {
            Path path = Path.of(parentDirectory, file.getOriginalFilename());
            if (Files.exists(path)) {
                throw new FileAlreadyExistsException(path.toString());
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new AppException(HttpStatus.BAD_REQUEST, 
                    "Ảnh này đã tồn tại. Hãy thử đổi tên file");
            }
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi lưu file");
        }
    }

    private void normalize(String... fileInputName) {
        for (String fileName : fileInputName) {
            fileName = fileName.replace(" ", "_");
        }
    }

    private void delete(String parentDirectory, String filename) {
        File file = new File(Path.of(parentDirectory, filename).toString());
        if (!file.exists()) 
            return;
        boolean isSucceed = file.delete();
        if (!isSucceed) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi khi xóa file");
        }
    }
    
    private Resource getResource(String parentDirectory, String filename) {
        if (filename.matches("^(http|https)://.*$")) {
            try {
                return new UrlResource(filename);
            } catch (MalformedURLException e) {
                throw new AppException(HttpStatus.BAD_REQUEST, 
                    "Đường dẫn ảnh không hợp lệ");
            }
        }
        File file = new File(Path.of(parentDirectory, filename).toString());
        if (!file.exists()) 
            return null;
        return new FileSystemResource(file.getPath());
    }

    @Override
    public void saveCoverImage(MultipartFile file){
        saveImage(file, AppConstant.COVERIMAGE_DIR, AppConstant.DEFAULT_COVERIMAGE);
    } 

    @Override
    public String saveAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return AppConstant.DEFAULT_AVATAR;
        }
        saveImage(file, AppConstant.AVATAR_DIR, AppConstant.DEFAULT_AVATAR);
        return file.getOriginalFilename();
    }

    @Override
    public void saveChapter(List<MultipartFile> files, String bookName, String chapterName) {
        normalize(bookName, chapterName);
        Path dir = Path.of(AppConstant.BOOK_DIR, bookName, chapterName);
        if (!Files.exists(dir)) {
            throw new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy chapter này");
        }
        // Lưu lại những file hợp lệ. nếu file ko hợp lệ thì skip
        int i = 0; 
        for (MultipartFile file : files) {
            if (validFile(file, "jpg")) {
                String newFileName = "%02d.jpg".formatted(++i);
                if (Files.exists(Path.of(dir.toString(), newFileName))) {
                    continue;
                }
                try {
                    Files.copy(file.getInputStream(), 
                        Path.of(dir.toString(), newFileName), 
                        StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "Lỗi khi lưu file");
                }
            }
        }
    } 

    // Luôn chắc kèo file tồn tại
    public Resource getCoverImage(String filename){
        return getResource(AppConstant.COVERIMAGE_DIR, filename);
    }

    public Integer getNumberImageOfChapter(String bookName, String chapterName) {
        normalize(bookName, chapterName);
        Path path = Path.of(AppConstant.BOOK_DIR, bookName, chapterName);
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory() || file.listFiles() == null)
            return 0;
        return file.listFiles().length;
    }

    public Resource getChapter(String bookName, String chapterName, Integer fileId){
        return getResource(Path.of(AppConstant.BOOK_DIR, bookName, chapterName).toString(),
        "%02d.jpg".formatted(fileId));
    }

    public Resource getAvatar(String filename) {
        return getResource(AppConstant.AVATAR_DIR, filename);
    }

    public void deleteAvatar(String filename) {
        if (filename.equals(AppConstant.DEFAULT_AVATAR)) {
            return;
        }
        delete(AppConstant.AVATAR_DIR, filename);
    }

    public void deleteCoverImage(String filename) {
        if (filename.equals(AppConstant.DEFAULT_COVERIMAGE)) {
            return;
        }
        delete(AppConstant.COVERIMAGE_DIR, filename);
    }

    public void deleteChapter(String bookName, String chapterName) {
        File file = new File(Path.of(AppConstant.BOOK_DIR, bookName, chapterName).toString());
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
        File folder = new File(Path.of(AppConstant.BOOK_DIR, bookName).toString());
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

    private boolean validFile(MultipartFile file, String extension) {
        if (file == null || file.isEmpty())
            return false;
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            if (fileName != null && 
            !fileName.isBlank() && 
            fileName.substring(fileName.lastIndexOf(".") + 1)
                .equals(extension))
                return true;
        }
        return false;
    }
}
