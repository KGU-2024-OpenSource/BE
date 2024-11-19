package com.be_provocation.auth.service;

import com.be_provocation.auth.dto.request.SignUpRequest;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import com.be_provocation.member.domain.Member;
import com.be_provocation.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
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

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {

        String email = request.email();
        String password = request.password();
        int index = request.profileImageIndex();

        String profileImageUrl = null; // 인덱스에 따른 이미지 링크 추가

        if (isMemberRegistered(email)) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        String encodedPassword = passwordEncoder.encode(password);

        Member member = request.toEntity(encodedPassword, profileImageUrl);

        memberRepository.save(member);

    }

    private Boolean isMemberRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }


    private void generateToken(String loginId, HttpServletResponse response) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, "default");

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        jwtTokenService.generateToken(authentication, response);
    }

}
