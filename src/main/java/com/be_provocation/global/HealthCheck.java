package com.be_provocation.global;

import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
@Tag(name = "Server Health", description = "서버가 정상 작동하는지 확인")
public class HealthCheck {
    @GetMapping
    @Operation(summary = "헬스 체크 API", description = "서버가 정상 작동하는지 확인합니다.")
    public String check() { return "OK";}

    @GetMapping("/test")
    @Operation(summary = "공통응답코드 체크 API", description = "공통응답코드 테스트 API")
    public ApiResponse<Void> test() {
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

}
