package com.be_provocation.auth.controller;

import com.be_provocation.auth.dto.request.SignUpRequest;
import com.be_provocation.auth.service.AuthService;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "회원 인증 관련 API")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입을 진행하는 API입니다. (SocialType : BASIC, GOOGLE, NAVER, KAKAO")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }


}
