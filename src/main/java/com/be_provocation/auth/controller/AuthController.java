package com.be_provocation.auth.controller;

import com.be_provocation.auth.dto.request.LoginRequest;
import com.be_provocation.auth.dto.request.SignUpRequest;
import com.be_provocation.auth.service.AuthService;
import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import com.be_provocation.member.domain.Member;
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
    @Operation(summary = "회원가입 API", description = "회원가입을 진행하는 API입니다.")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "테스트용 기본 로그인 API입니다.")
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


}
