package com.twitter_backend_spring_boot.twitter.exceptions;

import com.twitter_backend_spring_boot.twitter.logger.InjectLogger;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomException {

    @InjectLogger
    private Logger log;

    CustomException(){
        System.out.println("Configuring CustomException....");
    }

    @PostConstruct
    public void init(){
        log.info("CustomException are setup...");
    }

    private String getStackTraceLines(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
                .map(ste -> ste.toString())
                .filter(line -> line.contains("springproject1")) // restrict to your code
                .collect(Collectors.joining("\n"));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Map<String, Object>> handleHttpException(HttpException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of(
                        "statusCode", ex.getStatus().value(),
                        "error", getStackTraceLines(ex),
                        "message", ex.getMessage(),
                        "success", false
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "statusCode", HttpStatus.BAD_REQUEST.value(),
                        "error", getStackTraceLines(ex),
                        "message", errorMsg,
                        "success", false
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", getStackTraceLines(ex),
                        "message", ex.getMessage(),
                        "success", false
                ));
    }
}
