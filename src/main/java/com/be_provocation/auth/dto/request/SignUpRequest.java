package com.be_provocation.auth.dto.request;

import com.be_provocation.auth.domain.ProfileNumber;
import com.be_provocation.domain.member.domain.Gender;
import com.be_provocation.domain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@kyonggi\\.ac\\.kr$",
                message = "이메일은 @kyonggi.ac.kr로 끝나야 합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
                message = "비밀번호는 영문과 숫자를 혼합하여 10자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotNull(message = "성별은 필수입니다.")
        Gender gender,

        @NotNull(message = "프로필 이미지 선택은 필수입니다.")
        ProfileNumber profileNumber

) {
    public Member toEntity(String encodedPassword, String profileImageUrl) {
            return Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .profileImageUrl(profileImageUrl)
                    .nickname(nickname)
                    .gender(gender)
                    .profileImageUrl(profileImageUrl)
                    .build();
    }
}
