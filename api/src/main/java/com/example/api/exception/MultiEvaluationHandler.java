package com.example.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MultiEvaluationHandler {

    //バリデーションエラーをまとめて返す
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    

    //評価期間エラーなど（InvalidEvaluationPeriodException）を 400 で返す
    @ExceptionHandler(InvalidEvaluationPeriodException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPeriodException(InvalidEvaluationPeriodException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);  // ← INTERNAL_SERVER_ERROR → BAD_REQUEST に修正
    }

    //その他のランタイム例外（予期しないエラー）を 500 で返す
<<<<<<< HEAD
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "サーバーエラーが発生しました");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
=======
//     @ExceptionHandler(RuntimeException.class)
// public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
//     ex.printStackTrace(); // ← 標準出力に例外の詳細を出力

//     Map<String, String> error = new HashMap<>();
//     error.put("message", "サーバーエラーが発生しました");
//     return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
// }

>>>>>>> mizukami
}
