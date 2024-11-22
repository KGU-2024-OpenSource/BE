package com.be_provocation.auth.dto.request;

import jakarta.validation.constraints.Pattern;

public record GetVerificationRequest(
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@kyonggi\\.ac\\.kr$",
                message = "이메일은 @kyonggi.ac.kr로 끝나야 합니다.")
        String email
) {
}
