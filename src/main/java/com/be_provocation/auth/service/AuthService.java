package com.be_provocation.auth.service;

import com.be_provocation.auth.domain.ProfileNumber;
import com.be_provocation.auth.dto.request.LoginRequest;
import com.be_provocation.auth.dto.request.SignUpRequest;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {

        String email = request.email();
        String password = request.password();
        ProfileNumber profileNumber = request.profileNumber();

        String profileImageUrl = null; // 인덱스에 따른 이미지 링크 추가
        switch (profileNumber) {
            case ONE -> profileImageUrl = "http://localhost:8080/images/profile1.png";
            case TWO -> profileImageUrl = "http://localhost:8080/images/profile2.png";
            case THREE -> profileImageUrl = "http://localhost:8080/images/profile3.png";
            default -> throw CheckmateException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
        if (isMemberRegistered(email)) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        if(!verificationService.isVerified(email)) {
            throw CheckmateException.from(ErrorCode.VERIFICATION_REQUIRED);
        }

        verificationService.removeCode(email);

        String encodedPassword = passwordEncoder.encode(password);

        Member member = request.toEntity(encodedPassword, profileImageUrl);

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest request, HttpServletResponse response) {

        String email = request.email();
        String password = request.password();

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.INCORRECT_ACCOUNT));

        if(!passwordEncoder.matches(password, findMember.getPassword())) {
            throw CheckmateException.from(ErrorCode.INCORRECT_PASSWORD);
        }

        generateToken(email, password, response);

    }

    public void checkNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname)) {
            throw CheckmateException.from(ErrorCode.DUPLICATE_MEMBER_NICKNAME);
        }
    }

    private Boolean isMemberRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }


    private void generateToken(String email, String password, HttpServletResponse response) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        jwtTokenService.generateToken(authentication, response);
    }

}
