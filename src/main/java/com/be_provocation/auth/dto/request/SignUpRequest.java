package com.be_provocation.auth.dto.request;

import com.be_provocation.member.domain.Gender;
import com.be_provocation.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@kyonggi\\.ac\\.kr$",
                message = "이메일은 @kyonggi.ac.kr로 끝나야 합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
                message = "비밀번호는 영문과 숫자를 혼합하여 10자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(max = 10, message = "닉네임은 10자 이내로 작성해야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$",
                message = "닉네임은 한글, 영문, 숫자, 공백만 포함할 수 있습니다.")
        String nickname,

        @NotBlank(message = "성별은 필수입니다.")
        Gender gender
) {
    public static Member toEntity()
}
