package com.spring.exam;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class CtrlAdvice {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<String> example(Exception exception,
                                             Object body,
                                             WebRequest request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("message:"+exception.getMessage());
    }

}
