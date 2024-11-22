package com.be_provocation.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VerificationCode {

    @Id
    private String email;               // 이메일 (PK)

    private String code;                // 인증 코드

    private LocalDateTime expirationTime; // 유효기간

    private boolean isVerified;

    // 정적 팩토리 메서드로 toEntity 구현
    public static VerificationCode toEntity(String email, String code) {
        VerificationCode entity = new VerificationCode();
        entity.email = email;
        entity.code = code;
        entity.expirationTime = LocalDateTime.now().plusMinutes(30); // 30분 유효기간
        entity.isVerified = false;
        return entity;
    }

    public void setVerified() {
        isVerified = true;
    }
}
