package com.duyhelloworld.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class AppExceptionHandler {

    private void sout(Exception e, LocalDateTime time) {
        System.out.println("=================== " + String.format("%02d:%02d %02d/%02d/%d",
            time.getHour(), time.getMinute(), time.getDayOfMonth(), time.getMonthValue(), time.getYear()) + " =====================");
        System.out.println("\u001B[31m" + e.getClass().getName());
        System.out.println("\u001B[34m" + e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            System.out.println("\u001B[33m \t" + stackTraceElement);            
        }
        System.out.println("========================================");
        System.out.println("\u001B[0m");
        // e.printStackTrace();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorMessage> handleAppException(Exception ex) {
        sout(ex, LocalDateTime.now());
        if (ex instanceof AppException appEx) {
            return ResponseEntity
            .status(appEx.getStatusCode())
            .body(new ErrorMessage(appEx.getMessage()));
        } else if (ex instanceof MissingServletRequestPartException e) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage("Thiếu trường " + e.getRequestPartName()));
        } else if (ex instanceof NoHandlerFoundException nfe) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorMessage("Không tìm thấy đường dẫn " + nfe.getRequestURL()));
        } else if (ex instanceof AccessDeniedException) {
            return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorMessage("Bạn không có quyền truy cập vào tài nguyên này"));
        } else if (ex instanceof AuthenticationException) {
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorMessage("Xác thực lỗi. Vui lòng đăng nhập lại"));
        } else if (ex instanceof NoResourceFoundException) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorMessage("Không tìm thấy tài nguyên"));
        } else if (ex instanceof BadCredentialsException) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage("Sai tên đăng nhập hoặc mật khẩu. Vui lòng"));
        }
        else 
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Có lỗi ở server. Liên hệ admin để biết thêm"));
        }
}
