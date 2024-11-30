package com.be_provocation.domain.info.domain;

import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.Arrays;

public enum SnoringStatus {
    YES("O"),
    NO("X"),
    SOMETIMES("가끔");

    private final String displayName;

    SnoringStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SnoringStatus fromDisplayName(String displayName) {
        return Arrays.stream(SnoringStatus.values())
                .filter(status -> status.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> CheckmateException.from(ErrorCode.ENUM_CONVERSION_ERROR));
    }
}
