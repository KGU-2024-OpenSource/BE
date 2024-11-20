package com.be_provocation.auth.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
