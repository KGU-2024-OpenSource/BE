package com.be_provocation.handler;

import com.be_provocation.exception.CheckmateException;
import com.be_provocation.global.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CheckmateException.class)
    public ApiResponse<Void> handle(CheckmateException e, HttpServletRequest request) {
        return new ApiResponse<>(e);
    }
}
