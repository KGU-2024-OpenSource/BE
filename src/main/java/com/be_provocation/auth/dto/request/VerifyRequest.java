package com.be_provocation.auth.dto.request;

public record VerifyRequest(
        String email,
        String code
) {
}
