package ptit.edu.vn.service.file;

import org.springframework.web.multipart.MultipartFile;


public class FileValidatorService {
    
    public static boolean ValidFileAvatar(MultipartFile file) {
        if (file == null || file.isEmpty())
            return false;
        String fileName = file.getOriginalFilename(), contentType = file.getContentType();
        if (fileName != null) {
            if (fileName != null && 
            !fileName.isBlank() && 
            fileName.substring(fileName.lastIndexOf(".") + 1)
                .equals("png"))
                return true;
        }
        if (contentType != null) {
            if (contentType.equals("image/png"))
                return true;
        }
        return false;
    }

    public static boolean ValidFileChapter(MultipartFile file) {
        if (file == null || file.isEmpty())
            return false;
        String fileName = file.getOriginalFilename();
        if (fileName != null && 
            !fileName.isBlank() && 
            fileName.substring(fileName.lastIndexOf(".") + 1)
                .equals("jpg")) {
            return true;
        }
        return false;
    }
}
