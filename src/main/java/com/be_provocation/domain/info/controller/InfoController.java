package com.be_provocation.domain.info.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.service.InfoService;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.domain.SuccessCode;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Tag(name = "InfoController", description = "룸메이트 정보 관련 API")
public class InfoController {

    private final InfoService infoService;

    @PostMapping
    @Operation(summary = "룸메이트 정보 등록 API", description = "나의 정보, 원하는 룸메이트의 정보를 등록하는 API입니다.")
    public ApiResponse<Void> save(@RequestBody InfoSaveRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        infoService.save(request, member);
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}