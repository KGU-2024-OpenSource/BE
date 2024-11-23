package com.be_provocation.auth.util;

import org.springframework.util.StringUtils;

public class LogSanitizerUtil {

    // 이메일 주소를 마스킹
    public static String sanitizeForLog(String email) {
        if (StringUtils.isEmpty(email) || !email.contains("@")) {
            return email; // 유효하지 않은 이메일은 그대로 반환
        }

        String[] parts = email.split("@");
        String localPart = parts[0]; // '@' 앞부분
        String domainPart = parts[1]; // '@' 뒷부분

        // 로컬 부분의 첫 글자만 남기고 나머지는 '*'
        String maskedLocalPart = localPart.charAt(0) + "****";

        return maskedLocalPart + "@" + domainPart;
    }
}
