package com.be_provocation.global.handler;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CheckmateException.class)
    public ApiResponse<Void> handle(CheckmateException e, HttpServletRequest request) {
        return new ApiResponse<>(e);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        // 첫 번째 발생한 검증 오류 메시지를 가져오기
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 오류 메시지로부터 ErrorCode를 찾기
        ErrorCode errorCode = ErrorCode.fromMessage(message);

        // ApiResponse 생성
        ApiResponse<Void> apiResponse = new ApiResponse<>(errorCode);

        // ResponseEntity로 ApiResponse를 반환
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
