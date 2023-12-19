package ptit.edu.vn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class AppExceptionHandler {

    private void sout(Exception e) {
        System.out.println("========================================");
        System.out.println("\u001B[31m" + e.getClass().getName());
        System.out.println("\u001B[34m" + e.getMessage());
        System.out.println("\u001B[33m" + e.getStackTrace()[0]);
        System.out.println("========================================");
        System.out.println("\u001B[0m");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorMessage> handleAppException(Exception ex) {
        sout(ex);
        if (ex instanceof AppException appEx) {
            return ResponseEntity
            .status(appEx.getStatusCode())
            .body(new ErrorMessage(appEx.getMessage()));
        } else if (ex instanceof MissingServletRequestPartException e) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage("Thiếu trường " + e.getRequestPartName()));
        }
        else 
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Có lỗi ở server. Liên hệ admin để biết thêm"));
        }
}
