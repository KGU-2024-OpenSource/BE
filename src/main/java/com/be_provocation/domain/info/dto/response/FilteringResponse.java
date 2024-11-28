package com.be_provocation.domain.info.dto.response;

public record FilteringResponse
        (Long myInfoId,
         String nickname,
         String profileImageUrl,
         Integer studentId,
         Integer birthYear,
         String department,
         String desiredCloseness
        ) {
}
