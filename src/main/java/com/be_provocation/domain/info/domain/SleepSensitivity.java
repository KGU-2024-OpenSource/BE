package com.be_provocation.domain.info.domain;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.Arrays;

public enum SleepSensitivity {
    LIGHT("밝음"),
    DEEP("어두움"),
    UNKNOWN("모름");

    private final String displayName;

    SleepSensitivity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SleepSensitivity fromDisplayName(String displayName) {
        return Arrays.stream(SleepSensitivity.values())
                .filter(status -> status.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR));
    }
}
