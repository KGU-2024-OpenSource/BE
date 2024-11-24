package com.be_provocation.domain.info.domain;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.Arrays;

public enum SmokingStatus {
    NON_SMOKER("노담"),
    CIGARETTE("연초"),
    E_CIGARETTE("전담");

    private final String displayName;

    SmokingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SmokingStatus fromDisplayName(String displayName) {
        return Arrays.stream(SmokingStatus.values())
                .filter(status -> status.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR));
    }
}
