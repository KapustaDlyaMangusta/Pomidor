package com.chervonyi.pomidor.domain.shared;

import jakarta.annotation.Nullable;

public record ValueResult<TValue>(
        @Nullable TValue value,
        boolean isSuccess,
        ApplicationError error) {
    public @Nullable TValue getValue() {
        if (!isSuccess) {
            throw new IllegalStateException(
                    "The value of a failure result cannot be accessed.");
        }
        return value;
    }

    public static <TValue> ValueResult<TValue> of(
            final @Nullable TValue value) {
        return value != null
                ? success(value)
                : failure(ApplicationError.NULL_VALUE);
    }

    public static <TValue> ValueResult<TValue> of(
            final @Nullable TValue value,
            final ApplicationError error) {
        return value != null
                ? success(value)
                : failure(error);
    }

    private static <TValue> ValueResult<TValue> success(
            final TValue value) {
        return new ValueResult<>(
                value,
                true,
                ApplicationError.NONE);
    }

    private static <TValue> ValueResult<TValue> failure(
            final ApplicationError error) {
        return new ValueResult<>(
                null,
                false,
                error);
    }
}
