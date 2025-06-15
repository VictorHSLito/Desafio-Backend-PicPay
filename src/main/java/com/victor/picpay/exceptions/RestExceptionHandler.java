package com.victor.picpay.exceptions;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(PicPayException.class)
    public ProblemDetail picPayHandleException(PicPayException exception) {
        return exception.toProblemDetail();
    }
}
