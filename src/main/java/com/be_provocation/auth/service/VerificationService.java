package com.be_provocation.auth.service;

import com.be_provocation.auth.domain.VerificationCode;
import com.be_provocation.auth.dto.request.VerifyRequest;
import com.be_provocation.auth.repository.VerificationCodeRepository;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Transactional
    public String generateVerificationCode(String email) {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> verificationCode.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> verificationCode.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> verificationCode.append(random.nextInt(10)); // 숫자
            }
        }

        checkDuplicated(email);

        // 정적 팩토리 메서드를 사용해 엔터티 생성
        VerificationCode codeEntity = VerificationCode.toEntity(email, verificationCode.toString());

        verificationCodeRepository.save(codeEntity); // DB에 저장
        return verificationCode.toString();
    }

    @Transactional
    public void verifyCode(VerifyRequest request) {
        String email = request.email();
        String inputCode = request.code();

        VerificationCode storedCode = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        if(storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw CheckmateException.from(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if(!inputCode.equals(storedCode.getCode())) {
            throw CheckmateException.from(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }

        storedCode.setVerified();
    }

    @Transactional
    public void checkDuplicated(String email) {
        Optional<VerificationCode> code = verificationCodeRepository.findByEmail(email);
        if(code.isPresent()) {
            removeCode(email);
        }
    }

    @Transactional(readOnly = true)
    public boolean isVerified(String email) {
        VerificationCode storedCode = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.VERIFICATION_REQUIRED));

        return storedCode.isVerified();
    }

    @Transactional
    public void removeCode(String email) {
        verificationCodeRepository.deleteById(email);
    }
}
