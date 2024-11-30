package com.be_provocation.domain.info.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.dto.response.DetailResponse;
import com.be_provocation.domain.info.dto.response.FilteringResponse;
import com.be_provocation.domain.info.dto.response.IamYouAreResponse;
import com.be_provocation.domain.info.service.InfoService;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.domain.SuccessCode;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @Operation(summary = "룸메이트 필터링 API", description = "필터링된 룸메이트의 정보를 가져오하는 API입니다.")
    public ApiResponse<FilteringResponse> filtering(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        return new ApiResponse<>(infoService.filtering(member));
    }

    @GetMapping("{myInfoId}")
    @Operation(summary = "룸메이트 정보 상세보기 API", description = "한 명의 룸메이트의 구체적인 정보를 가져오는 API입니다.")
    public ApiResponse<DetailResponse> getDetails(@PathVariable("myInfoId") Long myInfoId) {
        return new ApiResponse<>(infoService.getDetails(myInfoId));
    }

    @GetMapping("/my")
    @Operation(summary = "나의 나는너는 정보 조회하기 API", description = "로그인한 사용자가 작성한 나는 너는 정보를 반환하는 API입니다.")
    public ApiResponse<IamYouAreResponse> getMyIamYouAreInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        return new ApiResponse<IamYouAreResponse>(infoService.getMyIamYouAreInfo(member));
    }

}