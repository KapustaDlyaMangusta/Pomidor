package com.chervonyi.pomidor.domain.shared;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ApplicationError(
        String code,
        String message,
        @Nullable HttpStatus httpStatus) {
    public static final ApplicationError NONE = new ApplicationError("", "", null);
    public static final ApplicationError NULL_VALUE =  new ApplicationError(
            "Error.NullValue",
            "The specified result value is null.",
            HttpStatus.INTERNAL_SERVER_ERROR);

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        return ((ApplicationError) obj).code.equals(code);
    }

    @Override
    public int hashCode() {
        return code.hashCode() + message.hashCode();
    }
}
