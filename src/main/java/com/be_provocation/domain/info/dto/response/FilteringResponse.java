package com.be_provocation.domain.info.dto.response;

import com.be_provocation.domain.info.domain.MBTI;

public record FilteringResponse
        (Long myInfoId,
         String nickname,
         String profileImageUrl,
         MBTI mbti,
         Integer studentId,
         Integer birthYear,
         String department,
         String desiredCloseness
        ) {
}
