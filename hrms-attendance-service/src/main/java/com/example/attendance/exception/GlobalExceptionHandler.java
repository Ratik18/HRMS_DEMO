package com.example.attendance.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AttendanceException.class)
    public ResponseEntity<?> handleAttendanceException(
            AttendanceException ex) {

        return ResponseEntity.badRequest().body(
            Map.of(
                "timestamp", OffsetDateTime.now(),
                "status", 400,
                "error", ex.getMessage()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(
            Exception ex) {

        return ResponseEntity.status(
            HttpStatus.INTERNAL_SERVER_ERROR
        ).body(
            Map.of(
                "timestamp", OffsetDateTime.now(),
                "status", 500,
                "error", "Internal server error"
            )
        );
    }
}