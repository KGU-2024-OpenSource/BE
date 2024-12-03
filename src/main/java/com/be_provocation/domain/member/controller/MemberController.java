package com.be_provocation.domain.member.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.dto.response.DetailResponse;
import com.be_provocation.domain.info.dto.response.FilteringResponse;
import com.be_provocation.domain.info.dto.response.IamYouAreResponse;
import com.be_provocation.domain.info.service.InfoService;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.dto.response.MemberResponse;
import com.be_provocation.domain.member.dto.response.NicknameResponse;
import com.be_provocation.domain.member.service.MemberService;
import com.be_provocation.global.domain.SuccessCode;
import com.be_provocation.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my")
    @Operation(summary = "내 프로필 조회하기 API", description = "로그인한 사용자의 프로필 정보를 반환하는 API입니다.")
    public ApiResponse<MemberResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        return new ApiResponse<MemberResponse>(memberService.getMyProfile(member));
    }

    @GetMapping("/nickname/{id}")
    @Operation(summary = "닉네임 조회하기 API", description = "아이디에 해당하는 사용자의 닉네임을 반환하는 API입니다.")
    public ApiResponse<NicknameResponse> getNickname(@PathVariable("id") Long id) {
        return new ApiResponse<NicknameResponse>(memberService.getNickname(id));
    }

}