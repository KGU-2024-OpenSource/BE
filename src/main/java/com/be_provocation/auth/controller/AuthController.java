package com.be_provocation.auth.controller;

import com.be_provocation.auth.dto.request.GetVerificationRequest;
import com.be_provocation.auth.dto.request.LoginRequest;
import com.be_provocation.auth.dto.request.SignUpRequest;
import com.be_provocation.auth.dto.request.VerifyRequest;
import com.be_provocation.auth.service.AuthService;
import com.be_provocation.auth.service.EmailAsyncService;
import com.be_provocation.auth.service.VerificationService;
import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import com.be_provocation.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
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
    private final EmailAsyncService emailAsyncService;
    private final VerificationService verificationService;


    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입을 진행하는 API입니다.")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "로그인 API입니다.")
    public ApiResponse<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        authService.login(request, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @GetMapping("/test")
    @Operation(summary = "로그인 테스트 API", description = "로그인 여부를 확인할 수 있는 API입니다. 회원의 닉네임을 리턴합니다.")
    public ApiResponse<String> test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        String result = member.getNickname();
        return new ApiResponse<>(result);
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복확인 API", description = "닉네임의 중복 여부를 확인할 수 있는 API입니다..")
    public ApiResponse<Void> checkNickname(@RequestParam("nickname") String nickname) {

        authService.checkNickname(nickname);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/verification-code")
    @Operation(summary = "인증 코드 발행 API", description = "인증 코드를 경기대 메일에 전송하는 API입니다.")
    public ApiResponse<Void> sendVerificationCode(@RequestBody @Valid GetVerificationRequest request)
            throws MessagingException {
        String verificationCode = verificationService.generateVerificationCode(request.email());
        emailAsyncService.sendEmailAsync(request.email(), verificationCode);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/verify-code")
    @Operation(summary = "인증 코드 검증 API", description = "인증 코드를 검증하는 API입니다.")
    public ApiResponse<Void> verifyCode(@RequestBody VerifyRequest request) {
        verificationService.verifyCode(request);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }



}
