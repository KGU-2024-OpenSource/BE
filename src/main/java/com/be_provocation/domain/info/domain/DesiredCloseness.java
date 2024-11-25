package com.be_provocation.domain.info.domain;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.Arrays;

public enum DesiredCloseness {
    BUSINESS("비즈니스"),
    FRIEND("친구"),
    BEST_FRIEND("짱친");

    private final String displayName;

    DesiredCloseness(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DesiredCloseness fromDisplayName(String displayName) {
        return Arrays.stream(DesiredCloseness.values())
                .filter(status -> status.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR));
    }
}
