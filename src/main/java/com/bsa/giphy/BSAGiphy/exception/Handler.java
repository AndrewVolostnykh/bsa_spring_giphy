package com.bsa.giphy.BSAGiphy.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public final class Handler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleNoBsaGiphyException(NoBsaGiphyHeaderException ex) {
        return new ResponseEntity<>(Map.of("message", "Request not assembled necessary headers!"), HttpStatus.FORBIDDEN);
//        return ResponseEntity
//                .unprocessableEntity()
//                .body(Map.of("message", ex.getMessage() == null ? "Request not assembled necessary headers" : ex.getMessage()));
    }


}
