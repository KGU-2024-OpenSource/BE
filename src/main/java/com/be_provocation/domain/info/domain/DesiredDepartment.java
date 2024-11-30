package com.be_provocation.domain.info.domain;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.Arrays;

public enum DesiredDepartment {
    SAME("같아야 함"),
    DIFFERENT("달라야 함"),
    NO_MATTER("상관 없음");

    private final String displayName;

    DesiredDepartment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DesiredDepartment fromDisplayName(String displayName) {
        return Arrays.stream(DesiredDepartment.values())
                .filter(status -> status.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR));
    }
}
